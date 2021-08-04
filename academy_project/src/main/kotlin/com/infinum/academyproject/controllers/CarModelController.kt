package com.infinum.academyproject.controllers

import com.infinum.academyproject.controllers.CarController.Companion.log
import com.infinum.academyproject.resources.assemblers.CarModelResourceAssembler
import com.infinum.academyproject.services.CarService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.awt.print.Pageable

@Controller
@RequestMapping("/models")
class CarModelController (
    private val carService: CarService,
    private val carModelResourceAssembler: CarModelResourceAssembler
    ){

    @GetMapping
    fun getExistingModels(pageable: Pageable) {
        log.info("Fetching all car models in base.")

    }
}