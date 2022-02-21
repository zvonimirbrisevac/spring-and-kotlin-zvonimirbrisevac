package com.infinum.academyproject.models

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "cars")
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_SEQ")
    @SequenceGenerator(name = "CAR_SEQ", sequenceName = "CAR_SEQ", allocationSize = 1)
    @JoinColumn(name = "id")
    val id: Long = 0,

    val ownerId: Long,
    val addedDate: LocalDate = LocalDate.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    val model : CarModel,
    val productionYear: Int,
    val serialNumber: String

)
