package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AdoptarMascota extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*""", """https:\/\/dl5zpyw5k3jeb\.cloudfront\.net\/photos\/pets\/.*"""), WhiteList())
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

	object AdoptionsForm {
		val adoptionsForm = exec(http("AdoptionsForm")
			.get("/adoptions/owner/1")
			.headers(headers_0))
		.pause(23)
	}

	object AdoptionResults {
		val adoptionResults = exec(http("AdoptionResults")
			.get("/adoptions/owner/1/find?type=Dog&size=medium&gender=male")
			.headers(headers_0))
		.pause(14)
	}

	object AdoptionPet {
		val adoptionPet = exec(http("AdoptionPetProfile")
			.get("/adoptions/owner/1/pet/48061517")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(25)
		.exec(http("AdoptPet")
			.post("/adoptions/owner/1/pet")
			.headers(headers_3)
			.formParam("name", "Ralphie")
			.formParam("type", "Dog")
			.formParam("age", "Adult")
			.formParam("_csrf", "${stoken}"))
		.pause(13)
	}

	val adoptionPet = scenario("adoptionPet").exec(
		Home.home,
		Login.login,
		AdoptionsForm.adoptionsForm,
		AdoptionResults.adoptionResults,
		AdoptionPet.adoptionPet
	)

	setUp(
		adoptionPet.inject(rampUsers(90) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1500),
		global.successfulRequests.percent.gt(95)
	)
}