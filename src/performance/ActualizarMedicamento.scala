package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ActualizarMedicamento extends Simulation {

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
	
	object MedicinesList {
		val medicinesList =exec(http("MedicinesList")
			.get("/medicines")
			.headers(headers_0))
		.pause(13)
	}
	object MedicineUpdated {
		val medicineUpdated =exec(http("MedicineForm")
			.get("/medicines/2/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(11)
	    .exec(http("MedicineUpdated")
			.post("/medicines/2/edit")
			.headers(headers_3)
			.formParam("name", "Medicine name changed")
			.formParam("code", "PCA-010")
			.formParam("expirationDate", "2024/04/19")
			.formParam("description", "Medicine description changed")
			.formParam("_csrf", "${stoken}"))
	}
	
		object MedicineNotUpdated {
		val medicineNotUpdated =exec(http("MedicineForm")
			.get("/medicines/6/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(11)
	    .exec(http("MedicineNotUpdated")
			.post("/medicines/6/edit")
			.headers(headers_3)
			.formParam("name", "Atenolol")
			.formParam("code", "MET-529")
			.formParam("expirationDate", "2024/05/09")
			.formParam("description", "Heart medication used to control the circulatory system")
			.formParam("_csrf", "${stoken}"))
	}

	val updateMedicineScn = scenario("ActualizarMedicamento").exec(Home.home,
															 Login.login,
															 MedicinesList.medicinesList,
															 MedicineUpdated.medicineUpdated)
															 
	val dontUpdateMedicineScn = scenario("NoActualizarMedicamento").exec(Home.home,
																	Login.login,
																	MedicinesList.medicinesList,
																	MedicineNotUpdated.medicineNotUpdated)															 

	setUp(
		updateMedicineScn.inject(rampUsers(7000) during (100 seconds)),
		dontUpdateMedicineScn.inject(rampUsers(7000) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}