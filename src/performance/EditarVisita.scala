package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EditarVisita extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-419,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(10)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}

	object Pets {
		val pets = exec(http("Pets")
			.get("/vets/pets")
			.headers(headers_0))
		.pause(12)
	}

	object PetVisits {
		val petVisits = exec(http("PetVisits")
			.get("/vets/pets/2/visits")
			.headers(headers_0))
		.pause(14)
	}

	object EditVisit {
		val editVisit = exec(http("EditVisitForm")
			.get("/vets/pets/2/visits/6")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))			
		.pause(12)
		.exec(http("EditVisit")
			.post("/vets/pets/2/visits/6")
			.headers(headers_3)
			.formParam("date", "2020/05/01")
			.formParam("description", "Dental checkup made. Everything is correct.")
			.formParam("medicalTests", "Radiography")
			.formParam("_medicalTests", "on")
			.formParam("petId", "2")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	object ErrorEditVisit {
		val errorEditVisit = exec(http("ErrorEditVisitForm")
			.get("/vets/pets/2/visits/6")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))			
		.pause(12)
		.exec(http("ErrorEditVisit")
			.post("/vets/pets/2/visits/6")
			.headers(headers_3)
			.formParam("date", "2020/05/01")
			.formParam("description", "")
			.formParam("medicalTests", "Radiography")
			.formParam("_medicalTests", "on")
			.formParam("petId", "2")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	val editVisitScn = scenario("editVisit").exec(
		Home.home,
		Login.login,
		Pets.pets,
		PetVisits.petVisits,
		EditVisit.editVisit
	)

	val errorEditVisitScn = scenario("errorEditVisit").exec(
		Home.home,
		Login.login,
		Pets.pets,
		PetVisits.petVisits,
		ErrorEditVisit.errorEditVisit
	)

	setUp(
		editVisitScn.inject(rampUsers(150) during (100 seconds)),
		errorEditVisitScn.inject(rampUsers(150) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}