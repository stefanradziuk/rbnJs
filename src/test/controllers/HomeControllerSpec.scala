package controllers

import models.Network
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

/** Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  *
  * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
  */
class HomeControllerSpec
    extends PlaySpec
    with GuiceOneAppPerTest
    with Injecting {

  "HomeController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new HomeController(stubControllerComponents())
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
    }

    "render the index page from the application" in {
      val controller = inject[HomeController]
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
    }
  }

  "HomeController networkToHtml" should {

    "generate correct table" in {
      val seed = 5432
      val controller = new HomeController(stubControllerComponents())
      val networksHtml = controller.networksToHtml(Network.iterations(seed))

      networksHtml must include("<tr")
      networksHtml must include("<td")
      networksHtml must include("</tr>")
      networksHtml must include("</td>")
      networksHtml mustNot include("List")
    }
  }
}
