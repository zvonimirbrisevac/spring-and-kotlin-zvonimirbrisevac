package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface CheckUpRepository : Repository<CarCheckUp, Long> {

    fun save(checkUp: CarCheckUp): CarCheckUp

    fun findByCarOrderByTimeAndDateDesc(car: Car): List<CarCheckUp>

    fun findAllByCarOrderByTimeAndDateDesc(pageable: Pageable, car: Car): Page<CarCheckUp>

    fun saveAll(checkUps: Iterable<CarCheckUp>): List<CarCheckUp>

    fun deleteAll()

    fun findAll(): List<CarCheckUp>

    fun findByWorkerNameAndPrice(workerName: String, price: Double): CarCheckUp

    fun deleteById(id: Long)

    //fun findByTimeAndDateBefore(pageable:Pageable, endTime: LocalDateTime): Page<CarCheckUp>

    //drugačije mi ne radi pa sam ostavio ovako jer sam u žurbi, gledat ću da kasnije popravim
    @Query(
        value = "select * from checkups c where c.time_and_date <= current_timestamp " +
                "order by c.time_and_date desc",
        countQuery = "select count(*) from checkups c where c.time_and_date <= current_timestamp",
        nativeQuery = true
    )
    fun findLastTenCheckUps(pageable: Pageable): Page<CarCheckUp>

    @Query(
        value = "select * from checkups c where c.time_and_date > current_timestamp(0) and " +
                "c.time_and_date - make_interval(weeks => :weeks, months => :months ) <= current_timestamp(0)  order by c.time_and_date",
        countQuery = "select count(*) from checkups c where c.time_and_date > current_timestamp(0) and " +
                "c.time_and_date - make_interval(weeks => :weeks, months => :months ) <= current_timestamp(0)",
        nativeQuery = true
    )
    fun findUpcomingCheckUps(
        @Param("weeks") numWeeks: Int = 0,
        @Param("months") numMonths: Int = 0,
        pageable: Pageable
    ): Page<CarCheckUp>
}