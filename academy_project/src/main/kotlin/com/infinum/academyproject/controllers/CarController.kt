package com.infinum.academyproject.controllers

import com.infinum.academyproject.dto.*
import com.infinum.academyproject.errors.IllegalCarModelException
import com.infinum.academyproject.errors.NoCarIdException
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.services.CarService
import com.infinum.academyproject.services.SchedulingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

@Controller
@RequestMapping("/cars")
class CarController (
    private val carService: CarService,
    private val scheduleService: SchedulingService
) {

    @PostMapping("/add")
    fun addCar(@RequestBody car : AddCarDTO) : ResponseEntity<CarDTO> {
        log.info("Adding car ${car}.")
        val carDTO : CarDTO = carService.addCar(car)
        return ResponseEntity(carDTO, HttpStatus.OK)
    }


    @GetMapping("/{id}/checkups")
    fun getCarCheckUp(@PathVariable id : Long) : ResponseEntity<CarWithCheckUpsDTO> {
        log.info("Fetching car check-ups for car id ${id}.")
        val checkUps = carService.getCarCheckUps(id)
        return ResponseEntity(checkUps, HttpStatus.OK)
    }

    @GetMapping("/paged")
    fun getCarsPaged(pageable: Pageable): ResponseEntity<Page<CarDTO>> {
        log.info("Fetching one car page.")
        val page = carService.getCarsPaged(pageable)
        return ResponseEntity(page, HttpStatus.OK)
    }

    @GetMapping("/{id}/checkups-paged")
    fun getCheckUpsPaged(pageable: Pageable, @PathVariable id: Long) : ResponseEntity<Page<CarCheckUpNoCarDTO>> {
        log.info("Fetching check up page.")
        val page = carService.getCheckUpsPaged(pageable, id)
        return ResponseEntity(page, HttpStatus.OK)

    }

    @ExceptionHandler(value = [NoCarIdException::class, IllegalCarModelException::class])
    fun handleCustomException(ex:Exception): ResponseEntity<String>{
        log.error("${ex.message}", ex)
        return ResponseEntity("Error occurred. ${ex.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleRuntimeException(ex:Exception): ResponseEntity<String>{
        log.error("${ex.message}", ex)
        return ResponseEntity("Runtime Error occurred. ${ex.message}", HttpStatus.BAD_REQUEST)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(CarController::class.java)
    }

    @GetMapping("/update-models")
    fun updateModelsCheck() {
        log.info("Updating models.")
        scheduleService.updateModels()
    }

    @PostMapping("/add-models-for-tests")
    fun addModelTest(@RequestBody carModelDTO: AddCarModelDTO) {

    }
}
