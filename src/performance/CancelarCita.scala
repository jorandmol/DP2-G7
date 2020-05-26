package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CancelarCita extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

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
		.pause(5)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(12)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}

	object FindOwners {
		val findOwners = exec(http("FindOwners")
			.get("/owners/find")
			.headers(headers_0))
		.pause(12)
	}

	object ListOwners {
		val listOwners = exec(http("ListOwners")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(12)
	}

	object OwnerDetails {
		val ownerDetails = exec(http("OwnerDetails")
			.get("/owners/1")
			.headers(headers_0))
		.pause(21)
	}

	object DeleteAppointment {
		val deleteAppointment = exec(http("DeleteAppointment")
			.get("/owners/1/pets/1/appointments/7/delete")
			.headers(headers_0))
		.pause(15)
	}

	object ErrorDeleteAppointment {
		val errorDeleteAppointment = exec(http("ErrorDeleteAppointment")
			.get("/owners/1/pets/1/appointments/11/delete")
			.headers(headers_0))
		.pause(12)
	}


	val deleteAppointmentScn = scenario("deleteAppointment").exec(
		Home.home,
		Login.login,
		FindOwners.findOwners,
		ListOwners.listOwners,
		OwnerDetails.ownerDetails,
		DeleteAppointment.deleteAppointment
	)

	val errorDeleteAppointmentScn = scenario("errorDeleteAppointment").exec(
		Home.home,
		Login.login,
		FindOwners.findOwners,
		ListOwners.listOwners,
		OwnerDetails.ownerDetails,
		ErrorDeleteAppointment.errorDeleteAppointment
	)

	setUp(
		deleteAppointmentScn.inject(rampUsers(1550) during (100 seconds)),
		errorDeleteAppointmentScn.inject(rampUsers(1550) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}