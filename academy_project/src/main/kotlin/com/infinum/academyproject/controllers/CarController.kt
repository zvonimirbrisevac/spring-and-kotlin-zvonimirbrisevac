package com.infinum.academyproject.controllers

import com.infinum.academyproject.dto.*
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.services.CarService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.awt.print.Pageable

@Controller
class CarController (
    private val carService: CarService
) {

    @PostMapping("/add-car")
    fun addCar(@RequestBody car : AddCarDTO) : ResponseEntity<CarDTO> {
        log.info("Adding car ${car}.")
        val carDTO : CarDTO = carService.addCar(car)
        return ResponseEntity(carDTO, HttpStatus.OK)
    }

    @PostMapping("/add-car-checkup")
    fun addCarCheckUp(@RequestBody carCheckUp: AddCarCheckUpDTO) : ResponseEntity<CarCheckUpDTO> {
        log.info("Adding car check-up ${carCheckUp}.")
        val carCheckUpDTO : CarCheckUpDTO = carService.addCarCheckUp(carCheckUp)
        return ResponseEntity(carCheckUpDTO, HttpStatus.OK)
    }

    @GetMapping("/get-car-checkups/{id}")
    fun getCarCheckUp(@PathVariable id : Long) : ResponseEntity<CarWithCheckUpsDTO> {
        log.info("Fetching car check-ups for car id ${id}.")
        val checkUps = carService.getCarCheckUps(id)
        return ResponseEntity(checkUps, HttpStatus.OK)
    }

    /*@GetMapping("/all-cars-paged")
    fun getCarsPaged(pageable: Pageable): Page<Car> {
        log.info("Fetching one car page.")

    }

    @GetMapping("/get-car-checkup-paged/{id}")
    fun getCheckUpsPaged(pageable: Pageable, @PathVariable id: Long) : Page<List<CarCheckUp>> {
        log.info("Fetching check up page.")

    }*/

    @ExceptionHandler(value = [(Exception::class)])
    fun handleException(ex:Exception): ResponseEntity<String>{
        log.error("Error occurred", ex)
        return ResponseEntity("Error occurred", HttpStatus.BAD_REQUEST)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(CarController::class.java)
    }

}
