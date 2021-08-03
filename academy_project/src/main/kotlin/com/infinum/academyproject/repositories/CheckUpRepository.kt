package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository

interface CheckUpRepository : Repository<CarCheckUp, Long> {

    fun save(checkUp: CarCheckUp): CarCheckUp

    fun findByCarOrderByTimeAndDateDesc(car: Car) : List<CarCheckUp>

    fun findAllByCarOrderByTimeAndDateDesc(pageable: Pageable, car : Car) : Page<CarCheckUp>

    fun saveAll(checkUps: Iterable<CarCheckUp>) : List<CarCheckUp>

    fun deleteAll()

    fun findAll() : List<CarCheckUp>

    fun findByWorkerNameAndPrice(workerName : String, price : Double) : CarCheckUp

}