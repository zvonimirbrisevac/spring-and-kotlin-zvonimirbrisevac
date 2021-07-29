package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.CarModel
import org.springframework.data.repository.Repository

interface ModelRepository : Repository<CarModel, Long> {

    fun findById(id : Long) : CarModel?

    fun findAll() : List<CarModel>

    fun findByManufacturerAndModelName(manufacturer : String, modelName : String) : CarModel?

    fun save(model: CarModel) : CarModel

    fun saveAll(models: Iterable<CarModel>) : List<CarModel>

    fun deleteAll()



}