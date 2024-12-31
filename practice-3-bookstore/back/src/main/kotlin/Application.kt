package ru.mkn

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.configureCors() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true
    }
}

fun Application.mainModule() {
    install(Resources)
    configureCors()
    routing()
}

fun Application.routing() {
    val repo = MockRepository()
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
        route("/api") {
            get("/books") {
                call.respond(HttpStatusCode.OK, Json.encodeToString(repo.getAllBooks()))
            }
            get("/books/search") {
                val keyword = call.request.queryParameters["keyword"] ?: return@get call.respond(HttpStatusCode.BadRequest, "No keywords provided")
                call.respond(HttpStatusCode.OK, Json.encodeToString(repo.searchByKeyword(keyword)))
            }
            route("/book/{id}") {
                get("") {
                    val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid id")
                    call.respond(HttpStatusCode.OK, Json.encodeToString(repo.getBookById(id)))
                }
                get("reviews") {
                    val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid id")
                    call.respond(HttpStatusCode.OK, Json.encodeToString(repo.getAllReviewsById(id)))
                }
            }
        }
    }
}
