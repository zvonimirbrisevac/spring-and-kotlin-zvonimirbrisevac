package com.infinum.academyproject.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.persistence.*
import kotlin.time.measureTimedValue

@Entity
@Table(name = "checkups")
data class CarCheckUp(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHECKUP_SEQ")
    @SequenceGenerator(name = "CHECKUP_SEQ", sequenceName = "CHECKUP_SEQ", allocationSize = 1)
    val id: Long = 0,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val timeAndDate: LocalDateTime,
    val workerName: String,
    val price: Double,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    val car: Car
)
