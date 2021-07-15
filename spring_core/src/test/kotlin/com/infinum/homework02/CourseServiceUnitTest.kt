package com.infinum.homework02

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UnitTest {

    private val repository = mockk<CourseRepository>()

    private lateinit var service: CourseService

    @BeforeEach
    fun setUp() {
        service = CourseService(repository)

    }

    @Test
    fun testFirstInsertion() {

        every {
            repository.insert("spring and kotlin")
        } returns 1

        val testService = CourseService(repository)

        val actualId = testService.insertCourse("spring and kotlin")

        Assertions.assertThat(actualId).isEqualTo(1)
    }

    @Test
    fun testMultiInsertions () {
        every {
            repository.insert("android")
        } returns 6

        val actualId = service.insertCourse("android")

        Assertions.assertThat(actualId).isEqualTo(6)
    }

    @Test
    fun testFindingCourse() {

        every {
            repository.findById(3)
        } returns Course(3, "python")

        Assertions.assertThat(service.findCourseById(3)).isEqualTo(Course(3, "python"))

    }

    @Test
    fun testExceptionFindById() {

        every {
            repository.findById(6)
        } throws CourseNotFoundException(6)

        Assertions.assertThatThrownBy{
            service.findCourseById(6) }.isInstanceOf(CourseNotFoundException::class.java)
    }

    @Test
    fun testDeleteById() {

        every {
            repository.deleteById(4)
        } returns Course(4, "ruby")

        Assertions.assertThat(service.deleteCourseById(4)).isEqualTo(Course(4, "ruby"))
    }

    @Test
    fun testDeleteException() {

        every {
            repository.deleteById(7)
        } throws CourseNotFoundException(7)

        Assertions.assertThatThrownBy {
            service.deleteCourseById(7)
        }.isInstanceOf(CourseNotFoundException::class.java)
    }

}