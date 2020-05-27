package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AceptarRechazarEstancia extends Simulation {

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
	
	var numStays = 4;

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
				.headers(headers_4)
				.formParam("username", "admin1")
				.formParam("password", "4dm1n")
				.formParam("_csrf", "${stoken}"))
			.pause(6)
	}
	object ListStays {
		val listStays = exec(http("ListStays")
			.get("/admin/stays")
			.headers(headers_0))
		.pause(5)
	}
	object AcceptStay {
		val acceptStay2 = exec(http("ChangeStatusForm")
			.get("/admin/stays/2")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(11)
		.exec(http("StayAccepted")
			.post("/admin/stays/2")
			.headers(headers_4)
			.formParam("registerDate", "2020/10/01")
			.formParam("releaseDate", "2020/10/05")
			.formParam("status", "ACCEPTED")
			.formParam("_csrf", "${stoken}"))
	}
	object RejectStay {
		val rejectStay3 = exec(http("ChangeStatusForm")
			.get("/admin/stays/3")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(11)
		.exec(http("StayRejected")
			.post("/admin/stays/3")
			.headers(headers_4)
			.formParam("registerDate", "2020/11/01")
			.formParam("releaseDate", "2020/11/05")
			.formParam("status", "ACCEPTED")
			.formParam("_csrf", "${stoken}"))
	}
	
	val acceptStayScn = scenario("AceptarSolicitudEstancia").exec(Home.home,
																	Login.login,
																	ListStays.listStays,
																	AcceptStay.acceptStay2)

	val rejectStayScn = scenario("RechazarSolicitudEstancia").exec(Home.home,
																	Login.login,
																	ListStays.listStays,
																	RejectStay.rejectStay3)


	setUp(
		acceptStayScn.inject(rampUsers(3500) during (100 seconds)),
		rejectStayScn.inject(rampUsers(3500) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}