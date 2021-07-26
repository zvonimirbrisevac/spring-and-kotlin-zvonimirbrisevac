package com.infinum.academyproject

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.annotation.Commit
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class JdbcTest {

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val dateFormatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val cars = listOf(
        Car(ownerId = 111, manufacturer = "opel", productionYear = 2015, serialNumber = "12345"),
        Car(ownerId = 222, manufacturer = "honda", productionYear = 2010, serialNumber = "10101"),
        Car(ownerId = 333, manufacturer = "kia", productionYear = 2000, serialNumber = "55555"),
        Car(ownerId = 444, manufacturer = "vw", productionYear = 1998, serialNumber = "44444"),
        Car(ownerId = 555, manufacturer = "citroen", productionYear = 2005, serialNumber = "98765")
    )

    val carsCheckUps = listOf(
        CarCheckUp(LocalDateTime.parse("2015-06-07 14:00", dateTimeFormatter), "mijo", 250.00, 1),
        CarCheckUp(LocalDateTime.parse("2016-11-30 08:00", dateTimeFormatter), "pero", 1000.00, 1),
        CarCheckUp(LocalDateTime.parse("2017-02-05 12:45", dateTimeFormatter), "ivo", 2000.00, 1),
        CarCheckUp(LocalDateTime.parse("2007-03-03 13:00", dateTimeFormatter), "matej", 2150.00, 2),
        CarCheckUp(LocalDateTime.parse("2010-03-15 12:15", dateTimeFormatter), "mijo", 500.00, 2),
        CarCheckUp(LocalDateTime.parse("2015-08-09 13:00", dateTimeFormatter), "jura", 750.00, 3),
        CarCheckUp(LocalDateTime.parse("2018-12-09 12:45", dateTimeFormatter), "drago", 3000.00, 3),
        CarCheckUp(LocalDateTime.parse("2013-10-01 14:50", dateTimeFormatter), "blaz", 4230.00, 3),
        CarCheckUp(LocalDateTime.parse("2014-04-25 09:35", dateTimeFormatter), "pejo", 210.00, 4)
    )

    @BeforeEach
    fun setUp() {
        jdbcTemplate.update("ALTER SEQUENCE cars_id_seq RESTART WITH 1", mapOf("" to ""))
        jdbcTemplate.update("ALTER SEQUENCE checkups_id_seq RESTART WITH 1", mapOf("" to ""))

        for (i in 0..4) {
            jdbcTemplate.update(
                "INSERT INTO cars (addeddate, manufacturer, productionyear, serialnumber) " +
                        "VALUES (:date, :manuf, :prodyear, :sernum)",
                mapOf(
                    "date" to cars[i].addedDate,
                    "manuf" to cars[i].manufacturer,
                    "prodyear" to cars[i].productionYear,
                    "sernum" to cars[i].serialNumber
                )

            )
        }

        for (i in 0..8) {
            jdbcTemplate.update(
                "INSERT INTO checkups (timeanddate, workername, price, carid) " +
                        "VALUES (:time, :worker, :price, :carid)",
                mapOf(
                    "time" to carsCheckUps[i].timeAndDate,
                    "worker" to carsCheckUps[i].workerName,
                    "price" to carsCheckUps[i].price,
                    "carid" to carsCheckUps[i].carId
                )
            )
        }

        val countCars =  jdbcTemplate.queryForObject(
            "SELECT count(*) FROM cars",
            mapOf("" to ""),
            Int::class.java
        )

        val countCheckUps = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM checkups",
            mapOf("" to ""),
            Int::class.java
        )

        println("cars: $countCars , checkup: $countCheckUps  ")

    }

    @AfterEach
    fun breakDown() {
        jdbcTemplate.update("DELETE FROM cars", mapOf("" to ""))
        jdbcTemplate.update("DELETE FROM checkups", mapOf("" to ""))
    }

    @Test
    fun checkTables() {
        jdbcTemplate.queryForList(
            "SELECT id FROM cars LIMIT 1",
            mapOf("" to ""),
            Any::class.java
        )
    }

    @Test
    fun getCarId() {
        Assertions.assertThat(
            jdbcTemplate.queryForObject(
                "SELECT id FROM cars WHERE manufacturer = :manuf",
                mapOf("manuf" to "kia"),
                Long::class.java
            )
        ).isEqualTo(3)
    }

    @Test
    fun getCheckUpId() {
        Assertions.assertThat(
            jdbcTemplate.queryForObject(
                "SELECT id FROM checkups WHERE workername = :name",
                mapOf("name" to "matej"),
                Long::class.java
            )
        ).isEqualTo(4)
    }

    @Test
    fun getSerialNumberJoin() {
        Assertions.assertThat(
            jdbcTemplate.queryForObject(
                "SELECT cars.serialnumber FROM cars FULL JOIN checkups ON cars.id = checkups.carid " +
                        "WHERE checkups.price = :price",
                mapOf("price" to 250.00),
                String::class.java
            )
        ).isEqualTo("12345")
    }

    @Test
    fun getPriceDesc() {
        val results: MutableList<MutableMap<String, Any>> =
            jdbcTemplate.queryForList(
                "SELECT price FROM checkups WHERE price >= 1000 ORDER BY price DESC",
                mapOf("" to "")
            )
        var resultsList: MutableList<Double> = mutableListOf()

        for (map in results) {
            resultsList.add(map["price"].toString().toDouble())
        }

        Assertions.assertThat(resultsList).isEqualTo(mutableListOf<Double>(4230.00, 3000.00, 2150.00, 2000.00, 1000.00 ))
    }

    @Test
    fun getCheckUpByPriceAndName() {
        Assertions.assertThat(
            jdbcTemplate.queryForObject(
                "SELECT timeanddate FROM checkups WHERE workername = :name AND price = 500.00",
                mapOf("name" to "mijo"),
                LocalDateTime::class.java
            )
        ).isEqualTo(LocalDateTime.parse("2010-03-15 12:15", dateTimeFormatter))

    }

    @Test
    fun getManufacturerAscByProdYear() {
        val results: MutableList<MutableMap<String, Any>> =
            jdbcTemplate.queryForList(
                "SELECT manufacturer FROM cars WHERE cars.productionyear >= 2005 ORDER BY productionyear ASC",
                mapOf("" to "")
            )
        var resultsList: MutableList<String> = mutableListOf()

        for (map in results) {
            resultsList.add(map["manufacturer"].toString())
        }

        Assertions.assertThat(resultsList).isEqualTo(mutableListOf<String>("citroen", "honda", "opel"))
    }

    @Test
    fun getEmptyListCheckUps() {
        val count = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM cars INNER JOIN checkups ON cars.id = checkups.carid WHERE cars.id = 5",
            mapOf("" to ""),
            Int::class.java
        )

        Assertions.assertThat(count).isEqualTo(0)
    }


}