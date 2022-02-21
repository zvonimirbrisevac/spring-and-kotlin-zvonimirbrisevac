package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.CarModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface ModelRepository : Repository<CarModel, Long> {

    fun findById(id : Long) : CarModel?

    fun findAll() : List<CarModel>

    fun findByManufacturerAndModelName(manufacturer : String, modelName : String) : CarModel?

    fun save(model: CarModel) : CarModel

    fun saveAll(models: Iterable<CarModel>) : List<CarModel>

    fun deleteAll()

    @Query(value = "select distinct m.id, m.manufacturer, m.model_name, m.is_common from cars c join models m " +
            "on m.id = c.model_id",
        countQuery = "select count(distinct(m.id, m.manufacturer, m.model_name, m.is_common)) from cars c join models m " +
                "on m.id = c.model_id",
        nativeQuery = true)
    fun getAllExistingModelsInBase(pageable: Pageable) : Page<CarModel>


}