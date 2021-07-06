import java.time.LocalDate

data class Book (
    val title: String,
    val authorName: String,
    val inventoryNumber: Int,
    var dueToDate: LocalDate?
    )

enum class RentDuration {
    TWO_WEEKS, MONTH, TWO_MONTHS
}

object Library {
    var avaliableBooks: List<Book> = listOf(
        Book("Detransition, baby", "Torrey Petres", 1, null),
        Book("Nora", "Nuala O'Connor", 2, null),
        Book("Aftershocks", "Nadia Owusu", 3, null),
        Book("Aftershocks", "Nadia Owusu", 4, null),
        Book("Let me tell you wath I mean", "Joan Didion", 5, null),
        Book("Milk Fed", "Melissa Broder", 6, null),
        Book("Milk Fed", "Melissa Broder", 7, null),
        Book("My year abroad", "Chang Rae Lee", 8, null),
        Book("Milk Fed", "Melissa Broder", 9, null),
        Book ("Tom Stoppard: A life", "Hermione Lee", 10, null)
    )
    var rentedBooks: Map<String, List<Book>> = mapOf()

    fun isBookAvailable(title: String, authorName: String): Boolean{
        for (book in avaliableBooks)
            if (book.authorName == authorName && book.title == title)
                return true

        return false
    }

    fun rentBook(title: String, authorName: String, customerOIB: String, duration: RentDuration): Book? {
        for (book in avaliableBooks)
            if (book.title == title && book.authorName == authorName) {
                if (rentedBooks.containsKey(customerOIB))
                    rentedBooks.get(customerOIB)?.toMutableList()?.add(book)
                else
                    rentedBooks.toMutableMap().put(customerOIB, listOf(book))

                book.copy(dueToDate = when(duration) {
                    RentDuration.TWO_WEEKS -> LocalDate.now().plusWeeks(2)
                    RentDuration.MONTH -> LocalDate.now().plusMonths(1)
                    RentDuration.TWO_MONTHS -> LocalDate.now().plusMonths(2)
                })
                avaliableBooks.toMutableList().remove(book)
                return book
            }
        return null
    }

    fun returnBook(book: Book) {
        for ((customer, list) in rentedBooks)
            if (list.contains(book)) {
                list.toMutableList().remove(book)
                book.copy(dueToDate = null)
                avaliableBooks.toMutableList().add(book)
                return
            }
        throw Exception("This book was not rented.")
    }

    fun isBookRented(book: Book): Boolean {
        for ((customer, list) in rentedBooks)
            if (list.contains(book))
                return true

        return false
    }

    fun getRentedBooks(customerOIB: String): List<Book> {
        if (rentedBooks.containsKey(customerOIB))
            return rentedBooks.getValue(customerOIB)

        throw Exception("Customer has not rented any books.")
    }
}
