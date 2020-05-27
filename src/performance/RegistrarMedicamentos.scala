package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RegistrarMedicamentos extends Simulation {

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
	object ListMedicines{
	
	 val listMedicines = exec(http("listMedicines")
			.get("/medicines")
			.headers(headers_0))
		.pause(14)
	}
	
	object AddMedicines{
	
	val addMedicines = exec(http("MedicineForm")
			.get("/medicines/new")
			.headers(headers_0)
			check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(61)
		.exec(http("AddMedicines")
			.post("/medicines/new")
			.headers(headers_3)
			.formParam("name", "Medicine1")
			.formParam("code", "FFF-202")
			.formParam("expirationDate", "2021/02/25")
			.formParam("description", "Medicine standar description")
			.formParam("_csrf", "${stoken}"))
	
	
	
	}
	
	object DontAddMedicines{
	
	val dontAddMedicines = exec(http("MedicineForm")
			.get("/medicines/new")
			.headers(headers_0)
			check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(61)
		.exec(http("DontAddMedicines")
			.post("/medicines/new")
			.headers(headers_3)
			.formParam("name", "MedicineBadCode")
			.formParam("code", "MET-529")
			.formParam("expirationDate", "2021/02/25")
			.formParam("description", "Medicine standar description")
			.formParam("_csrf", "${stoken}"))
	
	
	
	}
	val addMedicineScn = scenario("RegistrarMedicamentos").exec(Home.home,
																	Login.login,
																	ListMedicines.listMedicines,
																	AddMedicines.addMedicines)
	
	val dontAddMedicineScn = scenario("NoRegistrarMedicamentos").exec(Home.home,
																	Login.login,
																	ListMedicines.listMedicines,
																	DontAddMedicines.dontAddMedicines)															 

	setUp(
		addMedicineScn.inject(rampUsers(4200) during (100 seconds)),
		dontAddMedicineScn.inject(rampUsers(4200) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}