package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository

interface CheckUpRepository : Repository<CarCheckUp, Long> {

    fun save(checkUp: CarCheckUp): CarCheckUp

    fun findByCar(car: Car) : List<CarCheckUp>

    fun findAll(pageable: Pageable) : Page<CarCheckUp>

    fun saveAll(checkUps: Iterable<CarCheckUp>) : List<CarCheckUp>


}