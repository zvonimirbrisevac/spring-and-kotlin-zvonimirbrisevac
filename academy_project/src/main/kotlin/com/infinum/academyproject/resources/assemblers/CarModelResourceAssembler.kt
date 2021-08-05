package com.infinum.academyproject.resources.assemblers

import com.infinum.academyproject.controllers.CarModelController
import com.infinum.academyproject.dto.CarModelDTO
import com.infinum.academyproject.resources.CarModelResource
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.stereotype.Component

@Component
class CarModelResourceAssembler : RepresentationModelAssemblerSupport<CarModelDTO, CarModelResource>(
    CarModelController::class.java, CarModelResource::class.java
) {
    override fun toModel(entity: CarModelDTO): CarModelResource {
        return createModelWithId(entity.id, entity)
    }

    override fun instantiateModel(entity: CarModelDTO): CarModelResource {
        return CarModelResource(entity.manufacturer, entity.modelName)
    }

}