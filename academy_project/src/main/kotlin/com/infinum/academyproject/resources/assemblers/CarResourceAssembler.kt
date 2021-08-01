package com.infinum.academyproject.resources.assemblers

import com.infinum.academyproject.controllers.CarController
import com.infinum.academyproject.dto.CarCheckUpDTO
import com.infinum.academyproject.dto.CarDTO
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.resources.CarResource
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo

class CarResourceAssembler : RepresentationModelAssemblerSupport<CarDTO, CarResource>(
       CarController::class.java, CarResource::class.java
    ){
    override fun toModel(entity: CarDTO): CarResource {

        val noPagination = Pageable.unpaged()
        val nullAssembler = PagedResourcesAssembler<CarCheckUpDTO>(null, null)

        return createModelWithId(entity.id, entity).apply {
            add(
                linkTo<CarController> {
                    getCheckUpsPaged(noPagination, entity.id, nullAssembler)
                }.withRel("check-ups")
            )
        }
    }

    override fun instantiateModel(entity: CarDTO): CarResource {
        return CarResource(entity.id, entity.ownerId, entity.addedDate, entity.manufacturer, entity.model,
                                entity.productionYear, entity.serialNumber)
    }

}