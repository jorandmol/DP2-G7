package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EliminarHistorialTratamiento extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

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
		.pause(2)
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
		.pause(2)
	}
	object ShowTreatment {
		val showTreatment = exec(http("ShowTreatment")
			.get("/vets/pets/1/treatments")
			.headers(headers_0))
		.pause(2)
		.exec(http("request_6")
			.get("/vets/pets/1/treatments/1")
			.headers(headers_0))
		.pause(15)
	}
	object DeleteRegister {
		val deleteRegister = exec(http("DeleteRegister")
			.get("/vets/pets/1/treatments/1/history/2/delete")
			.headers(headers_0))
		.pause(20)
	}
	val deleteHistoryRegisterScn = scenario("DeleteHistoryRegister").exec(Home.home,
																 		 Login.login,
																 		 PetsList.petsList,
																		 ShowTreatment.showTreatment,
																		 DeleteRegister.deleteRegister)

	setUp(
		deleteHistoryRegisterScn.inject(rampUsers(200) during (100 seconds))
	.protocols(httpProtocol))
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}