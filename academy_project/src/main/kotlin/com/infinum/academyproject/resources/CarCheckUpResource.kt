package com.infinum.academyproject.resources

import com.infinum.academyproject.models.Car
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
data class CarCheckUpResource(
    val id: Long,
    val timeAndDate: LocalDateTime,
    val workerName: String,
    val price: Double,
): RepresentationModel<CarCheckUpResource>()
