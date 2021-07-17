package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.springframework.stereotype.Component

@Component
class CarRepository {

    private val cars = mutableMapOf<Long, Car>()
    private val carCheckUps = mutableMapOf<Long, CarCheckUp>()


    fun addCar(car: Car): Long {
        val id  = (cars.keys.maxOrNull() ?: 0) + 1
        cars.put(id, car)
        return id
    }

    fun addCheckUp(checkUp: CarCheckUp): Long {
        val id = (carCheckUps.keys.maxOrNull() ?: 0) + 1
        carCheckUps.put(id, checkUp)
        return id
    }
}