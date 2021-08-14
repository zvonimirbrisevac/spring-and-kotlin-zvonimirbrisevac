package com.infinum.academyproject

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academyproject.config.SecurityConfig
import com.infinum.academyproject.dto.AddCarCheckUpDTO
import com.infinum.academyproject.dto.AddCarDTO
import com.infinum.academyproject.dto.AddCarModelDTO
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import com.infinum.academyproject.models.CarModel
import com.infinum.academyproject.services.CarService
import com.infinum.academyproject.services.HttpCarModelService
import com.infinum.academyproject.services.SchedulingService
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
@AutoConfigureMockMvc
@MockServerTest
@MockBean(SchedulingService::class)
class AcademyProjectApplicationTests @Autowired constructor(
    private val httpService: HttpCarModelService,
    private val carService: CarService,
//    private val securityConfig: SecurityConfig
){

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    lateinit var mockServerClient: MockServerClient

//    @MockBean
//    lateinit var schedulingService: SchedulingService


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
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun testSimplePostCarUser() {
        val car = AddCarDTO(
            ownerId = 111,
            manufacturer = "Mazda",
            model = "323",
            productionYear = 1999,
            serialNumber = "1111"

        )

        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun testSimplePostCar() {
        val car = AddCarDTO(
            ownerId = 111,
            manufacturer = "Mazda",
            model = "323",
            productionYear = 1999,
            serialNumber = "1111"

        )

        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    @WithAnonymousUser
    fun testSimplePostCarAnonymous() {
        val car = AddCarDTO(
            ownerId = 111,
            manufacturer = "Mazda",
            model = "323",
            productionYear = 1999,
            serialNumber = "1111"

        )

        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun testSimplePostCarCheckUp() {
        mvc.post("/cars") {
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

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2023-12-02 15:00", format),
                    workerName = "pero", price = 1000.00, carId = 1
                )
            )
            contentType = MediaType.APPLICATION_JSON

        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun testSimplePostCarCheckUpUser() {
        mvc.post("/cars") {
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

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2023-12-02 15:00", format),
                    workerName = "pero", price = 1000.00, carId = 1
                )
            )
            contentType = MediaType.APPLICATION_JSON

        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun testAddCarCheckUpFail() {

        mvc.post("/car-checkups") {
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
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun testGetCarCheckUps() {

        mvc.post("/cars") {
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
        }

       mvc.post("/cars") {
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
        }

        mvc.post("/cars") {
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

        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2022-04-20 09:30", format),
                    workerName = "pero", price = 1000.00, carId = 3
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2022-12-15 12:00", format),
                    workerName = "marko", price = 2000.00, carId = 3
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2022-12-15 11:00", format),
                    workerName = "ivica", price = 200.00, carId = 3
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }

        mvc.get("/cars/3/checkups")
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$._embedded.item[0].workerName") { value("marko") }
                jsonPath("$._embedded.item[0].price") { value(2000.00) }

                jsonPath("$._embedded.item[1].workerName") { value("ivica") }
                jsonPath("$._embedded.item[1].price") { value(200.00) }

                jsonPath("$._embedded.item[2].workerName") { value("pero") }
                jsonPath("$._embedded.item[2].price") { value(1000.00) }
        }

    }


    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun testGetCarCheckupsFail() {
        mvc.get("/cars/100/checkups")
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun testPostCarInvalidModel() {
        val car = AddCarDTO(
            ownerId = 111,
            manufacturer = "bzvz",
            model = "bzvz",
            productionYear = 1999,
            serialNumber = "1111"

        )

        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun getLastTenCheckups() {
        val carId1 = mvc.post("/cars") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 122,
                    manufacturer = "Dacia",
                    model = "Duster",
                    productionYear = 1999,
                    serialNumber = "1222",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn().response.getHeaderValue("location")
            .toString().split("/").last().toLong()

        val carId2 = mvc.post("/cars") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 123,
                    manufacturer = "Audi",
                    model = "A6",
                    productionYear = 2000,
                    serialNumber = "1233",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn().response.getHeaderValue("location")
            .toString().split("/").last().toLong()


        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2021-02-16 11:00", format),
                    workerName = "ivica", price = 200.00, carId = carId1
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2015-10-09 11:00", format),
                    workerName = "perica", price = 200.00, carId = carId2
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }


        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2015-10-09 10:00", format),
                    workerName = "marko", price = 200.00, carId = carId1
                )
         )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2022-02-15 11:00", format).plusYears(1),
                    workerName = "josip", price = 200.00, carId = carId2
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2017-03-15 11:00", format),
                    workerName = "ante", price = 200.00, carId = carId2
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse("2020-12-15 11:00", format),
                    workerName = "matej", price = 200.00, carId = carId1)
            )
            contentType = MediaType.APPLICATION_JSON
        }


        mvc.get("/car-checkups")
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$._embedded.item.length()") {value(5)}

                jsonPath("$._embedded.item[0].workerName") { value("ivica") }
                jsonPath("$._embedded.item[0]._links.car.href") {
                    value("http://localhost/cars/$carId1") }

                jsonPath("$._embedded.item[1].workerName") { value("matej") }

                jsonPath("$._embedded.item[2].workerName") { value("ante") }
                jsonPath("$._embedded.item[2]._links.car.href") {
                    value("http://localhost/cars/$carId2") }

                jsonPath("$._embedded.item[3].workerName") { value("perica") }

                jsonPath("$._embedded.item[4].workerName") { value("marko") }

            }

    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun getUpcomingCheckUpsWeekAndHalfYear() {

        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 122,
                    manufacturer = "Dacia",
                    model = "Duster",
                    productionYear = 2020,
                    serialNumber = "1221",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }

        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 124,
                    manufacturer = "Audi",
                    model = "A6",
                    productionYear = 2010,
                    serialNumber = "1234",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }

        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 132,
                    manufacturer = "Fiat",
                    model = "Punto",
                    productionYear = 1995,
                    serialNumber = "1322",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }

        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 123,
                    manufacturer = "Alfa Romeo",
                    model = "145",
                    productionYear = 2000,
                    serialNumber = "1233",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse(format.format(LocalDateTime.now().plusWeeks(3)), format),
                    workerName = "ivica", price = 200.00, carId = 4
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse(format.format(LocalDateTime.now().plusMonths(3)), format),
                    workerName = "perica", price = 200.00, carId = 4
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }


        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse(format.format(LocalDateTime.now().plusMonths(8)), format),
                    workerName = "marko", price = 200.00, carId = 3
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse(format.format(LocalDateTime.now().plusDays(2)), format),
                    workerName = "josip", price = 200.00, carId = 3
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse(format.format(LocalDateTime.now().plusDays(5)), format),
                    workerName = "ante", price = 200.00, carId = 4
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse(format.format(LocalDateTime.now().plusHours(1)), format),
                    workerName = "matej", price = 200.00, carId = 3)
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse(format.format(LocalDateTime.now().plusWeeks(2)), format),
                    workerName = "mato", price = 200.00, carId = 3)
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse(format.format(LocalDateTime.now().plusDays(3)), format),
                    workerName = "borna", price = 200.00, carId = 4)
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.get("/car-checkups/upcoming") {
            param("duration", "ONE_WEEK")
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$._embedded.item.length()") {value(4)}

            jsonPath("$._embedded.item[0].workerName") { value("matej") }
            jsonPath("$._embedded.item[0]._links.car.href") {
                value("http://localhost/cars/3") }

            jsonPath("$._embedded.item[1].workerName") { value("josip") }
            jsonPath("$._embedded.item[1]._links.car.href") {
                value("http://localhost/cars/3") }

            jsonPath("$._embedded.item[2].workerName") { value("borna") }
            jsonPath("$._embedded.item[2]._links.car.href") {
                value("http://localhost/cars/4") }

            jsonPath("$._embedded.item[3].workerName") { value("ante") }
            jsonPath("$._embedded.item[3]._links.car.href") {
                value("http://localhost/cars/4") }

        }

        mvc.get("/car-checkups/upcoming") {
            param("duration", "HALF_YEAR")
        }.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$._embedded.item.length()") {value(7)}

            jsonPath("$._embedded.item[0].workerName") { value("matej") }
            jsonPath("$._embedded.item[0]._links.car.href") {
                value("http://localhost/cars/3") }

            jsonPath("$._embedded.item[1].workerName") { value("josip") }
            jsonPath("$._embedded.item[1]._links.car.href") {
                value("http://localhost/cars/3") }

            jsonPath("$._embedded.item[2].workerName") { value("borna") }
            jsonPath("$._embedded.item[2]._links.car.href") {
                value("http://localhost/cars/4") }

            jsonPath("$._embedded.item[3].workerName") { value("ante") }
            jsonPath("$._embedded.item[3]._links.car.href") {
                value("http://localhost/cars/4") }

            jsonPath("$._embedded.item[4].workerName") { value("mato") }
            jsonPath("$._embedded.item[4]._links.car.href") {
                value("http://localhost/cars/3") }

            jsonPath("$._embedded.item[5].workerName") { value("ivica") }
            jsonPath("$._embedded.item[5]._links.car.href") {
                value("http://localhost/cars/4") }

            jsonPath("$._embedded.item[6].workerName") { value("perica") }
            jsonPath("$._embedded.item[6]._links.car.href") {
                value("http://localhost/cars/4") }

        }

    }

