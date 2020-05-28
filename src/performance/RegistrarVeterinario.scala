package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RegistrarVeterinario extends Simulation {

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

	object Veterinarians {
		val veterinarians = exec(http("Veterinarians")
			.get("/vets")
			.headers(headers_0))
		.pause(12)
	}

	object NewVet {
		val newVet = exec(http("NewVetForm")
			.get("/vets/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(12)
		.exec(http("NewVet")
			.post("/vets/new")
			.headers(headers_3)
			.formParam("firstName", "Luna")
			.formParam("lastName", "Rodriguez")
			.formParam("address", "Los Rosales, 10")
			.formParam("city", "Sevilla")
			.formParam("telephone", "123456789")
			.formParam("specialties", "pathology")
			.formParam("_specialties", "1")
			.formParam("user.username", "vetluna")
			.formParam("user.password", "v3terinarian_luna")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	object ErrorNewVet {
		val errorNewVet = exec(http("ErrorNewVetForm")
			.get("/vets/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(12)
		.exec(http("ErrorNewVet")
			.post("/vets/new")
			.headers(headers_3)
			.formParam("firstName", "Luna")
			.formParam("lastName", "Rodriguez")
			.formParam("address", "Los Rosales, 10")
			.formParam("city", "Sevilla")
			.formParam("telephone", "123456789")
			.formParam("_specialties", "1")
			.formParam("user.username", "vetluna")
			.formParam("user.password", "vetluna")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	val insertVetScn = scenario("insertVet").exec(
		Home.home,
		Login.login,
		Veterinarians.veterinarians,
		NewVet.newVet
	)
		
	val errorInsertVetScn = scenario("errorInsertVet").exec(
		Home.home,
		Login.login,
		Veterinarians.veterinarians,
		ErrorNewVet.errorNewVet
	)	
	
	setUp(
		insertVetScn.inject(rampUsers(5000) during (100 seconds)),
		errorInsertVetScn.inject(rampUsers(5000) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}