package com.infinum.academyproject

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academyproject.dto.*
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.services.CarService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@WebMvcTest
class ControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    /*@MockBean
    lateinit var service: CarService

    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val cars = listOf(
        Car(ownerId = 111, manufacturer = "opel", productionYear = 2015, serialNumber = "12345"),
        Car(ownerId = 222, manufacturer = "honda", productionYear = 2010, serialNumber = "10101"),
        Car(ownerId = 333, manufacturer = "kia", productionYear = 2000, serialNumber = "55555"),
        Car(ownerId = 444, manufacturer = "vw", productionYear = 1998, serialNumber = "44444"),
        Car(ownerId = 555, manufacturer = "citroen", productionYear = 2005, serialNumber = "98765")
    )

    val carsCheckUps = listOf(
        CarCheckUp(0, LocalDateTime.parse("2015-06-07 14:00", format), "mijo", 250.00, cars[0]),
        CarCheckUp(0, LocalDateTime.parse("2016-11-30 08:00", format), "pero", 1000.00, cars[0]),
        CarCheckUp(0, LocalDateTime.parse("2017-02-05 12:45", format), "ivo", 2000.00, cars[0]),
        CarCheckUp(0, LocalDateTime.parse("2007-03-03 13:00", format), "matej", 2150.00, cars[1]),
        CarCheckUp(0, LocalDateTime.parse("2010-03-15 12:15", format), "mijo", 500.00, cars[1]),
        CarCheckUp(0, LocalDateTime.parse("2015-08-09 13:00", format), "jura", 750.00, cars[2]),
        CarCheckUp(0, LocalDateTime.parse("2018-12-09 12:45", format), "drago", 3000.00, cars[2]),
        CarCheckUp(0, LocalDateTime.parse("2013-10-01 14:50", format), "blaz", 4230.00, cars[2]),
        CarCheckUp(0, LocalDateTime.parse("2014-04-25 09:35", format), "pejo", 210.00, cars[3])
    )

    @BeforeEach
    fun setUp() {

        for (i in 0..4) {
            Mockito.`when`(service.addCar(AddCarDTO(cars[i]))).thenReturn(CarDTO(cars[i]))
        }

        for (i in 0..8) {
            Mockito.`when`(service.addCarCheckUp(AddCarCheckUpDTO( carsCheckUps[i]))).thenReturn(CarCheckUpDTO(carsCheckUps[i]))
        }

        Mockito.`when`(service.getCarCheckUps(1))
            .thenReturn(CarWithCheckUpsDTO(cars[0], listOf(
                carsCheckUps[2], carsCheckUps[1],carsCheckUps[0]
            )))
        Mockito.`when`(service.getCarCheckUps(2))
            .thenReturn(CarWithCheckUpsDTO(cars[1], listOf(carsCheckUps[4], carsCheckUps[3])))
        Mockito.`when`(service.getCarCheckUps(3))
            .thenReturn(CarWithCheckUpsDTO(cars[2], listOf(carsCheckUps[6], carsCheckUps[5], carsCheckUps[7])))
        Mockito.`when`(service.getCarCheckUps(4))
            .thenReturn(CarWithCheckUpsDTO(cars[3], listOf(carsCheckUps[8])))
        Mockito.`when`(service.getCarCheckUps(5))
            .thenReturn(CarWithCheckUpsDTO(cars[4], listOf()))
        Mockito.`when`(service.getCarCheckUps(777)).thenThrow(RuntimeException::class.java)
        Mockito.`when`(service.addCarCheckUp(AddCarCheckUpDTO(
            CarCheckUp(0, LocalDateTime.parse("2020-02-02 01:33", format), "david", 20.00, cars[2]))))
            .thenThrow(RuntimeException::class.java)
    }

    @AfterEach
    fun clearSetUp() {
        service.deleteAllCars()
        service.deleteAllCarCheckUps()
    }

    @Test
    fun storeSimpleCar() {
        mvc.post("/cars/add") {
            content = objectMapper.writeValueAsString(Car(
                ownerId = 1234, manufacturer = "bmw", productionYear = 2012, serialNumber = "102030"
            ))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.ownerId") { value(1234) }
            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
            jsonPath("$.manufacturer") { value("bmw") }
            jsonPath("$.productionYear") { value(2012) }
            jsonPath("$.serialNumber") { value("102030") }
           // jsonPath("$.checkUps") { value(mutableListOf<CarCheckUp>()) }
        }
    }

    @Test
    fun storeSimpleCheckUp() {
        mvc.post("/car-checkups/add") {
            content = objectMapper.writeValueAsString(AddCarCheckUpDTO(
                LocalDateTime.parse("2012-05-21 12:00", format), "miro", 100.00, cars[0].id))
            contentType = MediaType.APPLICATION_JSON

        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("timeAndDate") { value(format.format(LocalDateTime.parse("2012-05-21 12:00", format))) }
            jsonPath("workerName") { value("miro") }
            jsonPath("price") { value(100.00) }
            //jsonPath("carId") { value(1) }
        }
    }

    @Test
    fun storeCheckUpFail() {
        mvc.post("/car-checkups/add") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(LocalDateTime.parse("2020-02-02 01:33", format), "david", 20.00, cars[2].id)
            )
            contentType = MediaType.APPLICATION_JSON

        }.andExpect {
            status { is4xxClientError() }
        }
    }

    @Test
    fun getCarCheckUps1() {
        mvc.get("/cars/3/checkups")
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$[0].timeAndDate") { value(format.format(LocalDateTime.parse("2018-12-09 12:45", format))) }
                jsonPath("$[0].workerName") { value("drago") }
                jsonPath("$[0].price") { value(3000.00) }
                //jsonPath("$[0].carId") { value(3) }

                jsonPath("$[1].timeAndDate") { value(format.format(LocalDateTime.parse("2015-08-09 13:00", format))) }
                jsonPath("$[1].workerName") { value("jura") }
                jsonPath("$[1].price") { value(750.00) }
               // jsonPath("$[1].carId") { value(3) }

                jsonPath("$[2].timeAndDate") { value(format.format(LocalDateTime.parse("2013-10-01 14:50", format))) }
                jsonPath("$[2].workerName") { value("blaz") }
                jsonPath("$[2].price") { value(4230.00) }
                //jsonPath("$[2].carId") { value(3) }
            }
    }

    @Test
    fun getCarCheckUpsFail() {
        mvc.get("/cars/777/checkups")
            .andExpect {
                status { is4xxClientError() }
            }
    }

    @Test
    fun getCarCheckUps2() {
        mvc.get("/cars/4/checkups")
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$[0].timeAndDate") { value(format.format(LocalDateTime.parse("2014-04-25 09:35", format))) }
                jsonPath("$[0].workerName") { value("pejo") }
                jsonPath("$[0].price") { value(210.00) }
                //jsonPath("$[0].carId") { value(4) }
            }
    }

    @Test
    fun getCarCheckUps3() {
        mvc.get("/cars/5/checkups")
            .andExpect{
                status { is2xxSuccessful() }
                //content { json("[]") }
            }
    }*/


}