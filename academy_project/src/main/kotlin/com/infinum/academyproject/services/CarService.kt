package com.infinum.academyproject.services

import com.infinum.academyproject.dto.*
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.repositories.CarRepository
import com.infinum.academyproject.repositories.CheckUpRepository
import org.springframework.stereotype.Service

@Service
class CarService(
    private val carRepository: CarRepository,
    private val checkUpRepository : CheckUpRepository
) {

    fun addCar(car: AddCarDTO): CarDTO = CarDTO(carRepository.save(car.toCar())) // ?: throw RuntimeException("Failed to add car.")

    fun addCarCheckUp(checkUp: AddCarCheckUpDTO) : CarCheckUpDTO = CarCheckUpDTO(checkUpRepository.save(checkUp.toCarCheckUp())) // ?: throw RuntimeException("Failed to add car check up.")


    fun getCarCheckUps(id: Long): CarWithCheckUpsDTO {

        return carRepository.findById(id)?.let {
            val checkUps = checkUpRepository.findByCar(it)
            CarWithCheckUpsDTO(it, checkUps)
        }
            ?: throw IllegalArgumentException("No director with such id: $id")
    }

    // fun deleteAllCars() = carRepository.clearCars()

    // fun deleteAllCarCheckUps() = carRepository.clearCheckUps()

}