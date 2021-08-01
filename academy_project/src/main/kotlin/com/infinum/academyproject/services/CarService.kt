package com.infinum.academyproject.services

import com.infinum.academyproject.dto.*
import com.infinum.academyproject.errors.IllegalCarModelException
import com.infinum.academyproject.errors.NoCarIdException
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.repositories.CarRepository
import com.infinum.academyproject.repositories.CheckUpRepository
import com.infinum.academyproject.repositories.ModelRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.awt.color.ICC_Profile

@Service
class CarService(
    private val carRepository: CarRepository,
    private val checkUpRepository : CheckUpRepository,
    private val modelRepository: ModelRepository
) {

    fun addCar(car: AddCarDTO): CarDTO {
        return CarDTO(carRepository.save(car.toCar {
                manufacturer, model -> modelRepository.findByManufacturerAndModelName(manufacturer, model)
            ?: throw IllegalCarModelException("Model not found.")
        }))
    }

    fun addCarCheckUp(checkUp: AddCarCheckUpDTO) : CarCheckUpDTO = CarCheckUpDTO(
        checkUpRepository.save(
            checkUp.toCarCheckUp {
                carId -> carRepository.findById(carId) ?: throw NoCarIdException("No car with such id.")
        }))


    fun getCarCheckUps(id: Long): CarWithCheckUpsDTO {

        return carRepository.findById(id)?.let {
            val checkUps = checkUpRepository.findByCarOrderByTimeAndDateDesc(it)
            CarWithCheckUpsDTO(it, checkUps)
        }
            ?: throw NoCarIdException("No car with that id.")
    }

    fun getCarsPaged(pageable: Pageable) : Page<CarDTO> = carRepository.findAll(pageable).map { CarDTO(it) }

    fun getCheckUpsPaged(pageable: Pageable, id : Long) : Page<CarCheckUpNoCarDTO> {
        val car : Car = carRepository.findById(id) ?: throw NoCarIdException("No car with such id.")
        return checkUpRepository.findAllByCarOrderByTimeAndDateDesc(pageable, car).map {CarCheckUpNoCarDTO(it)}
    }

    fun deleteAllCars() = carRepository.deleteAll()

    fun deleteAllCarCheckUps() = checkUpRepository.deleteAll()

    fun saveModel(model: AddCarModelDTO) = modelRepository.save(model.toCarModel())
}