package com.infinum.academyproject.resources.assemblers

import com.infinum.academyproject.controllers.CarController
import com.infinum.academyproject.dto.CarCheckUpDTO
import com.infinum.academyproject.resources.CarCheckUpResource
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport

class CarCheckUpsPagedResourcesAssembler :
    RepresentationModelAssemblerSupport<CarCheckUpDTO, CarCheckUpResource>(
        CarController::class.java, CarCheckUpResource::class.java
    ) {


    override fun toModel(entity: CarCheckUpDTO): CarCheckUpResource {
        return createModelWithId(entity.id, entity)
    }

    override fun instantiateModel(entity: CarCheckUpDTO): CarCheckUpResource {
        return CarCheckUpResource(entity.id, entity.timeAndDate, entity.workerName, entity.price)
    }
}