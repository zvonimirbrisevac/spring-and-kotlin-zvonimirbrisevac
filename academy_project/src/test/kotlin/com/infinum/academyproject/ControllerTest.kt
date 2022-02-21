//package com.infinum.academyproject
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.infinum.academyproject.dto.*
//import com.infinum.academyproject.errors.IllegalCarModelException
//import com.infinum.academyproject.errors.NoCarIdException
//import com.infinum.academyproject.models.Car
//import com.infinum.academyproject.models.CarCheckUp
//import com.infinum.academyproject.models.CarModel
//import com.infinum.academyproject.resources.assemblers.CarCheckUpResourceAssembler
//import com.infinum.academyproject.resources.assemblers.CarResourceAssembler
//import com.infinum.academyproject.services.CarService
//import com.infinum.academyproject.services.SchedulingService
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.mockito.Mockito
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.data.web.config.EnableSpringDataWebSupport
//import org.springframework.http.MediaType
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.get
//import org.springframework.test.web.servlet.post
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import java.time.LocalDate
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//
//@WebMvcTest
//class ControllerTest @Autowired constructor(
//    private val mvc: MockMvc,
//    private val objectMapper: ObjectMapper
//    //private val carResourceAssembler: CarResourceAssembler,
//    //private val carCheckUpResourceAssembler: CarCheckUpResourceAssembler
//) {
//
//
//    @MockBean
//    lateinit var service: CarService
//
//    @MockBean
//    lateinit var carResourceAssembler: CarResourceAssembler
//
//    @MockBean
//    lateinit var  carCheckUpResourceAssembler: CarCheckUpResourceAssembler
//
//    @MockBean
//    lateinit var scheduleService: SchedulingService
//
//    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//
//    val models = listOf(
//        CarModel(0, "Alfa Romeo", "145", true),
//        CarModel(0, "Audi", "A6", true),
//        CarModel(0, "BMW", "530", true),
//        CarModel(0, "Fiat", "Punto", true),
//        CarModel(0, "Ford", "Fiesta", true),
//        CarModel(0, "fake", "fake", false)
//    )
//
//    val cars = listOf(
//        Car(ownerId = 111, model = models[0], productionYear = 2015, serialNumber = "12345"),
//        Car(ownerId = 222, model = models[1], productionYear = 2010, serialNumber = "10101"),
//        Car(ownerId = 333, model = models[2], productionYear = 2000, serialNumber = "55555"),
//        Car(ownerId = 444, model = models[3], productionYear = 1998, serialNumber = "44444"),
//        Car(ownerId = 555, model = models[4], productionYear = 2005, serialNumber = "98765")
//    )
//
//    val carsCheckUps = listOf(
//        CarCheckUp(0, LocalDateTime.parse("2015-06-07 14:00", format), "mijo", 250.00, cars[0]),
//        CarCheckUp(0, LocalDateTime.parse("2016-11-30 08:00", format), "pero", 1000.00, cars[0]),
//        CarCheckUp(0, LocalDateTime.parse("2017-02-05 12:45", format), "ivo", 2000.00, cars[0]),
//        CarCheckUp(0, LocalDateTime.parse("2007-03-03 13:00", format), "matej", 2150.00, cars[1]),
//        CarCheckUp(0, LocalDateTime.parse("2010-03-15 12:15", format), "mijo", 500.00, cars[1]),
//        CarCheckUp(0, LocalDateTime.parse("2015-08-09 13:00", format), "jura", 750.00, cars[2]),
//        CarCheckUp(0, LocalDateTime.parse("2018-12-09 12:45", format), "drago", 3000.00, cars[2]),
//        CarCheckUp(0, LocalDateTime.parse("2013-10-01 14:50", format), "blaz", 4230.00, cars[2]),
//        CarCheckUp(0, LocalDateTime.parse("2014-04-25 09:35", format), "pejo", 210.00, cars[3])
//    )
//
//    @BeforeEach
//    fun setUp() {
//
//        for (i in 0..4) {
//            Mockito.`when`(service.addCar(AddCarDTO(cars[i]))).thenReturn(CarDTO(cars[i]))
//        }
//
//        for (i in 0..8) {
//            Mockito.`when`(service.addCarCheckUp(AddCarCheckUpDTO(carsCheckUps[i])))
//                .thenReturn(CarCheckUpDTO(carsCheckUps[i]))
//        }
//
//        Mockito.`when`(service.getCarCheckUps(1)).thenReturn(CarDTO(cars[0]))
//        Mockito.`when`(service.getCarCheckUps(2)).thenReturn(CarDTO(cars[1]))
//        Mockito.`when`(service.getCarCheckUps(3)).thenReturn(CarDTO(cars[2]))
//        Mockito.`when`(service.getCarCheckUps(4)).thenReturn(CarDTO(cars[3]))
//        Mockito.`when`(service.getCarCheckUps(5)).thenReturn(CarDTO(cars[4]))
//        Mockito.`when`(service.getCarCheckUps(777)).thenThrow(RuntimeException::class.java)
//        Mockito.`when`(
//            service.addCarCheckUp(
//                AddCarCheckUpDTO(
//                    CarCheckUp(0, LocalDateTime.parse("2020-02-02 01:33", format), "david", 20.00, cars[2])
//                )
//            )
//        ).thenThrow(RuntimeException::class.java)
//
//        Mockito.`when`(
//            service.addCar(
//                AddCarDTO(
//                    ownerId = 1234,
//                    manufacturer = "BMW",
//                    model = "530",
//                    productionYear = 2012,
//                    serialNumber = "102030"
//                )
//            )
//        ).thenReturn(
//            CarDTO(
//                id = 0, ownerId = 1234, manufacturer = "BMW", model = "530",
//                productionYear = 2012, serialNumber = "102030"
//            )
//        )
//
//        Mockito.`when`(
//            service.addCarCheckUp(
//                AddCarCheckUpDTO(LocalDateTime.parse("2012-05-21 12:00", format), "miro", 100.00, cars[0].id)
//            )
//        ).thenReturn(
//            CarCheckUpDTO(
//                CarCheckUp(0, LocalDateTime.parse("2012-05-21 12:00", format), "miro", 100.00, cars[0])
//            )
//        )
//
//        Mockito.`when`(
//            service.addCar(
//                AddCarDTO(
//                    ownerId = 1234,
//                    manufacturer = "fejk",
//                    model = "fejk",
//                    productionYear = 2013,
//                    serialNumber = "9999"
//                )
//            )
//        ).thenThrow(RuntimeException::class.java)
//    }
//
//    @AfterEach
//    fun clearSetUp() {
//        service.deleteAllCars()
//        service.deleteAllCarCheckUps()
//    }
//
//    @Test
//    fun storeSimpleCar() {
//        mvc.post("/cars") {
//            content = objectMapper.writeValueAsString(
//                AddCarDTO(
//                    ownerId = 1234,
//                    manufacturer = "BMW",
//                    model = "530",
//                    productionYear = 2012,
//                    serialNumber = "102030"
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//        }.andExpect {
//            status { is2xxSuccessful() }
////            jsonPath("$.ownerId") { value(1234) }
////            jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
////            jsonPath("$.manufacturer") { value("BMW") }
////            jsonPath("$.model") { value("530") }
////            jsonPath("$.productionYear") { value(2012) }
////            jsonPath("$.serialNumber") { value("102030") }
//        }
//    }
//
//    @Test
//    fun storeSimpleCheckUp() {
//        mvc.post("/car-checkups") {
//            content = objectMapper.writeValueAsString(
//                AddCarCheckUpDTO(
//                    LocalDateTime.parse("2012-05-21 12:00", format), "miro", 100.00, cars[0].id
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//
//        }.andExpect {
//            status { is2xxSuccessful() }
////            jsonPath("timeAndDate") { value(format.format(LocalDateTime.parse("2012-05-21 12:00", format))) }
////            jsonPath("workerName") { value("miro") }
////            jsonPath("price") { value(100.00) }
//        }
//    }
//
//    @Test
//    fun storeCheckUpFail() {
//        mvc.post("/car-checkups") {
//            content = objectMapper.writeValueAsString(
//                AddCarCheckUpDTO(LocalDateTime.parse("2020-02-02 01:33", format), "david", 20.00, cars[2].id)
//            )
//            contentType = MediaType.APPLICATION_JSON
//
//        }.andExpect {
//            status { is4xxClientError() }
//        }
//    }
//
//    @Test
//    fun getCarCheckUps1() {
//        mvc.get("/cars/3/checkups") {
//            param("size", "10")
//            param("page", "0")
//        }
//            .andExpect {
//                status { is2xxSuccessful() }
////                jsonPath("$.checkUpsDTO[0].timeAndDate") {
////                    value(
////                        format.format(
////                            LocalDateTime.parse(
////                                "2018-12-09 12:45",
////                                format
////                            )
////                        )
////                    )
////                }
////                jsonPath("$.checkUpsDTO[0].workerName") { value("drago") }
////                jsonPath("$.checkUpsDTO[0].price") { value(3000.00) }
////                jsonPath("$.checkUpsDTO[1].timeAndDate") {
////                    value(
////                        format.format(
////                            LocalDateTime.parse(
////                                "2015-08-09 13:00",
////                                format
////                            )
////                        )
////                    )
////                }
////                jsonPath("$.checkUpsDTO[1].workerName") { value("jura") }
////                jsonPath("$.checkUpsDTO[1].price") { value(750.00) }
////                jsonPath("$.checkUpsDTO[2].timeAndDate") {
////                    value(
////                        format.format(
////                            LocalDateTime.parse(
////                                "2013-10-01 14:50",
////                                format
////                            )
////                        )
////                    )
////                }
////                jsonPath("$.checkUpsDTO[2].workerName") { value("blaz") }
////                jsonPath("$.checkUpsDTO[2].price") { value(4230.00) }
//            }
//    }
//
//    @Test
//    fun getCarCheckUpsFail() {
//        mvc.get("/cars/777/checkups")
//            .andExpect {
//                status { is4xxClientError() }
//            }
//    }
//
//    @Test
//    fun getCarCheckUps2() {
//        mvc.get("/cars/4/checkups")
//            .andExpect {
//                status { is2xxSuccessful() }
////                jsonPath("$.checkUpsDTO[0].timeAndDate") {
////                    value(
////                        format.format(
////                            LocalDateTime.parse(
////                                "2014-04-25 09:35",
////                                format
////                            )
////                        )
////                    )
////                }
////                jsonPath("$.checkUpsDTO[0].workerName") { value("pejo") }
////                jsonPath("$.checkUpsDTO[0].price") { value(210.00) }
//            }
//    }
//
//    @Test
//    fun getCarCheckUps3() {
//        mvc.get("/cars/5/checkups")
//            .andExpect {
//                status { is2xxSuccessful() }
////                //jsonPath("$.id") { value(5) }
////                jsonPath("$.ownerId") { value(555) }
////                jsonPath("$.addedDate") { value(LocalDate.now().toString()) }
////                jsonPath("$.manufacturer") { value("Ford") }
////                jsonPath("$.productionYear") { value(2005) }
////                jsonPath("$.serialNumber") { value("98765") }
////                jsonPath("$.checkUpsDTO") { isEmpty() }
//            }
//    }
//
//    @Test
//    fun saveCarNoModelFail() {
//        mvc.post("/cars/add") {
//            content = objectMapper.writeValueAsString(
//                AddCarDTO(
//                    ownerId = 1234,
//                    manufacturer = "fejk",
//                    model = "fejk",
//                    productionYear = 2013,
//                    serialNumber = "9999"
//                )
//            )
//            contentType = MediaType.APPLICATION_JSON
//        }.andExpect {
//            status { is4xxClientError() }
//        }
//    }
//
//
//}