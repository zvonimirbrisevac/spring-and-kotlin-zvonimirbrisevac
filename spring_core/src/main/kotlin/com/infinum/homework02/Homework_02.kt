package com.infinum.homework02

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI
import java.net.URL

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
class ApplicationConfiguration

interface CourseRepository {
    fun insert(name: String): Long
    fun findById(id: Long): Course
    fun deleteById(id: Long): Course
}

data class Course(
    val id: Long,
    val name: String
)

@Component
class CourseService(
    @Qualifier("a") private val repository: CourseRepository
) {

    fun insertCourse(courseName: String): Long = repository.insert(courseName)

    fun findCourseById(courseId: Long) = repository.findById(courseId)

    fun deleteCourseById(courseId: Long) = repository.deleteById(courseId)
}

@Component
@Qualifier("a")
class InMemoryCourseRepository(dataSource: DataSource) : CourseRepository {
    private val courses = mutableMapOf<Long, Course>()

    init {
        println(dataSource)
    }

    override fun insert(name: String): Long {
        val id = (courses.keys.maxOrNull() ?: 0) + 1
        courses[id] = Course(id, name)
        return id
    }

    override fun findById(id: Long): Course {
        return courses[id] ?: throw CourseNotFoundException(id)
    }

    override fun deleteById(id: Long): Course {
        return courses.remove(id) ?: throw CourseNotFoundException(id)
    }
}

class CourseNotFoundException(id: Long) : RuntimeException("Course with and ID $id not found")

@Component
class InFileCourseRepository(
    @Value("\${database.name}") private val coursesFileResource: Resource// will be provided through dependency
) : CourseRepository {
    init {
        if (coursesFileResource.exists().not()) {
            //println("Courses file resource: ${coursesFileResource}")
            coursesFileResource.file.createNewFile()
        }
    }

    override fun insert(name: String): Long {
        val file = coursesFileResource.file
        val id = (file.readLines()
            .filter { it.isNotEmpty() }
            .map { line -> line.split(",").first().toLong() }
            .maxOrNull() ?: 0) + 1
        file.appendText("$id,$name\n")
        return id
    }

    override fun findById(id: Long): Course {
        return coursesFileResource.file.readLines()
            .filter { it.isNotEmpty() }
            .find { line -> line.split(",").first().toLong() == id }
            ?.let { line ->
                val tokens = line.split(",")
                Course(id = tokens[0].toLong(), name = tokens[1])
            }
            ?: throw CourseNotFoundException(id)
    }

    override fun deleteById(id: Long): Course {
        val coursesLines = coursesFileResource.file.readLines()
        var lineToDelete: String? = null
        FileOutputStream(coursesFileResource.file)
            .writer()
            .use { fileOutputWriter ->
                coursesLines.forEach { line ->
                    if (line.split(",").first().toLong() == id) {
                        lineToDelete = line
                    } else {
                        fileOutputWriter.appendLine(line)
                    }
                }
            }
        return lineToDelete?.let { line ->
            val tokens = line.split(",")
            Course(id = tokens[0].toLong(), name = tokens[1])
        } ?: throw CourseNotFoundException(id)
    }
}

@Component
data class DataSource(
    @Value("\${database.name}") val dbName: String,
    @Value("\${database.user}") val username: String,
    @Value("\${database.password}") val password: String
)

fun getFile(): Resource = DefaultResourceLoader().getResource("\${database.name}")


val listOfCourses: List<Course> = listOf(
    Course(1, "spring kotlin"),
    Course(2, "ruby on rails"),
    Course(3, "android"),
    Course(4, "angular"),
    Course(5, "python"),
    Course(6, "c++"),
    Course(7, "c#")
)

fun main() {

    val appContext = AnnotationConfigApplicationContext(ApplicationConfiguration::class.java)

    val service = appContext.getBean(CourseService::class.java)

    println("Spring kotlin id: ${service.insertCourse("spring")}, should be: 1")
    println("Ruby id: ${service.insertCourse("ruby")}, should be: 2")
    println("Android id: ${service.insertCourse("android")}, should be: 3")

    println("Trying to find id 2, found: ${service.findCourseById(2)}, should found: ruby")
    try {
        service.findCourseById(5)
    } catch (e: CourseNotFoundException) {
        println("Tryed to found course with id 5, but exception was thrown")
    }

    println("Deleting course with id 3, deleted: ${service.deleteCourseById(3)}, should deleted: android")
    try{
        service.deleteCourseById(4)
    } catch ( e: CourseNotFoundException) {
        println("Tryed to delete course with id 4, but exception was thrown")
    }

}