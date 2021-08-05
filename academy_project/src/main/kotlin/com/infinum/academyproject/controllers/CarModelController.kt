package com.infinum.academyproject.controllers

import com.infinum.academyproject.controllers.CarController.Companion.log
import com.infinum.academyproject.dto.CarModelDTO
import com.infinum.academyproject.resources.CarModelResource
import com.infinum.academyproject.resources.assemblers.CarModelResourceAssembler
import com.infinum.academyproject.services.CarService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/models")
class CarModelController (
    private val carService: CarService,
    private val carModelResourceAssembler: CarModelResourceAssembler
    ){

    @GetMapping
    fun getExistingModels(pageable: Pageable, pagedResourcesAssembler: PagedResourcesAssembler<CarModelDTO>)
        : ResponseEntity<PagedModel<CarModelResource>> {
        log.info("Fetching all car models in base.")
        val page = carService.getAllExistingModelsInBase(pageable)
        return ResponseEntity(pagedResourcesAssembler.toModel(page, carModelResourceAssembler), HttpStatus.OK)
    }
}