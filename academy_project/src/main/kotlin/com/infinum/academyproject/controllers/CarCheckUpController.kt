package com.infinum.academyproject.controllers

import com.infinum.academyproject.dto.AddCarCheckUpDTO
import com.infinum.academyproject.dto.CarCheckUpDTO
import com.infinum.academyproject.services.CarService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/car-checkups")
class CarCheckUpController (
    private val carService: CarService
) {

    @PostMapping("/add")
    fun addCarCheckUp(@RequestBody carCheckUp: AddCarCheckUpDTO) : ResponseEntity<CarCheckUpDTO> {
        CarController.log.info("Adding car check-up ${carCheckUp}.")
        val carCheckUpDTO : CarCheckUpDTO = carService.addCarCheckUp(carCheckUp)
        return ResponseEntity(carCheckUpDTO, HttpStatus.OK)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleException(ex:Exception): ResponseEntity<String>{
        CarController.log.error("Error occurred", ex)
        return ResponseEntity("Error occurred", HttpStatus.BAD_REQUEST)
    }
}