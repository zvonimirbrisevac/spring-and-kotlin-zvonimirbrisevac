package com.infinum.academyproject.services

import com.infinum.academyproject.dto.*
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.repositories.CarRepository
import com.infinum.academyproject.repositories.CheckUpRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CarService(
    private val carRepository: CarRepository,
    private val checkUpRepository : CheckUpRepository
) {

    fun addCar(car: AddCarDTO): CarDTO = CarDTO(carRepository.save(car.toCar())) // ?: throw RuntimeException("Failed to add car.")

    fun addCarCheckUp(checkUp: AddCarCheckUpDTO) : CarCheckUpDTO = CarCheckUpDTO(
        checkUpRepository.save(
            checkUp.toCarCheckUp {
                carId -> carRepository.findById(carId) ?: throw RuntimeException("No car with such id.")
        })) // ?: throw RuntimeException("Failed to add car check up.")


    fun getCarCheckUps(id: Long): CarWithCheckUpsDTO {

        return carRepository.findById(id)?.let {
            val checkUps = checkUpRepository.findByCarOrderByTimeAndDateDesc(it)
            CarWithCheckUpsDTO(it, checkUps)
        }
            ?: throw IllegalArgumentException("No car with such id: $id")
    }

    fun getCarsPaged(pageable: Pageable) : Page<CarDTO> = carRepository.findAll(pageable).map { CarDTO(it) }

    fun getCheckUpsPaged(pageable: Pageable, id : Long) : Page<CarCheckUpNoCarDTO> {
        val car : Car = carRepository.findById(id) ?: throw RuntimeException("No car with such id.")
        return checkUpRepository.findAllByCarOrderByTimeAndDateDesc(pageable, car).map {CarCheckUpNoCarDTO(it)}
    }

    fun deleteAllCars() = carRepository.deleteAll()

    fun deleteAllCarCheckUps() = checkUpRepository.deleteAll()

}