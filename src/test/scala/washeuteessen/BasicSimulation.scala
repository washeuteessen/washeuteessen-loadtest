package washeuteessen

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://api-test.washeuteessen.de")
    .acceptHeader("application/json")
    .disableFollowRedirect

  val scn = scenario("Search") // A scenario is a chain of requests and pauses
    .exec(http("search first page")
    .get("/search?s=Spargel&offset=0&limit=15"))
    .pause(7)
    .exec(http("search second page")
      .get("/search?s=Spargel&offset=15&limit=15"))
    .pause(2)
    .exec(http("search third page")
      .get("/search?s=Spargel&offset=30&limit=15"))
    .pause(3)
    .exec(http("open recipe")
      .get("/recipes/2/external")
      .check(status.is(301)))
    .pause(2)

  setUp(scn.inject(atOnceUsers(100)).protocols(httpProtocol))
}
