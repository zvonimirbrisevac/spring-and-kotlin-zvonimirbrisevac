package com.infinum.academyproject.repositories

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface CheckUpRepository : Repository<CarCheckUp, Long> {

    fun save(checkUp: CarCheckUp): CarCheckUp

    fun findByCarOrderByTimeAndDateDesc(car: Car) : List<CarCheckUp>

    fun findAllByCarOrderByTimeAndDateDesc(pageable: Pageable, car : Car) : Page<CarCheckUp>

    fun saveAll(checkUps: Iterable<CarCheckUp>) : List<CarCheckUp>

    fun deleteAll()

    fun findAll() : List<CarCheckUp>

    fun findByWorkerNameAndPrice(workerName : String, price : Double) : CarCheckUp

    @Query(value = "select c from checkups c where c.time_and_date <= current_timestamp order by c.time_and_date desc",
        countQuery = "select count(*) from checkups c where c.time_and_date <= current_timestamp " +
                "order by c.time_and_date desc", nativeQuery = true)
    fun findLastTenCheckUps(pageable: Pageable): Page<CarCheckUp>

    @Query(value = "select c from checkups c where c.time_and_date > current_timestamp and " +
            "c.time_and_date - :interval <= current_timestamp  order by c.time_and_date",
    countQuery = "select count(*) from checkups c where c.time_and_date > current_timestamp and " +
            "c.time_and_date - :interval <= current_timestamp  order by c.time_and_date",
    nativeQuery = true)
    fun findUpcomingCheckUps(interval: String, pageable: Pageable): Page<CarCheckUp>
}