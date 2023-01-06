package de.felix0351.routes

import de.felix0351.models.objects.Auth
import de.felix0351.models.objects.Auth.PermissionLevel.*
import de.felix0351.models.objects.Content
import de.felix0351.plugins.asBsonObjectId
import de.felix0351.plugins.checkPermission
import de.felix0351.plugins.withInjection
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


/**
 *  Get all available meals
 *  GET /content/meals
 *
 */
fun Route.meals() = withInjection { service ->
    get("/meals") {

        val meals = service.contentRepo.getMeals()
        call.respond(HttpStatusCode.OK, meals)
    }
}


/**
 * Create/Get/Delete/Edit one specific meal
 * POST/PUT /content/meal
 * GET/DELETE/ /content/meal/<id>
 *
 */
fun Route.meal() = withInjection { service ->
    route("/meal") {
        // Create a meal
        post {
            //Worker Permission is needed
            checkPermission(service, Auth.PermissionLevel.WORKER) {
                val meal = call.receive<Content.Meal>()
                service.contentRepo.addMeal(meal)

                call.respond(HttpStatusCode.OK, meal.id.toString())
            }


        }

        // Update a meal
        put {
            //Worker Permission is needed
            checkPermission(service, Auth.PermissionLevel.WORKER) {
                val meal = call.receive<Content.Meal>()
                service.contentRepo.updateMeal(meal)

                call.respond(HttpStatusCode.OK)
            }
        }

        route("/{id}") {


            // Get the meal <id>
            get {

                val id = call.parameters["id"]!!
                val meal = service.contentRepo.getMeal(id.asBsonObjectId())

                call.respond(HttpStatusCode.OK, meal)

            }

            //Remove the meal <id>
            delete {

                val id = call.parameters["id"]!!
                //Worker Permission is needed
                checkPermission(service, Auth.PermissionLevel.WORKER) {
                    service.contentRepo.deleteMeal(id.asBsonObjectId())

                    call.respond(HttpStatusCode.OK)
                }

            }
        }
    }
}


/**
 * Get all reports
 * GET /content/reports
 *
 *
 */
fun Route.reports() = withInjection { service ->
    get("/reports") {

        val reports = service.contentRepo.getReports()
        call.respond(HttpStatusCode.OK, reports)

    }
}


/**
 * Create/Get/Delete/Edit one specific meal
 * POST /content/report
 * GET/DELETE/PUT /content/report/<id>
 *
 */
fun Route.report() = withInjection { service ->
    route("/report") {

        // Add a report
        post {
            //Worker permission is needed
            checkPermission(service, Auth.PermissionLevel.WORKER) {

                val report = call.receive<Content.Report>()
                service.contentRepo.addReport(report)

                call.respond(HttpStatusCode.OK, report.id.toString())

            }
        }

        // Update a report
        put {
            //Worker permission is needed
            checkPermission(service, Auth.PermissionLevel.WORKER) {

                val report = call.receive<Content.Report>()
                service.contentRepo.updateReport(report)

                call.respond(HttpStatusCode.OK)
            }
        }

        route("/{id}") {

            // Get the report <id>
            get {

                val id = call.parameters["id"]!!
                val report = service.contentRepo.getReport(id.asBsonObjectId())

                call.respond(HttpStatusCode.OK, report)
            }

            // Delete the report <id>
            delete {

                val id = call.parameters["id"]!!
                // Worker permission needed
                checkPermission(service, Auth.PermissionLevel.WORKER) {
                    service.contentRepo.deleteReport(id.asBsonObjectId())
                    call.respond(HttpStatusCode.OK)
                }

            }
        }
    }
}

/**
 *  Get all category templates
 *  GET /content/categories
 *
 */
fun Route.categories() = withInjection { service ->
    get("/categories") {
        checkPermission(service, WORKER) {
            val categories = service.contentRepo.getCategories()

            call.respond(HttpStatusCode.OK, categories)
        }
    }
}

/**
 *  Add or delete a category template
 *
 *  POST/DELETE /content/category
 *
 *
 */
fun Route.category() = withInjection {service ->
    route("/category") {
        post {
            checkPermission(service, WORKER) {
                val category = call.receive<Content.Category>()
                service.contentRepo.addCategory(category)

                call.respond(HttpStatusCode.OK)
            }

        }
        delete {
            checkPermission(service, WORKER) {
                val categoryName = call.receive<String>()
                service.contentRepo.deleteCategory(categoryName)

                call.respond(HttpStatusCode.OK)
            }

        }


    }

}

/**
 * Get all selection templates
 * GET /content/selections
 */
fun Route.selections() = withInjection { service ->
    get("/selections") {
        checkPermission(service, WORKER) {
            val selections = service.contentRepo.getSelections()
            call.respond(HttpStatusCode.OK, selections)
        }
    }
}

/**
 * Add/Delete a selection template
 * POST/DELETE /content/selection
 *
 */
fun Route.selection() = withInjection { service ->
    route("/selection") {
        post {
            checkPermission(service, WORKER) {
                val selections = call.receive<Content.SelectionGroup>()
                service.contentRepo.addSelections(selections)

                call.respond(HttpStatusCode.OK)
            }
        }

        delete {
            checkPermission(service, WORKER) {
                val selectionName = call.receive<String>()
                service.contentRepo.deleteSelection(selectionName)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}



fun Application.contentRoutes() {
    routing {

        // All content routes need a active user session
        authenticate("session") {
            route("/content") {
                meals()
                meal()
                reports()
                report()
                categories()
                category()
                selections()
                selection()
            }
        }



    }
}