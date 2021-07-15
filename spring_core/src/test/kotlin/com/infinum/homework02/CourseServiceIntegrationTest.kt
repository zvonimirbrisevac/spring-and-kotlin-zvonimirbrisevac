package com.infinum.homework02

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(ApplicationConfiguration::class)
class CourseServiceIntegrationTest @Autowired constructor(
    private val applicationContext: ApplicationContext,
    private val service: CourseService
) {

//    init {
//        service.insertCourse("spring and kotlin")
//        service.insertCourse("java")
//        service.insertCourse("python")
//        service.insertCourse("ruby")
//        service.insertCourse("angular")
//    }

    @BeforeEach
    fun setInitialState() {
        service.insertCourse("spring and kotlin")
        service.insertCourse("java")
        service.insertCourse("python")
        service.insertCourse("ruby")
        service.insertCourse("angular")
    }

    @AfterEach
    fun cleanUp() {
        for (i in 1..20) {
            try {
                service.deleteCourseById(i.toLong())
            } catch (e: CourseNotFoundException) {

            }
        }
    }

    @Test
    fun verifyServiceBean() {
        Assertions.assertThat(applicationContext.getBean(CourseService::class.java)).isNotNull
    }

    @Test
    fun verifyInMemoryRepoBean() {
        Assertions.assertThat(applicationContext.getBean(InMemoryCourseRepository::class.java)).isNotNull
    }

    @Test
    fun verifyInFileRepoBean() {
        Assertions.assertThat(applicationContext.getBean(InFileCourseRepository::class.java)).isNotNull
    }

    @Test
    fun verifyDataSourceBean() {
        Assertions.assertThat(applicationContext.getBean(DataSource::class.java)).isNotNull
    }

    @Test
    fun testInsertions() {

        Assertions.assertThat(service.insertCourse("c++")).isEqualTo(6)
        Assertions.assertThat(service.insertCourse("android")).isEqualTo(7)

    }

    @Test
    fun testDelete() {

        Assertions.assertThat(service.deleteCourseById(3)).isEqualTo(Course(3, "python"))
        Assertions.assertThat(service.deleteCourseById(4)).isEqualTo(Course(4, "ruby"))

    }

    @Test
    fun testDeleteException() {

        Assertions.assertThatThrownBy {
            service.deleteCourseById(10)
        }.isInstanceOf(CourseNotFoundException::class.java)
    }

    @Test
    fun testFind() {
        Assertions.assertThat(service.findCourseById(4)).isEqualTo(Course(4, "ruby"))
        Assertions.assertThat(service.findCourseById(5)).isEqualTo(Course(5, "angular"))
    }

    @Test
    fun testFindException() {
        Assertions.assertThatThrownBy {
            service.findCourseById(6)
        }.isInstanceOf(CourseNotFoundException::class.java)

    }

    @Test
    fun deleteThenFind() {
        service.deleteCourseById(4)
        Assertions.assertThatThrownBy {
            service.findCourseById(4)
        }.isInstanceOf(CourseNotFoundException::class.java)
    }

    @Test
    fun insertThenFind() {
        service.insertCourse("c#")
        Assertions.assertThat(service.findCourseById(6)).isEqualTo(Course(6, "c#"))
    }
}