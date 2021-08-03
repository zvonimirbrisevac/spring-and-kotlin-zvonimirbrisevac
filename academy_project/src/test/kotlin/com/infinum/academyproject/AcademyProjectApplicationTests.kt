package com.infinum.academyproject

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academyproject.dto.AddCarCheckUpDTO
import com.infinum.academyproject.dto.AddCarDTO
import com.infinum.academyproject.dto.AddCarModelDTO
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.models.CarModel
import com.infinum.academyproject.repositories.ModelRepository
import com.infinum.academyproject.services.CarService
import com.infinum.academyproject.services.HttpCarModelService
import com.infinum.academyproject.services.SchedulingService
import org.apache.catalina.manager.StatusTransformer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.MediaType
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
@AutoConfigureMockMvc
@MockServerTest
@MockBean(SchedulingService::class)
class AcademyProjectApplicationTests @Autowired constructor(
    private val httpService: HttpCarModelService,
    private val carService: CarService
){

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    lateinit var mockServerClient: MockServerClient

    @MockBean
    lateinit var schedulingService: SchedulingService


    @BeforeEach
    fun setUp() {
        mockServerClient
            .`when`(
                HttpRequest.request()
                    .withPath("/api/v1/cars")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                    .withBody(
                        """
                        {
                            "data": [
                                {"manufacturer":"Alfa Romeo","model_name":"145","is_common":0},
                                {"manufacturer":"Audi","model_name":"A6","is_common":0},
                                {"manufacturer":"BMW","model_name":"530","is_common":0},
                                {"manufacturer":"Fiat","model_name":"Punto","is_common":0},
                                {"manufacturer":"Ford","model_name":"Fiesta","is_common":0},
                                {"manufacturer":"Mazda","model_name":"323","is_common":0},
                                {"manufacturer":"Nissan","model_name":"Juke","is_common":0},
                                {"manufacturer":"Dacia","model_name":"Duster","is_common":0}
                            ]
                        }
                    """.trimIndent()
                    )
            )



        val modelsClient : List<AddCarModelDTO> = httpService.getCarModels() ?: throw Exception("bzvz")

        for (model in modelsClient) {
            try {
                carService.saveModel(model)
            } catch(ex : DataIntegrityViolationException) {

            }
        }


    }

    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val models = listOf(
        CarModel(0, "Alfa Romeo", "145", true),
        CarModel(0, "Audi", "A6", true),
        CarModel(0, "BMW", "530", true),
        CarModel(0, "Fiat", "Punto", true),
        CarModel(0, "Ford", "Fiesta", true),
        CarModel(0, "fake", "fake", false)
    )

    val cars = listOf(
        Car(ownerId = 111, model = models[0], productionYear = 2015, serialNumber = "12345"),
        Car(ownerId = 222, model = models[1], productionYear = 2010, serialNumber = "10101"),
        Car(ownerId = 333, model = models[2], productionYear = 2000, serialNumber = "55555"),
        Car(ownerId = 444, model = models[3], productionYear = 1998, serialNumber = "44444"),
        Car(ownerId = 555, model = models[4], productionYear = 2005, serialNumber = "98765")
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

    @Test
    fun testSimplePostCar() {
        val car = AddCarDTO(
            ownerId = 111,
            manufacturer = "Mazda",
            model = "323",
            productionYear = 1999,
            serialNumber = "1111"

        )

        mvc.post("/cars/add") {
            content = objectMapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.ownerId") { value(111) }
            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
            jsonPath("$.manufacturer") { value("Mazda") }
            jsonPath("$.model") { value("323") }
            jsonPath("$.productionYear") { value(1999) }
            jsonPath("$.serialNumber") { value("1111") }
        }
    }

    @Test
    fun testSimplePostCarCheckUp() {
        mvc.post("/cars/add") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 222,
                    manufacturer = "Dacia",
                    model = "Duster",
                    productionYear = 2019,
                    serialNumber = "2222"
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups/add") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
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
            jsonPath("car.id") { value(1) }
        }
    }

    @Test
    fun testAddCarCheckUpFail() {

        mvc.post("/car-checkups/add") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
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

        mvc.post("/cars/add") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 111,
                    manufacturer = "Mazda",
                    model = "323",
                    productionYear = 1999,
                    serialNumber = "1111",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.ownerId") { value(111) }
            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
            jsonPath("$.manufacturer") { value("Mazda") }
            jsonPath("$.model") { value("323") }
            jsonPath("$.productionYear") { value(1999) }
            jsonPath("$.serialNumber") { value("1111") }
        }

        mvc.post("/cars/add") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 112,
                    manufacturer = "Nissan",
                    model = "Juke",
                    productionYear = 2008,
                    serialNumber = "1222",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.ownerId") { value(112) }
            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
            jsonPath("$.manufacturer") { value("Nissan") }
            jsonPath("$.productionYear") { value(2008) }
            jsonPath("$.serialNumber") { value("1222") }
        }

        mvc.post("/cars/add") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 113,
                    manufacturer = "Fiat",
                    model = "Punto",
                    productionYear = 2009,
                    serialNumber = "1223",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.ownerId") { value(113) }
            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
            jsonPath("$.manufacturer") { value("Fiat") }
            jsonPath("$.model") {value("Punto")}
            jsonPath("$.productionYear") { value(2009) }
            jsonPath("$.serialNumber") { value("1223") }
        }

        mvc.post("/car-checkups/add") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
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
            jsonPath("car.id") { value(3) }
        }

        mvc.post("/car-checkups/add") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
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
            jsonPath("car.id") { value(3) }
        }

        mvc.post("/car-checkups/add") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
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
            jsonPath("car.id") { value(3) }
        }

        mvc.get("/cars/3/checkups")
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.checkUpsDTO[0].timeAndDate") {
                    value(
                        format.format(
                            LocalDateTime.parse(
                                "2021-12-15 12:00",
                                format
                            )
                        )
                    )
                }
                jsonPath("$.checkUpsDTO[0].workerName") { value("marko") }
                jsonPath("$.checkUpsDTO[0].price") { value(2000.00) }

                jsonPath("$.checkUpsDTO[1].timeAndDate") {
                    value(
                        format.format(
                            LocalDateTime.parse(
                                "2021-12-15 11:00",
                                format
                            )
                        )
                    )
                }
                jsonPath("$.checkUpsDTO[1].workerName") { value("ivica") }
                jsonPath("$.checkUpsDTO[1].price") { value(200.00) }

                jsonPath("$.checkUpsDTO[2].timeAndDate") {
                    value(
                        format.format(
                            LocalDateTime.parse(
                                "2020-08-20 09:30",
                                format
                            )
                        )
                    )
                }
                jsonPath("$.checkUpsDTO[2].workerName") { value("pero") }
                jsonPath("$.checkUpsDTO[2].price") { value(1000.00) }
            }

    }

    @Test
    fun testGetCarCheckupsFail() {
        mvc.get("/cars/100/checkups")
            .andExpect {
                status { is4xxClientError() }
            }
    }

    @Test
    fun testPostCarInvalidModel() {
        val car = AddCarDTO(
            ownerId = 111,
            manufacturer = "bzvz",
            model = "bzvz",
            productionYear = 1999,
            serialNumber = "1111"

        )

        mvc.post("/cars/add") {
            content = objectMapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is4xxClientError() }
        }
    }

}
