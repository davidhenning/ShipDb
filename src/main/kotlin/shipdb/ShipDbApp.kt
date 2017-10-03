package shipdb

import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.Routing
import org.jetbrains.ktor.routing.get

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(GsonSupport) {
        setPrettyPrinting()
    }

    val ships = listOf(
            Ship(1, "Enterprise", "NCC-1701-E"),
            Ship(2, "Defiant", "NX-74205")
    )

    install(Routing) {
        get("/") {
            call.respondText("<h1>Welcome to ShipDB</h1>", ContentType.Text.Html)
        }

        get("/api") {
            call.respondText("{}", ContentType.Application.Json)
        }

        get("/api/v1/ship/{id}") {
            val item = ships.firstOrNull { it.id == call.parameters["id"]?.toInt() }

            if (item == null) {
                val error = ErrorResponse("Ship not found")
                call.response.status(HttpStatusCode.NotFound)
                call.respond(error)
            } else {
                call.respond(item)
            }
        }
    }
}
