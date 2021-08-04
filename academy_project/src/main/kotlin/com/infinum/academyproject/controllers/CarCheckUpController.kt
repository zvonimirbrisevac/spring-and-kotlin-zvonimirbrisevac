package com.infinum.academyproject.controllers

import com.infinum.academyproject.controllers.CarController.Companion.log
import com.infinum.academyproject.dto.AddCarCheckUpDTO
import com.infinum.academyproject.dto.CarCheckUpDTO
import com.infinum.academyproject.models.CarCheckUpDurationFromNow
import com.infinum.academyproject.resources.CarCheckUpResource
import com.infinum.academyproject.resources.assemblers.CarCheckUpResourceAssembler
import com.infinum.academyproject.services.CarService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
@RequestMapping("/car-checkups")
class CarCheckUpController(
    private val carService: CarService,
    private val carCheckUpResourceAssembler: CarCheckUpResourceAssembler

) {

    @PostMapping
    fun addCarCheckUp(@RequestBody carCheckUp: AddCarCheckUpDTO): ResponseEntity<Unit> {
        log.info("Adding car check-up ${carCheckUp}.")
        val carCheckUpDTO: CarCheckUpDTO = carService.addCarCheckUp(carCheckUp)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/${carCheckUpDTO.id}")
            .buildAndExpand()
            .toUri()
        return ResponseEntity.created(location).build()
    }


    @GetMapping
    fun getLastTenCheckUps(pagedResourcesAssembler: PagedResourcesAssembler<CarCheckUpDTO>):
            ResponseEntity<PagedModel<CarCheckUpResource>>{
        log.info("Fetching last 10 check-ups.")
        // val pageable: Pageable = Pageable.ofSize(10).withPage(0)
        val pageable: Pageable = PageRequest.of(0, 10)
        val page = carService.getLastTenCheckUps(pageable)
        return ResponseEntity(pagedResourcesAssembler.toModel(page, carCheckUpResourceAssembler), HttpStatus.OK)

    }

    @GetMapping("/upcoming")
    fun getUpcomingCheckUps(
        @RequestParam(defaultValue = "ONE_MONTH") duration: CarCheckUpDurationFromNow, pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarCheckUpDTO>
    ) : ResponseEntity<PagedModel<CarCheckUpResource>>{
        log.info("Fetching upcoming checkups.")
        val page = carService.getUpcomingCheckUps(duration, pageable)
        return ResponseEntity(pagedResourcesAssembler.toModel(page, carCheckUpResourceAssembler), HttpStatus.OK)
    }

    @GetMapping("{id}/delete")
    fun deleteCheckUp(@PathVariable id: Long) : ResponseEntity<Unit> {
        log.info("Deleting car check-up with id $id.")
        carService.delereCarCheckUp(id)
        return ResponseEntity.noContent().build()
    }



    @ExceptionHandler(value = [(Exception::class)])
    fun handleException(ex: Exception): ResponseEntity<String> {
        log.error("Error occurred", ex)
        return ResponseEntity("Error occurred", HttpStatus.BAD_REQUEST)
    }
}