package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository

interface CarRepository : Repository<Car, Long> {

    fun save(car: Car) : Car

    fun findById(id: Long) : Car?

    fun findAll(pageable: Pageable) : Page<Car>

    fun saveAll(cars: Iterable<Car>) : List<Car>

}