//    @Test
//    @WithMockUser(authorities = ["SCOPE_ADMIN"])
//    fun getModelsInBase() {
//        mvc.post("/cars") {
//            content = objectMapper.writeValueAsString(
//                AddCarDTO(
//                    ownerId = 201,
//                    manufacturer = "Mazda",
//                    model = "323",
//                    productionYear = 1999,
//                    serialNumber = "2001",
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//        }
//
//        mvc.post("/cars") {
//            content = objectMapper.writeValueAsString(
//                AddCarDTO(
//                    ownerId = 200,
//                    manufacturer = "Mazda",
//                    model = "323",
//                    productionYear = 1999,
//                    serialNumber = "2000",
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//        }
//
//        mvc.post("/cars") {
//            content = objectMapper.writeValueAsString(
//                AddCarDTO(
//                    ownerId = 200,
//                    manufacturer = "Nissan",
//                    model = "Juke",
//                    productionYear = 2005,
//                    serialNumber = "2002",
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//        }
//
//        mvc.post("/cars") {
//            content = objectMapper.writeValueAsString(
//                AddCarDTO(
//                    ownerId = 203,
//                    manufacturer = "Fiat",
//                    model = "Punto",
//                    productionYear = 1995,
//                    serialNumber = "2007",
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//        }
//
//        mvc.post("/cars") {
//            content = objectMapper.writeValueAsString(
//                AddCarDTO(
//                    ownerId = 204,
//                    manufacturer = "Alfa Romeo",
//                    model = "145",
//                    productionYear = 2008,
//                    serialNumber = "2008",
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//        }
//
//        mvc.post("/cars") {
//            content = objectMapper.writeValueAsString(
//                AddCarDTO(
//                    ownerId = 204,
//                    manufacturer = "Fiat",
//                    model = "Punto",
//                    productionYear = 1999,
//                    serialNumber = "2010",
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//        }
//
//        mvc.post("/cars") {
//            content = objectMapper.writeValueAsString(
//                AddCarDTO(
//                    ownerId = 200,
//                    manufacturer = "BMW",
//                    model = "530",
//                    productionYear = 2010,
//                    serialNumber = "2009",
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//        }
//
//        mvc.get("/models")
//            .andExpect {
//                status { is2xxSuccessful() }
//                //jsonPath("$._embedded.item.length()") {value(5)}
//
//                jsonPath("$._embedded.item[0].manufacturer") { value("BMW") }
////                jsonPath("$._embedded.item") { CoreMatchers.hasItems<CarModel>(
////                    AddCarModelDTO("BMW", "530", 1),
////
////                ) }
//                jsonPath("$._embedded.item[0].modelName") { value("530") }
//
//                jsonPath("$._embedded.item[1].manufacturer") { value("Mazda") }
//                jsonPath("$._embedded.item[1].modelName") { value("323") }
//
//                jsonPath("$._embedded.item[2].manufacturer") { value("Fiat") }
//                jsonPath("$._embedded.item[2].modelName") { value("Punto") }
//
//                jsonPath("$._embedded.item[3].manufacturer") { value("Nissan") }
//                jsonPath("$._embedded.item[3].modelName") { value("Juke") }
//
//                jsonPath("$._embedded.item[4].manufacturer") { value("Alfa Romeo") }
//                jsonPath("$._embedded.item[4].modelName") { value("145") }
//
//            }
//    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun deleteCar() {
        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 200,
                    manufacturer = "Nissan",
                    model = "Juke",
                    productionYear = 2005,
                    serialNumber = "2002",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.delete("/cars/1")
            .andExpect {
                status { isNoContent() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun deleteCarUser() {
        mvc.post("/cars") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 200,
                    manufacturer = "Nissan",
                    model = "Juke",
                    productionYear = 2005,
                    serialNumber = "2002",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.delete("/cars/1")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun deleteCheckUp() {
        val carId = mvc.post("/cars") {
            content = objectMapper.writeValueAsString(
                AddCarDTO(
                    ownerId = 200,
                    manufacturer = "Nissan",
                    model = "Juke",
                    productionYear = 2005,
                    serialNumber = "2002",
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andReturn().response.getHeaderValue("location")
            .toString().split("/").last().toLong()


        mvc.post("/car-checkups") {
            content = objectMapper.writeValueAsString(
                AddCarCheckUpDTO(
                    timeAndDate = LocalDateTime.parse(format.format(LocalDateTime.now().plusDays(3)), format),
                    workerName = "borna", price = 200.00, carId = carId)
            )
            contentType = MediaType.APPLICATION_JSON
        }

        mvc.delete("/car-checkups/$carId")
            .andExpect {
                status { isNoContent() }
            }
    }



}
