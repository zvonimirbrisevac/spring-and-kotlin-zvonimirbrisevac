package com.infinum.academyproject

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.repositories.CarRepository
import com.infinum.academyproject.repositories.CheckUpRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaTest @Autowired constructor(
    val carRepository: CarRepository,
    val checkUpRepository: CheckUpRepository
) {

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
        CarCheckUp(0, LocalDateTime.parse("2015-06-07 14:00", dateTimeFormatter),
            "mijo", 250.00, cars[0]),
        CarCheckUp(0, LocalDateTime.parse("2016-11-30 08:00", dateTimeFormatter),
            "pero", 1000.00, cars[0]),
        CarCheckUp(0, LocalDateTime.parse("2017-02-05 12:45", dateTimeFormatter),
            "ivo", 2000.00, cars[0]),
        CarCheckUp(0, LocalDateTime.parse("2007-03-03 13:00", dateTimeFormatter),
            "matej", 2150.00, cars[1]),
        CarCheckUp(0, LocalDateTime.parse("2010-03-15 12:15", dateTimeFormatter),
            "mijo", 500.00, cars[1]),
        CarCheckUp(0, LocalDateTime.parse("2015-08-09 13:00", dateTimeFormatter),
            "jura", 750.00, cars[2]),
        CarCheckUp(0, LocalDateTime.parse("2018-12-09 12:45", dateTimeFormatter),
            "drago", 3000.00, cars[2]),
        CarCheckUp(0, LocalDateTime.parse("2013-10-01 14:50", dateTimeFormatter),
            "blaz", 4230.00, cars[2]),
        CarCheckUp(0, LocalDateTime.parse("2014-04-25 09:35", dateTimeFormatter),
            "pejo", 210.00, cars[3])
    )

    @BeforeEach
    fun setUp() {

        carRepository.deleteAll()
        checkUpRepository.deleteAll()

        carRepository.saveAll(cars)
        checkUpRepository.saveAll(carsCheckUps)

    }

    @Test
    fun savesAll() {
        Assertions.assertThat(checkUpRepository.findAll().size).isEqualTo(9)
    }

    @Test
    fun findByWorkerNameAndPrice() {
        Assertions.assertThat(checkUpRepository.findByWorkerNameAndPrice("blaz", 4230.00))
            .isEqualTo(carsCheckUps[7])
    }

    @Test
    fun findAllCheckUpsDesc() {
        val car = carRepository.findByOwnerIdAndSerialNumber(111, "12345")
        Assertions.assertThat(checkUpRepository.findByCarOrderByTimeAndDateDesc(car))
            .isEqualTo(listOf(carsCheckUps[2], carsCheckUps[1], carsCheckUps[0]))

    }

    @Test
    fun saveCar() {
        val car = Car(ownerId = 222, manufacturer = "renault", productionYear = 2014, serialNumber = "12121" )
        Assertions.assertThat(carRepository.save(car)).isEqualTo(car)
    }

    @Test
    fun carPage() {
        val page = PageRequest.of(0, 3)
        val carPage = carRepository.findAll(page)
        Assertions.assertThat(carPage.content[0].manufacturer).isEqualTo("opel")
        Assertions.assertThat(carPage.content[1].manufacturer).isEqualTo("honda")
        Assertions.assertThat(carPage.content[2].manufacturer).isEqualTo("kia")

    }

    @Test
    fun checkUpPage() {
        val page = PageRequest.of(0, 2, Sort.by(CarCheckUp::price.name))
        val checkUpPage = checkUpRepository.findAllByCarOrderByTimeAndDateDesc(page, cars[0])
        Assertions.assertThat(checkUpPage.content.size).isEqualTo(2)
        Assertions.assertThat(checkUpPage.content[0].price).isEqualTo(2000.00)
        Assertions.assertThat(checkUpPage.content[1].price).isEqualTo(1000.0)
    }




}