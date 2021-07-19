package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class CarRepository {

    private val cars = mutableMapOf<Long, Car>()
    private val carCheckUps = mutableMapOf<Long, CarCheckUp>()


    fun addCar(car: Car): Long {
        val id  = (cars.keys.maxOrNull() ?: 0) + 1
        cars[id] = car
        return id
    }

    fun addCheckUp(checkUp: CarCheckUp): Long {
        val car = cars[checkUp.carId] ?: throw RuntimeException("No car with id.")
        car.checkUps.add(checkUp)
        val id = (carCheckUps.keys.maxOrNull() ?: 0) + 1
        carCheckUps[id] = checkUp
        return id
    }

    fun getCarCheckUps(carId: Long) : List<CarCheckUp> {
        val car = cars[carId] ?: throw RuntimeException("No car with that id")
        return car.checkUps.sortedWith(compareBy { it.timeAndDate }).reversed()
    }

    fun clearCars() = cars.clear()

    fun clearCheckUps() = carCheckUps.clear()
}