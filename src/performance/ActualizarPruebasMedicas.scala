package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ActualizarPruebasMedicas extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
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
		.exec(http("request_3")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(23)
	}
	object MedicalTests {
		val medicalTests = exec(http("MedicalTests")
			.get("/medical-tests")
			.headers(headers_0))
		.pause(7)
	}
	object UpdateMedicalTest {
		val updateMedicalTest = exec(http("UpdateMedicalTest")
			.get("/medical-tests/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(2)
		.exec(http("request_6")
			.post("/medical-tests/1/edit")
			.headers(headers_3)
			.formParam("name", "Radiografia")
			.formParam("description", "It is used to diagnose or treat patients by recording images of the internal structure of the body to assess the presence or absence of disease, foreign objects, and structural damage or anomaly. During a radiographic procedure, an x-ray beam is passed through the body.")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}
	object UpdateMedicalTestError {
		val updateMedicalTestError = exec(http("UpdateMedicalTestError")
			.get("/medical-tests/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(1)
		.exec(http("request_8")
			.post("/medical-tests/1/edit")
			.headers(headers_3)
			.formParam("name", "")
			.formParam("description", "It is used to diagnose or treat patients by recording images of the internal structure of the body to assess the presence or absence of disease, foreign objects, and structural damage or anomaly. During a radiographic procedure, an x-ray beam is passed through the body.")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}

	val updateMedicalTestscn = scenario("UpdateMedicalTest").exec(Home.home,
																  Login.login,
																  MedicalTests.medicalTests,
																  UpdateMedicalTest.updateMedicalTest)
	val updateMedicalTestErrorscn = scenario("UpdateMedicalTestError").exec(Home.home,
																  			Login.login,
																 			MedicalTests.medicalTests,
																			UpdateMedicalTestError.updateMedicalTestError)

	setUp(
		updateMedicalTestscn.inject(rampUsers(500) during (100 seconds)),
		updateMedicalTestErrorscn.inject(rampUsers(500) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}