package uk.radzi.rbn

import uk.radzi.rbn.controller.MainController
import scala.util.Random

object Main {

  def main(args: Array[String]): Unit = new MainController(Random.nextLong())

}