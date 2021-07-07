import java.time.LocalDate

data class Book (
    val title: String,
    val authorName: String,
    val inventoryNumber: Int,
    )

data class RentedBook (
    val book: Book,
    val dueToDate: LocalDate
        )

enum class RentDuration {
    TWO_WEEKS, MONTH, TWO_MONTHS
}

object Library {
    var avaliableBooks: MutableList<Book> = mutableListOf(
        Book("Detransition, baby", "Torrey Petres", 1),
        Book("Nora", "Nuala O'Connor", 2),
        Book("Aftershocks", "Nadia Owusu", 3),
        Book("Aftershocks", "Nadia Owusu", 4),
        Book("Let me tell you wath I mean", "Joan Didion", 5),
        Book("Milk Fed", "Melissa Broder", 6),
        Book("Milk Fed", "Melissa Broder", 7),
        Book("My year abroad", "Chang Rae Lee", 8),
        Book("Milk Fed", "Melissa Broder", 9),
        Book ("Tom Stoppard: A life", "Hermione Lee", 10)
    ) // Vogues top 10 books of 2020
    var rentedBooks: MutableMap<String, MutableMap<Book, LocalDate>> = mutableMapOf() //key - customer, value - map of books with their due to dates

    fun isBookAvailable(title: String, authorName: String): Boolean{
        for (book in avaliableBooks)
            if (book.authorName == authorName && book.title == title)
                return true

        return false
    }

    fun rentBook(title: String, authorName: String, customerOIB: String, duration: RentDuration): Book? {
        for (book in avaliableBooks)
            if (book.title == title && book.authorName == authorName) {
                val date = when(duration) {
                    RentDuration.TWO_WEEKS -> LocalDate.now().plusWeeks(2)
                    RentDuration.MONTH -> LocalDate.now().plusMonths(1)
                    RentDuration.TWO_MONTHS -> LocalDate.now().plusMonths(2)
                }
                if (rentedBooks.containsKey(customerOIB))
                    rentedBooks.get(customerOIB)?.put(book, date)
                else
                    rentedBooks.put(customerOIB, mutableMapOf(Pair(book, date)))

                avaliableBooks.remove(book)
                return book
            }
        return null
    }

    fun returnBook(book: Book) {
        for ((customer, map) in rentedBooks)
            if (map.containsKey(book)) {
                map.remove(book)
                avaliableBooks.add(book)
                return
            }
        throw Exception("This book was not rented.")
    }

    fun isBookRented(book: Book): Boolean {
        for (map in rentedBooks.values)
            if (map.containsKey(book))
                return true

        return false
    }

    fun getRentedBooks(customerOIB: String): List<Pair<Book, LocalDate>> {
        if (rentedBooks.containsKey(customerOIB) && rentedBooks.getValue(customerOIB)?.isNotEmpty())
            return rentedBooks.getValue(customerOIB).toList()

        throw Exception("Customer has not rented any books.")
    }
}

fun main(args: Array<String>) {

    println("${Book("bzvz", "bzvz", 1) == Book("bzvz", "bzvz", 1)}")
    println("${Book("bzvz","bzvz", 1) == Book("bzvz", "bzvz", 2)}")

    println("Book \"Milk Fed\" by Melissa Broder is avaliable: " +
            "${Library.isBookAvailable("Milk Fed", "Melissa Broder")}") // prints true
    println("Book \"Let me tell you wath I mean\" by Joan Didion is avaliable: " +
            "${Library.isBookAvailable("Let me tell you wath I mean", "Joan Didion")}") // prints true
    println(("Book \"Let me tell you wath I mean\" by Melissa Broder is avaliable: " +
            "${Library.isBookAvailable("Let me tell you wath I mean", "Melissa Broder")}")) // prints false
    println("Book \"Vlak u snijegu\" by Mato Lovrak is avaliable: " +
            "${Library.isBookAvailable("Vlak u snijegu", "Mato Lovrak")}") // prints false
    println()

    /*Book("Detransition, baby", "Torrey Petres", 1),
        Book("Nora", "Nuala O'Connor", 2),
        Book("Aftershocks", "Nadia Owusu", 3),
        Book("Aftershocks", "Nadia Owusu", 4),
        Book("Let me tell you wath I mean", "Joan Didion", 5),
        Book("Milk Fed", "Melissa Broder", 6),
        Book("Milk Fed", "Melissa Broder", 7),
        Book("My year abroad", "Chang Rae Lee", 8),
        Book("Milk Fed", "Melissa Broder", 9),
        Book ("Tom Stoppard: A life", "Hermione Lee", 10)*/
    println("User 111 rented a book: ${Library.rentBook("Milk Fed", "Melissa Broder", "111",
                                                            RentDuration.TWO_MONTHS)}")
    println("User 222 rented a book: ${Library.rentBook("Aftershocks", "Nadia Owusu", "222",
                                                            RentDuration.MONTH)}")
    println("User 111 rented a book: ${Library.rentBook("Aftershocks", "Nadia Owusu", "111",
                                                            RentDuration.TWO_WEEKS)}")
    println("User 333 rented a book: ${Library.rentBook("My year abroad", "Chang Rae Lee", 
                                                "333", RentDuration.MONTH)}")
    println("User 333 rented a book: ${if (Library.rentBook("Aftershocks", "Nadia Owusu", 
                                                "333", RentDuration.MONTH) == null) "null" else "he did it"}") //prints null
    println()
    println("Book \"Aftershocks\" by Nadia Owusu is avaliable: ${Library.isBookAvailable("Aftershocks", 
                                                        "Nadia Owusu")}") //prints false
    println("Book \"Milk Fed\" by Melissa Broder is avaliable: ${Library.isBookAvailable("Milk Fed", 
                                                                                "Melissa Broder")}") // prints true
    println()
    println("Book \"Milk Fed\" is rented: ${Library.isBookRented(Book("Milk Fed", "Melissa Broder", 6))}") //prints true
    println("Book \"Tom Stoppard: A life\" is rented: ${Library.isBookRented(Book("Tom Stoppard: A life", 
                                                            "Hermione Lee", 10))}") // prints false
    Library.returnBook(Book("My year abroad", "Chang Rae Lee", 8))
    println()
    println("Returned \"My year abroad\"")
    try {
        Library.returnBook(Book("Detransition, baby", "Torrey Petres", 1))
    } catch (e: Exception) {
        println("Tryed to return \"Destransition, baby\" but exception is thrown")
    }

    println()
    println("Customer 111 rented books:")
    for(pair in Library.getRentedBooks("111"))
        println("${pair.first}, return by: ${pair.second}")
    println()
    println("Customer 222 rented books:")
    for (pair in Library.getRentedBooks("222"))
        println("${pair.first}, return by: ${pair.second}")
    println()
    try {
        for (pair in Library.getRentedBooks("333"))
            println("${pair.first}, return by: ${pair.second}")

    } catch (e: Exception) {
        println("Tried to fetch 333 rented books but exception is thrown.") // catches exception
    }
}
