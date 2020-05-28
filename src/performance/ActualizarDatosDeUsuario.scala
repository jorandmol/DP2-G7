package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ActualizarDatosDeUsuario extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
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
		.pause(5)
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

	object EditMyProfile {
		val editMyProfile = exec(http("EditMyProfileForm")
			.get("/owners/1/edit")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(46)
		.exec(http("EditMyProfile")
			.post("/owners/1/edit")
			.headers(headers_2)
			.formParam("firstName", "George")
			.formParam("lastName", "Franklin")
			.formParam("address", "Las Encinas, 44")
			.formParam("city", "Madison")
			.formParam("telephone", "608555102")
			.formParam("user.password", "0wn3333r_1")
			.formParam("_csrf", "${stoken}"))
		.pause(19)
	}

	object ErrorEditMyProfile {
		val errorEditMyProfile = exec(http("ErrorEditMyProfileForm")
			.get("/owners/1/edit")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(7)
		.exec(http("ErrorEditMyProfile")
			.post("/owners/1/edit")
			.headers(headers_2)
			.formParam("firstName", "George")
			.formParam("lastName", "Franklin")
			.formParam("address", "Las Encinas, 44")
			.formParam("city", "Madison")
			.formParam("telephone", "608555102")
			.formParam("user.password", "0wn3333r")
			.formParam("_csrf", "${stoken}"))
		.pause(17)
	}

	val editMyProfileScn = scenario("editMyProfile").exec(
		Home.home,
		Login.login,
		EditMyProfile.editMyProfile
	)

	val errorEditMyProfileScn = scenario("errorEditMyProfile").exec(
		Home.home,
		Login.login,
		ErrorEditMyProfile.errorEditMyProfile
	)

	setUp(
		editMyProfileScn.inject(rampUsers(3000) during (100 seconds)),
		errorEditMyProfileScn.inject(rampUsers(3000) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}