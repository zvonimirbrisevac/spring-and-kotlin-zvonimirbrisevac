package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import org.springframework.data.repository.Repository

interface CarRepository : Repository<Car, Long> {

    fun save(car: Car) : Car

    fun findById(id: Long) : Car?

}
