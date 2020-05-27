package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CrearTratamiento extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
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
		.pause(1)
	}
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(8)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}
	object PetsList {
		val petsList = exec(http("PetsList")
			.get("/vets/pets")
			.headers(headers_0))
		.pause(1)
	}

	object PetTreatments {
		val petTreatments = exec(http("PetTreatments")
			.get("/vets/pets/1/treatments")
			.headers(headers_0))
		.pause(10)
	}

	object AddTreatment {
		val addTreatment = exec(http("AddTreatment")
			.get("/vets/pets/1/treatments/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(12)
		.exec(http("request_4")
			.post("/vets/pets/1/treatments/new")
			.headers(headers_3)
			.formParam("name", "Tratamiento10")
			.formParam("description", "Esto es un tratamiento")
			.formParam("timeLimit", "2020/07/31")
			.formParam("medicines", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}
	object AddTreatmentNoMed {
		val addTreatmentNoMed = exec(http("AddTreatmentNoMed")
			.get("/vets/pets/1/treatments/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(14)
		.exec(http("request_5")
			.post("/vets/pets/1/treatments/new")
			.headers(headers_3)
			.formParam("name", "Tratamiento11")
			.formParam("description", "Esto es un tratamiento sin medicina")
			.formParam("timeLimit", "2020/07/31")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}

	val addTreatmentscn = scenario("AddTreatment").exec(Home.home,
														Login.login,
														PetsList.petsList,
														PetTreatments.petTreatments,
														AddTreatment.addTreatment)
	val addTreatmentNoMedscn = scenario("AddTreatmentNoMed").exec(Home.home,
																  Login.login,
																  PetsList.petsList,
																  PetTreatments.petTreatments,
																  AddTreatmentNoMed.addTreatmentNoMed)

	setUp(
		addTreatmentscn.inject(rampUsers(100) during (100 seconds)),
		addTreatmentNoMedscn.inject(rampUsers(100) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}