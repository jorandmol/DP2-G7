package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RegistrarBanner extends Simulation {

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
		).pause(3)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}

	object Banners{
		val banners = exec(http("Banners")
			.get("/banners")
			.headers(headers_0))
		.pause(15)
	}

	object NewBanner{
		val newBanner = exec(http("NewBannerForm")
			.get("/banners/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(36)
		.exec(http("NewBanner")
			.post("/banners/new")
			.headers(headers_3)
			.formParam("picture", "http://www.adoptame.com/photos/imgfullhd.png")
			.formParam("slogan", "Soy el mejor de los amigos")
			.formParam("targetUrl", "http://www.adopta.com")
			.formParam("organizationName", "Adopta")
			.formParam("endColabDate", "2020/09/15")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
	}

	object ErrorNewBanner{
		val errorNewBanner = exec(http("ErrorNewBanner")
			.get("/banners/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(36)
		.exec(http("NewBanner")
			.post("/banners/new")
			.headers(headers_3)
			.formParam("picture", "")
			.formParam("slogan", "Soy el mejor de los amigos")
			.formParam("targetUrl", "http://www.adopta.com")
			.formParam("organizationName", "Adopta")
			.formParam("endColabDate", "2020/09/15")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
	}

	val insertNewBannerScn = scenario("insertNewBanner").exec(
		Home.home,
		Login.login,
		Banners.banners,
		NewBanner.newBanner
	)

	val errorInsertNewBannerScn = scenario("errorInsertNewBanner").exec(
		Home.home,
		Login.login,
		Banners.banners,
		ErrorNewBanner.errorNewBanner
	)

	setUp(
		insertNewBannerScn.inject(rampUsers(300) during (100 seconds)),
		errorInsertNewBannerScn.inject(rampUsers(300) during (100 seconds)))
	.protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}