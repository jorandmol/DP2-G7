package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class SolicitarEstancia extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png""", """.*.jpg"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

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
		"Accept-Encoding" -> "gzip",
		"User-Agent" -> "Go-http-client/1.1")

	val headers_4 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "es-ES,es;q=0.9,pt;q=0.8",
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
	object FindOwners {
		val findOwners = exec(http("FindOWners")
			.get("/owners/find")
			.headers(headers_0))
		.pause(5)
	}
	object ListOwners {
		val listOwners = exec(http("ListOwners")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(11)
	}
	object GetOWner {
		val getOwner2 = exec(http("GetOwner")
			.get("/owners/2")
			.headers(headers_0))
		.pause(16)
	}
	object PetStays {
		val getPet14Stays = exec(http("GetPetStays")
			.get("/owners/2/pets/14/stays")
			.headers(headers_0))
		.pause(8)
	}
	object NewStaySuccess {
		val newStaySuccess = exec(http("NewStayForm")
			.get("/owners/2/pets/14/stays/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(19)
		.exec(http("CreateStay")
			.post("/owners/2/pets/14/stays/new")
			.headers(headers_4)
			.formParam("registerDate", "2020/10/03")
			.formParam("releaseDate", "2020/10/06")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
	}

	object OtherNewStaySuccess {
		val newStaySuccess = exec(http("NewStayForm")
			.get("/owners/2/pets/14/stays/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(19)
		.exec(http("CreateOtherStay")
			.post("/owners/2/pets/14/stays/new")
			.headers(headers_4)
			.formParam("registerDate", "2020/09/03")
			.formParam("releaseDate", "2020/09/06")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
	}

	object NewStayError {
		val newStayError = exec(http("NewStayForm")
			.get("/owners/2/pets/14/stays/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(19)
		.exec(http("CreateStayError")
			.post("/owners/2/pets/14/stays/new")
			.headers(headers_4)
			.formParam("registerDate", "2020/09/04")
			.formParam("releaseDate", "2020/09/05")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}
	
	val newStayCreatedScn = scenario("SolicitudEstanciaCreada").exec(Home.home,
																	Login.login,
																	FindOwners.findOwners,
																	GetOWner.getOwner2,
																	PetStays.getPet14Stays,
																	NewStaySuccess.newStaySuccess)

	val newStayNotCreatedScn = scenario("SolicitudEstanciaNoCreada").exec(Home.home,
																	Login.login,
																	FindOwners.findOwners,
																	GetOWner.getOwner2,
																	PetStays.getPet14Stays,
																	OtherNewStaySuccess.newStaySuccess,
																	NewStayError.newStayError)


	setUp(
		newStayCreatedScn.inject(rampUsers(2800) during (100 seconds)),
		newStayNotCreatedScn.inject(rampUsers(2800) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}