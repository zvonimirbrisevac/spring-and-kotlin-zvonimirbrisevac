package com.infinum.academyproject.services

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.repositories.CarRepository
import org.springframework.stereotype.Service

@Service
class CarService(
    private var carRepository: CarRepository,
) {

    fun addCar(car: Car): Long = carRepository.addCar(car)

    fun addCarCheckUp(checkUp: CarCheckUp) : Long = carRepository.addCheckUp(checkUp)

    fun getCarCheckUps(carId: Long) : List<CarCheckUp> = carRepository.getCarCheckUps(carId)

    fun deleteAllCars() = carRepository.clearCars()

    fun deleteAllCarCheckUps() = carRepository.clearCheckUps()

}