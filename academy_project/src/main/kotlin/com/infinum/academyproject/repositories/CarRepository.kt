package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository

interface CarRepository : Repository<Car, Long> {

    fun save(car: Car) : Car

    fun findById(id: Long) : Car?

    fun findByOwnerIdAndSerialNumber(ownerId : Long, serialNumber : String) : Car

    fun findAll(pageable: Pageable) : Page<Car>

    fun findAll() : List<Car>

    fun saveAll(cars: Iterable<Car>) : List<Car>

    fun deleteAll()

    fun deleteById(id: Long)

}
