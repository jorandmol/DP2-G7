package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AceptarRechazarSolicitudMascota extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,pt;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Language" -> "es-ES,es;q=0.9,pt;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")



	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
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

	object PetRequests {
		val petRequests = exec(http("PetRequests")
			.get("/requests")
			.headers(headers_0))
		.pause(10)
	}

	object ErrorRejectedPetRequest {
		val errorRejectedPetRequest = exec(http("ErrorRejectedPetRequestForm")
			.get("/owners/1/pet/3")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(3)
		.exec(http("ErrorRejectedPetRequest")
			.post("/owners/1/pet/3")
			.headers(headers_2)
			.formParam("status", "REJECTED")
			.formParam("justification", "")
			.formParam("_csrf", "${stoken}"))
		.pause(14)
	}


	object AcceptedPetRequest {
		val acceptedPetRequest = exec(http("AcceptedPetRequestForm")
			.get("/owners/6/pet/9")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(3)
		.exec(http("AcceptedPetRequest")
			.post("/owners/6/pet/9")
			.headers(headers_2)
			.formParam("status", "ACCEPTED")
			.formParam("justification", "")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	val errorRejectedPetRequestScn = scenario("errorRejectedPetRequest").exec(
		Home.home,
		Login.login,
		PetRequests.petRequests,
		ErrorRejectedPetRequest.errorRejectedPetRequest
	)

	val acceptedPetRequestSnc = scenario("acceptedPetRequestMascota").exec(
		Home.home,
		Login.login,
		PetRequests.petRequests,
		AcceptedPetRequest.acceptedPetRequest
	)

	setUp(
		errorRejectedPetRequestScn.inject(rampUsers(1) during (100 seconds)),
		acceptedPetRequestSnc.inject(rampUsers(1) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}