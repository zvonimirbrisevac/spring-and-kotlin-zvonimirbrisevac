package com.infinum.academyproject

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.apache.catalina.manager.StatusTransformer
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
@AutoConfigureMockMvc
class AcademyProjectApplicationTests {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


    @Test
    fun testSimplePostCar() {
        val car = Car(
            ownerId = 111,
            manufacturer = "mazda",
            productionYear = 1999,
            serialNumber = "1111",
        )

        mvc.post("/add-car") {
            content = objectMapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.ownerId") { value(111) }
            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
            jsonPath("$.manufacturer") { value("mazda") }
            jsonPath("$.productionYear") { value(1999) }
            jsonPath("$.serialNumber") { value("1111") }
            jsonPath("$.checkUps") { value(mutableListOf<CarCheckUp>()) }
        }
    }

    @Test
    fun testSimplePostCarCheckUp() {
        mvc.post("/add-car") {
            content = objectMapper.writeValueAsString(
                Car(ownerId = 222, manufacturer = "dacia", productionYear = 2019, serialNumber = "2222")
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/add-car-checkup") {
            content = objectMapper.writeValueAsString(
                CarCheckUp(
                    timeAndDate = LocalDateTime.parse("2021-02-02 15:00", format),
                    workerName = "pero", price = 1000.00, carId = 1
                )
            )
            contentType = MediaType.APPLICATION_JSON

        }.andExpect {
            status { is2xxSuccessful() }
            //jsonPath("timeAndDate") { value(LocalDateTime.parse("2021-02-02 15:00", format))}
            jsonPath("timeAndDate") { value(format.format(LocalDateTime.parse("2021-02-02 15:00", format))) }
            jsonPath("workerName") { value("pero") }
            jsonPath("price") { value(1000.00) }
            jsonPath("carId") { value(1) }
        }
    }

    @Test
    fun testAddCarCheckUpFail() {

        mvc.post("/add-car-checkup") {
            content = objectMapper.writeValueAsString(
                CarCheckUp(
                    timeAndDate = LocalDateTime.parse("2021-12-12 15:00", format),
                    workerName = "juraj", price = 1500.00, carId = 10
                )
            )
            contentType = MediaType.APPLICATION_JSON

        }.andExpect {
            status { is4xxClientError() }
        }
    }

    @Test
    fun testGetCarCheckUps() {

        mvc.post("/add-car") {
            content = objectMapper.writeValueAsString(Car(
                ownerId = 111,
                manufacturer = "mazda",
                productionYear = 1999,
                serialNumber = "1111",
            ))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.ownerId") { value(111) }
            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
            jsonPath("$.manufacturer") { value("mazda") }
            jsonPath("$.productionYear") { value(1999) }
            jsonPath("$.serialNumber") { value("1111") }
            jsonPath("$.checkUps") { value(mutableListOf<CarCheckUp>()) }
        }

        mvc.post("/add-car") {
            content = objectMapper.writeValueAsString(Car(
                ownerId = 112,
                manufacturer = "nissan",
                productionYear = 2008,
                serialNumber = "1222",
            ))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.ownerId") { value(112) }
            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
            jsonPath("$.manufacturer") { value("nissan") }
            jsonPath("$.productionYear") { value(2008) }
            jsonPath("$.serialNumber") { value("1222") }
            jsonPath("$.checkUps") { value(mutableListOf<CarCheckUp>()) }
        }

        mvc.post("/add-car") {
            content = objectMapper.writeValueAsString(Car(
                ownerId = 113,
                manufacturer = "pezo",
                productionYear = 2009,
                serialNumber = "1223",
            ))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.ownerId") { value(113) }
            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
            jsonPath("$.manufacturer") { value("pezo") }
            jsonPath("$.productionYear") { value(2009) }
            jsonPath("$.serialNumber") { value("1223") }
            jsonPath("$.checkUps") { value(mutableListOf<CarCheckUp>()) }
        }

        mvc.post("/add-car-checkup") {
            content = objectMapper.writeValueAsString(
                CarCheckUp(
                    timeAndDate = LocalDateTime.parse("2020-08-20 09:30", format),
                    workerName = "pero", price = 1000.00, carId = 3
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            //jsonPath("timeAndDate") { value(LocalDateTime.parse("2021-02-02 15:00", format))}
            jsonPath("timeAndDate") { value(format.format(LocalDateTime.parse("2020-08-20 09:30", format))) }
            jsonPath("workerName") { value("pero") }
            jsonPath("price") { value(1000.00) }
            jsonPath("carId") { value(3) }
        }

        mvc.post("/add-car-checkup") {
            content = objectMapper.writeValueAsString(
                CarCheckUp(
                    timeAndDate = LocalDateTime.parse("2021-12-15 12:00", format),
                    workerName = "marko", price = 2000.00, carId = 3
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            //jsonPath("timeAndDate") { value(LocalDateTime.parse("2021-02-02 15:00", format))}
            jsonPath("timeAndDate") { value(format.format(LocalDateTime.parse("2021-12-15 12:00", format))) }
            jsonPath("workerName") { value("marko") }
            jsonPath("price") { value(2000.00) }
            jsonPath("carId") { value(3) }
        }

        mvc.post("/add-car-checkup") {
            content = objectMapper.writeValueAsString(
                CarCheckUp(
                    timeAndDate = LocalDateTime.parse("2021-12-15 11:00", format),
                    workerName = "ivica", price = 200.00, carId = 3
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            //jsonPath("timeAndDate") { value(LocalDateTime.parse("2021-02-02 15:00", format))}
            jsonPath("timeAndDate") { value(format.format(LocalDateTime.parse("2021-12-15 11:00", format))) }
            jsonPath("workerName") { value("ivica") }
            jsonPath("price") { value(200.00) }
            jsonPath("carId") { value(3) }
        }

        mvc.get("/get-car-checkups/3")
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.checkUps[0].timeAndDate") { value(format.format(LocalDateTime.parse("2021-12-15 12:00", format))) }
                jsonPath("$.checkUps[0].workerName") { value("marko") }
                jsonPath("$.checkUps[0].price") { value(2000.00) }
                jsonPath("$.checkUps[0].carId") { value(3) }
                jsonPath("$.checkUps[1].timeAndDate") { value(format.format(LocalDateTime.parse("2021-12-15 11:00", format))) }
                jsonPath("$.checkUps[1].workerName") { value("ivica") }
                jsonPath("$.checkUps[1].price") { value(200.00) }
                jsonPath("$.checkUps[1].carId") { value(3) }
                jsonPath("$.checkUps[2].timeAndDate") { value(format.format(LocalDateTime.parse("2020-08-20 09:30", format))) }
                jsonPath("$.checkUps[2].workerName") { value("pero") }
                jsonPath("$.checkUps[2].price") { value(1000.00) }
                jsonPath("$.checkUps[2].carId") { value(3) }
            }

    }

    @Test
    fun testGetCarCheckupsFail() {
        mvc.get("/get-car-checkups/100")
            .andExpect{
                status { is4xxClientError() }
            }
    }

}
