package com.infinum.academyproject.services

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.repositories.CarRepository
import org.springframework.stereotype.Service

@Service
class CarService(
    private var carRepository: CarRepository,
) {

    fun addCar(car: Car): Long = carRepository.addCar(car) ?: throw RuntimeException("Failed to add car.")

    fun addCarCheckUp(checkUp: CarCheckUp) : Long = carRepository.addCheckUp(checkUp) ?: throw RuntimeException("Failed to add car check up.")

    fun getCarCheckUps(carId: Long) : Car? {
        val car : Car = carRepository.getCarById(carId) ?: throw RuntimeException("No car with that id.")
        car.checkUps = carRepository.getCarCheckUps(carId) ?: throw RuntimeException("No car id.")
        return car
    }

    fun deleteAllCars() = carRepository.clearCars()

    fun deleteAllCarCheckUps() = carRepository.clearCheckUps()

}