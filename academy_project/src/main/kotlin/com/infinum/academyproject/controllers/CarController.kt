package com.infinum.academyproject.controllers

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.services.CarService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class CarController (
    private val carService: CarService
) {

    @PostMapping("/add-car")
    fun addCar(@RequestBody car : Car) : ResponseEntity<Car> {
        log.info("Adding car ${car}.")
        carService.addCar(car)
        return ResponseEntity(car, HttpStatus.OK)
    }

    @PostMapping("/add-car-checkup")
    fun addCarCheckUp(@RequestBody carCheckUp: CarCheckUp) : ResponseEntity<CarCheckUp> {
        log.info("Adding car check-up ${carCheckUp}.")
        carService.addCarCheckUp(carCheckUp)
        return ResponseEntity(carCheckUp, HttpStatus.OK)
    }

    @GetMapping("/get-car-checkups/{id}")
    fun getCarCheckUp(@PathVariable id : Long) : ResponseEntity<List<CarCheckUp>> {
        log.info("Fetching car check-ups for car id ${id}.")
        val checkUps = carService.getCarCheckUps(id)
        return ResponseEntity(checkUps, HttpStatus.OK)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleException(ex:Exception): ResponseEntity<String>{
        log.error("Error occurred", ex)
        return ResponseEntity("Error occurred", HttpStatus.BAD_REQUEST)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(CarController::class.java)
    }

}
