package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.springframework.format.datetime.DateFormatter
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
    val dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val dateFormatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun addCar(car: Car): Long? {
        jdbcTemplate.update(
            "INSERT INTO cars (ownerid, addeddate, manufacturer, productionyear, serialnumber)" +
                    "VALUES (:ownerid, :date, :manuf, :prodyear, :sernum)",
            mapOf("ownerid" to car.ownerId,
                "date" to car.addedDate,
                "manuf" to car.manufacturer,
                "prodyear" to car.productionYear,
                "sernum" to car.serialNumber)
        )

        return jdbcTemplate.queryForObject(
            "SELECT id FROM cars WHERE addeddate = :date AND " +
                    "manufacturer = :manuf AND " +
                    "productionyear = :prodyear AND " +
                    "serialnumber = :sernum LIMIT 1",
            mapOf("date" to car.addedDate,
                "manuf" to car.manufacturer,
                "prodyear" to car.productionYear,
                "sernum" to car.serialNumber
            ), Long::class.java
        )
    }

    fun addCheckUp(checkUp: CarCheckUp): Long? {
        val count = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM cars WHERE id = :id",
            mapOf("id" to checkUp.carId),
            Int::class.java
        )

        if (count == 0) throw RuntimeException("No car with that id")

        jdbcTemplate.update(
            "INSERT INTO checkups (timeanddate, workername, price, carid)" +
                    "VALUES (:time, :worker, :price, :carid)",
            mapOf(
                "time" to checkUp.timeAndDate,
                "worker" to checkUp.workerName,
                "price" to checkUp.price,
                "carid" to checkUp.carId
            )
        )

        return jdbcTemplate.queryForObject(
            "SELECT id FROM checkups WHERE timeanddate = :time AND " +
                    "workername = :worker AND " +
                    "price = :price AND " +
                    "carid = :carid LIMIT 1",
            mapOf(
                "time" to checkUp.timeAndDate,
                "worker" to checkUp.workerName,
                "price" to checkUp.price,
                "carid" to checkUp.carId
            ), Long::class.java
        )
    }


    fun getCarCheckUps(carId: Long) : List<CarCheckUp>? {
        val count = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM cars WHERE id = :id",
            mapOf("id" to carId),
            Int::class.java
        )

        if (count == 0) throw RuntimeException("No car with that id")

        val results: MutableList<MutableMap<String, Any>> =
            jdbcTemplate.queryForList(
                "SELECT * FROM checkups WHERE carid = :id ORDER BY timeanddate DESC",
                mapOf("id" to carId)
            )
        var returnList: MutableList<CarCheckUp> = mutableListOf()

        for (map in results) {
            returnList.add(CarCheckUp(
                LocalDateTime.parse(map["timeanddate"].toString().subSequence(0, map["timeanddate"].toString()
                    .lastIndexOf(":")), dateTimeformatter),
                map["workername"].toString(),
                map["price"].toString().toDouble(),
                map["carid"].toString().toLong()
            ))
        }

        return returnList
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