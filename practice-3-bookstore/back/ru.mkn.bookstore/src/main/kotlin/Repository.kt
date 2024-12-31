package ru.mkn

interface BookRepository {
    fun getAllBooks(): BookSet

    fun getBookById(id: Int): Book?

}

interface ReviewRepository {
    fun getAllReviewsById(id: Int): List<Review>
}

class MockRepository : BookRepository, ReviewRepository {

    private val mockBook = listOf(
        Book(
            0,
            name = "Compilers: Principles, Techniques, and Tools",
            desc = "Detailed introduction to the compiler development",
            categories = listOf(Category.Programming),
            rating = 7.5,
            authors = listOf(Author("Alfred Aho"), Author("Jeffrey Ullman"))
        ),
        Book(
            1,
            name = "Pride and Prejudice",
            desc = "The best romantic novel of Jane Austen",
            categories = listOf(Category.Romance),
            rating = 7.0,
            authors = listOf(Author("Jane Austen"))
        )
    )

    private val mockReviews = mapOf(
        0 to listOf(
            CriticReview(
                "Vladimir N. Makarov",
                0,
                "In general, 2nd edition is not the best description of all compiler tasks and problems" +
                        " (some of them are pretty weak) but parts of them (garbage collection, optimization" +
                        " for locality and using modern processors parallelism) makes sense to have it on the" +
                        " bookshelf. But personally I am not going to buy this book.",
                6.0
            ),
            UserReview(0, 1, "Very fundamental introduction to the compilers, was glad to buy it for yourself!", 9.0, true),
            UserReview(1, 2, "Shit book", 0.0, false)
        ),
        1 to listOf(UserReview(0, 0, "Very romantic book", 7.0, true))
    )

    override fun getAllBooks(): BookSet = mockBook

    fun searchByKeyword(keyword: String): BookSet = getAllBooks().filter { keyword in it.name }

    override fun getBookById(id: Int): Book? = mockBook.find { it.id == id }
    override fun getAllReviewsById(id: Int): List<Review> = mockReviews[id] ?: listOf()

}
