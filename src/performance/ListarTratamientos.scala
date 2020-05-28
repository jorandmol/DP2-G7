package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ListarTratamientos extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
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
		).pause(8)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}
	object OwnerPetsView {
		val ownerPetsView = exec(http("OwnerPetsView")
			.get("/owners/find")
			.headers(headers_0))
		.pause(4)
		.exec(http("request_5")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(8)
		.exec(http("request_6")
			.get("/owners/1")
			.headers(headers_0))
		.pause(6)
		.exec(http("request_7")
			.get("/owner/1/pets")
			.headers(headers_0))
		.pause(24)
	}
	object ListTreatmentsOwner {
		val listTreatmentsOwner = exec(http("ListTreatmentsOwner")
			.get("/owners/1/pets/1/treatments")
			.headers(headers_0))
		.pause(25)
	}
	object VetPetsView {
		val vetPetsView = exec(http("VetPetsView")
			.get("/vets")
			.headers(headers_0))
		.pause(20)
		.exec(http("request_10")
			.get("/vets/pets")
			.headers(headers_0))
		.pause(33)
	}
	object ListTreatmentsVet {
		val listTreatmentsVet = exec(http("ListTreatmentsVet")
			.get("/vets/pets/1/treatments")
			.headers(headers_0))
		.pause(11)
	}
	val ownerListTreatmentsScn = scenario("ListTreatmentsOwner").exec(Home.home,
																Login.login,
																OwnerPetsView.ownerPetsView,
																ListTreatmentsOwner.listTreatmentsOwner)

	val vetListTreatmentsScn = scenario("ListTreatmentsVet").exec(Home.home,
															   	Login.login,
															    VetPetsView.vetPetsView,
															    ListTreatmentsVet.listTreatmentsVet)

	setUp(
		ownerListTreatmentsScn.inject(rampUsers(5000) during (100 seconds)),
		vetListTreatmentsScn.inject(rampUsers(5000) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}