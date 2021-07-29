package com.infinum.academyproject.models

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "models")
data class CarModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MODEL_SEQ")
    @SequenceGenerator(name = "MODEL_SEQ", sequenceName = "MODEL_SEQ", allocationSize = 1)
    val id : Long = 0,
    val manufacturer : String,
    val modelName : String,
    val isCommon : Boolean
)
