package uk.radzi.rbn

import uk.radzi.rbn.controller.MainController
import scala.util.Random
import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom.document

object Main {

  def main(args: Array[String]): Unit = new MainController(Random.nextLong())

  @JSExportTopLevel("openInfo")
  def openInfo(): Unit =
    document.getElementById("infoPopup").classList.toggle("visiblePopup")

}
