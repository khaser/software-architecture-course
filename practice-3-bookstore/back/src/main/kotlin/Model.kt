package ru.mkn

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

enum class Category {
    Fiction,
    Fantasy,
    Romance,
    Programming,
}

typealias Name = String

@Serializable
data class Author(val name: Name)

@Serializable
data class Book(
    val id: Int,
    val name: String,
    val desc: String,
    val rating: Rating,
    val categories: List<Category>,
    val authors: List<Author>,
)

typealias BookSet = List<Book>
typealias Cart = BookSet
typealias WishList = BookSet

@Serializable
data class User(val id: Int, val name: Name, val address: String, val cart: Cart, val wishList: WishList)

typealias Rating = Double

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed interface Review {
    val id: Int
    val desc: String
    val rating: Rating
}

@Serializable
@SerialName("Critic")
data class CriticReview(
    val name: Name,
    override val id: Int,
    override val desc: String,
    override val rating: Rating,
) : Review

@Serializable
@SerialName("User")
data class UserReview(
    val userID: Int,
    override val id: Int,
    override val desc: String,
    override val rating: Rating,
    val isPublished: Boolean,
) : Review
