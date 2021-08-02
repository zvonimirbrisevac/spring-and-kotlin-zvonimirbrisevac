package com.infinum.academyproject.resources.assemblers

import com.infinum.academyproject.controllers.CarController
import com.infinum.academyproject.dto.CarCheckUpDTO
import com.infinum.academyproject.resources.CarCheckUpResource
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component

@Component
class CarCheckUpResourceAssembler :
    RepresentationModelAssemblerSupport<CarCheckUpDTO, CarCheckUpResource>(
        CarController::class.java, CarCheckUpResource::class.java
    ) {


    override fun toModel(entity: CarCheckUpDTO): CarCheckUpResource {
        return createModelWithId(entity.id, entity).apply {
            add(
                linkTo<CarController> {
                    getCarWithCheckUp(entity.car.id)
                }.withRel("car")
            )
        }
    }

    override fun instantiateModel(entity: CarCheckUpDTO): CarCheckUpResource {
        return CarCheckUpResource(entity.id, entity.timeAndDate, entity.workerName, entity.price)
    }
}