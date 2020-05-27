package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RegistrarPetType extends Simulation {

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
		.pause(6)
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
	
	object PetTypeList {
		val petTypeList = exec(http("PetTypeList")
			.get("/pet-type")
			.headers(headers_0))
		.pause(17)
	}
	
	object AddPetType {
		val addPetType = exec(http("PetTypeForm")
			.get("/pet-type/new?")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(25)
		.exec(http("AddPetType")
			.post("/pet-type/new")
			.headers(headers_3)
			.formParam("name", "cacatua")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
		
	}
	
	object DontAddPetType {
		val dontAddPetType = exec(http("PetTypeForm")
			.get("/pet-type/new?")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(25)
		.exec(http("DontAddPetType")
			.post("/pet-type/new")
			.headers(headers_3)
			.formParam("name", "dog")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
		
	}
	

    val addPetTypeScn = scenario("RefistrarPetType").exec(Home.home,
																	Login.login,
																	PetTypeList.petTypeList,
																	AddPetType.addPetType)
																	
	
	val dontAddPetTypeScn = scenario("NoRegistrarPetType").exec(Home.home,
																	Login.login,
																	PetTypeList.petTypeList,
																	DontAddPetType.dontAddPetType)															 

	setUp(
		addPetTypeScn.inject(rampUsers(5000) during (100 seconds)),
		dontAddPetTypeScn.inject(rampUsers(5000) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}