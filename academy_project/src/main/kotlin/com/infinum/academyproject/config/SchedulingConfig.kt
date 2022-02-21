package com.infinum.academyproject.config

import com.infinum.academyproject.services.SchedulingService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled


@Configuration
@EnableScheduling
class SchedulingConfig(
    private val schedulingService : SchedulingService
) {

    @Scheduled(fixedRate = 86400000)
    fun updateModels() {
        schedulingService.updateModels()
    }

}