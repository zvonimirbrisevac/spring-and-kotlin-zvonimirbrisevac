package com.infinum.academyproject.services

import com.infinum.academyproject.dto.AddCarCheckUpDTO
import com.infinum.academyproject.dto.AddCarDTO
import com.infinum.academyproject.dto.CarCheckUpDTO
import com.infinum.academyproject.dto.CarDTO
import com.infinum.academyproject.errors.IllegalCarModelException
import com.infinum.academyproject.errors.NoCarIdException
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUpDurationFromNow
import com.infinum.academyproject.repositories.CarRepository
import com.infinum.academyproject.repositories.CheckUpRepository
import com.infinum.academyproject.repositories.ModelRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

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


    fun getCarCheckUps(id: Long): CarDTO = CarDTO(carRepository.findById(id) ?: throw NoCarIdException("No car with that id."))


    fun getCarsPaged(pageable: Pageable) : Page<CarDTO> = carRepository.findAll(pageable).map { CarDTO(it) }

    fun getCheckUpsPaged(pageable: Pageable, id : Long) : Page<CarCheckUpDTO> {
        val car : Car = carRepository.findById(id) ?: throw NoCarIdException("No car with such id.")
        return checkUpRepository.findAllByCarOrderByTimeAndDateDesc(pageable, car).map {CarCheckUpDTO(it)}
    }

    fun getLastTenCheckUps(pageable: Pageable): Page<CarCheckUpDTO> = checkUpRepository.findLastTenCheckUps(pageable)
        .map{ CarCheckUpDTO(it) }

    fun getUpcomingCheckUps(durationFromNow: CarCheckUpDurationFromNow, pageable: Pageable) : Page<CarCheckUpDTO>{
        val durationString : String = when(durationFromNow) {
            CarCheckUpDurationFromNow.ONE_WEEK -> "interval '1 week'"
            CarCheckUpDurationFromNow.ONE_MONTH -> "interval '1 month'"
            CarCheckUpDurationFromNow.HALF_YEAR -> "interval '6 months'"
        }
        return checkUpRepository.findUpcomingCheckUps(durationString, pageable).map { CarCheckUpDTO(it) }
    }

    fun deleteAllCars() = carRepository.deleteAll()

    fun deleteAllCarCheckUps() = checkUpRepository.deleteAll()


}