package com.infinum.academyproject.config

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.springframework.context.annotation.Configuration
import org.springframework.format.Formatter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.print.attribute.standard.PrinterMoreInfoManufacturer

@Configuration
class MVCConfiguration : WebMvcConfigurer {

    /*override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatterForFieldType(MyData::class.java, object: Formatter<MyData> {
            override fun parse(text: String, locale: Locale): MyData {
                return MyData(text)
            }
            override fun print(theObject: MyData, locale: Locale): String {
                return theObject.text
            }
        })
    }*/

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatterForFieldType(Car::class.java, object: Formatter<Car> {
            override fun parse(text: String, locale: Locale): Car {
                println("U parseu je")
                val data: List<String> = text.split(",")
                println("list: ${data}")
                val ownerId: Long = data[0].toLong()
                val date: LocalDate = LocalDate.parse(data[1])
                val manufacturer = data[2]
                val prodYear = data[3].toInt()
                val serialNumber = data[4]
                println("Car has been made")
                return Car( ownerId, date, manufacturer, prodYear, serialNumber)
            }

            fun parse(ownerId: String, date: String, manufacturer: String, prodYear: String, serialNumber: String) : Car {
                println("U parseu 2 je.")
                return Car( ownerId.toLong(), LocalDate.parse(date), manufacturer, prodYear.toInt(), serialNumber)
            }

            override fun print(obj: Car, local: Locale): String {
                return obj.toString()
            }
        })

        registry.addFormatterForFieldType(CarCheckUp::class.java, object: Formatter<CarCheckUp> {
            override fun print(obj: CarCheckUp, locale: Locale): String {
                return obj.toString()
            }

            override fun parse(text: String, locale: Locale): CarCheckUp {
                val data: List<String> = text.split(",")
                val timeAndDate = LocalDateTime.parse(data[0])
                val workerName = data[1]
                val price = data[2].toDouble()
                val carId = data[3].toLong()
                return CarCheckUp(timeAndDate, workerName, price, carId)
            }

        })
    }
}