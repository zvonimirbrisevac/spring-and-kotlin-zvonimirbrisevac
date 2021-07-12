import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.FileOutputStream

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
    private val repository: CourseRepository
) {

    fun insertCourse(courseName: String): Long = repository.insert(courseName)

    fun findCourseById(courseId: Long) = repository.findById(courseId)

    fun deleteCourseById(courseId: Long) = repository.deleteById(courseId)
}

@Component
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
    @Value("\${database.name}") private val coursesFileResource: Resource // will be provided through dependency
) : CourseRepository {
    init {
        if (coursesFileResource.exists().not()) {
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
    @Value("\${database.username}") val username: String,
    @Value("\${database.password}") val password: String
)


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


}