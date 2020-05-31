package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RegistrarVisita extends Simulation {

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

	object Appointments {
		val appointments = exec(http("Appointments")
			.get("/appointments/1")
			.headers(headers_0))
		.pause(12)
	}

	object NewVisit {
		val newVisit = exec(http("NewVisitForm")
			.get("/owners/7/pets/20/visits/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(14)
		.exec(http("NewVisit")
			.post("/owners/7/pets/20/visits/new")
			.headers(headers_2)
			.formParam("date", "2020/05/28")
			.formParam("description", "Annual checkup: Some tests made. Everithing looks correct.")
			.formParam("medicalTests", "Serologic test")
			.formParam("medicalTests", "Complete Blood Count (CBC)")
			.formParam("medicalTests", "Fluid Analysis")
			.formParam("_medicalTests", "on")
			.formParam("petId", "20")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	object ErrorNewVisit {
		val errorNewVisit = exec(http("ErrorNewVisitForm")
			.get("/owners/4/pets/15/visits/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(6)
		.exec(http("ErrorNewVisit")
			.post("/owners/4/pets/15/visits/new")
			.headers(headers_2)
			.formParam("date", "2020/05/28")
			.formParam("description", "")
			.formParam("_medicalTests", "on")
			.formParam("petId", "15")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}


	val insertVisitScn = scenario("insertVisit").exec(
		Home.home,
		Login.login,
		Appointments.appointments,
		NewVisit.newVisit
	)

	val errorInsertVisitScn = scenario("errorInsertVisit").exec(
		Home.home,
		Login.login,
		Appointments.appointments,
		ErrorNewVisit.errorNewVisit
	)

	setUp(
		insertVisitScn.inject(rampUsers(150) during (100 seconds)),
		errorInsertVisitScn.inject(rampUsers(150) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}