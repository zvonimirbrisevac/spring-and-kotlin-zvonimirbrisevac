package com.infinum.academyproject.models

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "cars")
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_SEQ")
    @SequenceGenerator(name = "CAR_SEQ", sequenceName = "CAR_SEQ", allocationSize = 1)
    val id: Long = 0,

    val ownerId: Long,
    val addedDate: LocalDate = LocalDate.now(),
    val manufacturer: String,
    val productionYear: Int,
    val serialNumber: String

)
