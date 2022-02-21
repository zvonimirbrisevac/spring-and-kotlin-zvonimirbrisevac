package com.infinum.academyproject.resources

import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDate

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
data class CarResource(
    val id: Long,
    val ownerId: Long,
    val addedDate: LocalDate,
    val manufacturer: String,
    val model: String,
    val productionYear: Int,
    val serialNumber: String
) : RepresentationModel<CarResource>()