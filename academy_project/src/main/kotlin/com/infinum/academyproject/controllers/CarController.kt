package com.infinum.academyproject.controllers

import com.infinum.academyproject.dto.AddCarDTO
import com.infinum.academyproject.dto.CarCheckUpDTO
import com.infinum.academyproject.dto.CarDTO
import com.infinum.academyproject.errors.IllegalCarModelException
import com.infinum.academyproject.errors.NoCarIdException
import com.infinum.academyproject.resources.CarCheckUpResource
import com.infinum.academyproject.resources.CarResource
import com.infinum.academyproject.resources.assemblers.CarCheckUpResourceAssembler
import com.infinum.academyproject.resources.assemblers.CarResourceAssembler
import com.infinum.academyproject.services.CarService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
@RequestMapping("/cars")
class CarController(
    private val carService: CarService,
    private val carResourceAssembler: CarResourceAssembler,
    private val carCheckUpResourceAssembler: CarCheckUpResourceAssembler
    //private val scheduleService: SchedulingService
) {

    @PostMapping
    fun addCar(@RequestBody car: AddCarDTO): ResponseEntity<Unit> {
        log.info("Adding car ${car}.")
        val carDTO: CarDTO = carService.addCar(car)
        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/${carDTO.id}")
            .buildAndExpand()
            .toUri()
        return ResponseEntity.created(location).build()
    }


    @GetMapping("/{id}")
    fun getCarWithCheckUp(@PathVariable id: Long): ResponseEntity<CarResource> {
        log.info("Fetching car check-ups for car id ${id}.")
        val car = carService.getCarCheckUps(id)
        return ResponseEntity(carResourceAssembler.toModel(car), HttpStatus.OK) // poziva samo sebe, smisli novu putanju u toModel
    }

    @GetMapping
    fun getCarsPaged(
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarDTO>
    ): ResponseEntity<PagedModel<CarResource>> {
        log.info("Fetching one car page.")
        val page = carService.getCarsPaged(pageable)
        return ResponseEntity(pagedResourcesAssembler.toModel(page, carResourceAssembler), HttpStatus.OK)
    }

    @GetMapping("/{id}/checkups")
    fun getCheckUpsPaged(
        pageable: Pageable, @PathVariable id: Long,
        pagedResourcesAssembler: PagedResourcesAssembler<CarCheckUpDTO>
    ): ResponseEntity<PagedModel<CarCheckUpResource>> {
        log.info("Fetching check-ups page for car id $id.")
        val page = carService.getCheckUpsPaged(pageable, id)
        return ResponseEntity(pagedResourcesAssembler.toModel(page, carCheckUpResourceAssembler), HttpStatus.OK) // pokusaj apply dodat

    }

    @ExceptionHandler(value = [NoCarIdException::class, IllegalCarModelException::class])
    fun handleCustomException(ex: Exception): ResponseEntity<String> {
        log.error("${ex.message}", ex)
        return ResponseEntity("Error occurred. ${ex.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleRuntimeException(ex: Exception): ResponseEntity<String> {
        log.error("${ex.message}", ex)
        return ResponseEntity("Runtime Error occurred. ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(CarController::class.java)
    }

//    @GetMapping("/update-models")
//    fun updateModelsCheck() {
//        log.info("Updating models.")
//        scheduleService.updateModels()
//    }

}
