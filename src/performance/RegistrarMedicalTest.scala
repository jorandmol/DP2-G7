package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RegistrarMedicalTest extends Simulation {

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
		
		
	object MedicalTestList {
		val medicalTestList = exec(http("MedicalTestList")
			.get("/medical-tests")
			.headers(headers_0))
		.pause(17)
	}
	
		object AddMedicalTest {
		val addMedicalTest = exec(http("MedicalTestForm")
			.get("/medical-tests/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(11)
		 .exec(http("AddMedicalTest")
			.post("/medical-tests/new")
			.headers(headers_3)
			.formParam("name", "Medical Test name")
			.formParam("description", "Medical Name description")
			.formParam("_csrf", "${stoken}"))
	}
		
			object DontAddMedicalTest {
		val dontAddMedicalTest = exec(http("MedicalTestForm")
			.get("/medical-tests/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(11)
		 .exec(http("DontAddMedicalTest")
			.post("/medical-tests/new")
			.headers(headers_3)
			.formParam("name", "")
			.formParam("description", "")
			.formParam("_csrf", "${stoken}"))
	}
		
		
		
	val addMedicalTestScn = scenario("RegistrarMedicalTest").exec(Home.home,
																	Login.login,
																	MedicalTestList.medicalTestList,
																	AddMedicalTest.addMedicalTest)

	val dontAddMedicalTestScn = scenario("NoRegistrarMedicalTest").exec(Home.home,
																	Login.login,
																	MedicalTestList.medicalTestList,
																	DontAddMedicalTest.dontAddMedicalTest)															 

	setUp(
		addMedicalTestScn.inject(rampUsers(500) during (100 seconds)),
		dontAddMedicalTestScn.inject(rampUsers(500) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}