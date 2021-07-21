package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class CarRepository (
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun addCar(car: Car): Long? {
        jdbcTemplate.update(
            "INSERT INTO cars (addeddate, manufacturer, productionyear, serialnumber)" +
                    "VALUES (:date, :manuf, :prodyear, :sernum)",
            mapOf("date" to car.addedDate.toString(),
                "manuf" to car.manufacturer,
                "prodyear" to car.productionYear,
                "sernum" to car.serialNumber)
        )

        return jdbcTemplate.queryForObject(
            "SELECT id FROM cars WHERE addeddate = :date AND" +
                    "manufacturer = :manuf AND" +
                    "productionyear = :prodyear AND" +
                    "serialnumber = :sernum LIMIT 1",
            mapOf("date" to car.addedDate.toString(),
                "manuf" to car.manufacturer,
                "prodyear" to car.productionYear,
                "sernum" to car.serialNumber
            ), Long::class.java
        )
    }

    fun addCheckUp(checkUp: CarCheckUp): Long? {
        jdbcTemplate.update(
            "INSERT INTO checkups (timeanddate, workername, price, carid)" +
                    "VALUES (:time, :worker, :price, :carid)",
            mapOf(
                "time" to checkUp.timeAndDate.toString(),
                "worker" to checkUp.workerName,
                "price" to checkUp.price,
                "carid" to checkUp.carId
            )
        )

        return jdbcTemplate.queryForObject(
            "SELECT id FROM checkups WHERE timeanddate = :time AND" +
                    "workername = :worker AND" +
                    "price = :price AND" +
                    "carid = :carid LIMIT 1",
            mapOf(
                "time" to checkUp.timeAndDate.toString(),
                "worker" to checkUp.workerName,
                "price" to checkUp.price,
                "carid" to checkUp.carId
            ), Long::class.java
        )
    }


    fun getCarCheckUps(carId: Long) : List<CarCheckUp>? {
        val results: MutableList<MutableMap<String, Any>> =
            jdbcTemplate.queryForList(
                "SELECT * FROM checkups WHERE carid = :id",
                mapOf("id" to carId)
            )
        var returnList: MutableList<CarCheckUp> = mutableListOf()
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        for (map in results) {
            returnList.add(CarCheckUp(
                LocalDateTime.parse(map["timeanddate"].toString(), format),
                map["workername"].toString(),
                map["price"].toString().toDouble(),
                map["carid"].toString().toLong()
            ))
        }

        return returnList.sortedWith(compareBy { it.timeAndDate }).reversed()
    }

    /*fun clearCars() = cars.clear()

    fun clearCheckUps() = carCheckUps.clear()*/

    fun clearCars() {
        jdbcTemplate.update("TRUNCATE cars", mapOf("" to ""))

    }

    fun clearCheckUps() {
        jdbcTemplate.update("TRUNCATE checkups", mapOf("" to ""))
    }
}