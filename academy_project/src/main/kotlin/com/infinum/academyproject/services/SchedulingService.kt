package com.infinum.academyproject.services

import com.infinum.academyproject.dto.AddCarModelDTO
import com.infinum.academyproject.repositories.ModelRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SchedulingService(
    private val modelRepository: ModelRepository,
    private val httpService: HttpCarModelService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun updateModels() {
        logger.info("Fetching models from page...")
        val modelList: List<AddCarModelDTO>? = httpService.getCarModels()
        if (!modelList.isNullOrEmpty()) {
            logger.info("Updating database...")
            for (model in modelList) {
                modelRepository.findByManufacturerAndModelName(model.manufacturer, model.modelName)
                    ?: modelRepository.save(model.toCarModel())
            }
            logger.info("Updating finished.")
        }
    }
}