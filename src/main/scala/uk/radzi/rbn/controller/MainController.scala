package uk.radzi.rbn.controller

import uk.radzi.rbn.model._
import uk.radzi.rbn.controller.MainController._
import org.scalajs.dom.document
import org.scalajs.dom.window

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class MainController(val seed: Long) {

  println(s"Generating an rbn from seed: $seed")

  private val (truthTable, initNetwork) = Network.generateSetup(seed)

  // TODO use lazy list instead?
  private val networks = initNetwork
    .iterations(truthTable)
    .take(InitialIterations)
    .toList

  private var lastIteration = networks.last

  private val networkTable = document.createElement("table")
  networkTable.id = "rbn-table"

  private val tableBody = document.createElement("tbody")
  tableBody.id = "rbn-tbody"
  tableBody.innerHTML = networksToHtml(networks)

  networkTable.appendChild(tableBody)
  document.body.appendChild(networkTable)

  window.onscroll = _ => {
    if (
      window.innerHeight + window.pageYOffset + OnScrollMargin >= document.body.offsetHeight
    ) {
      loadMore() foreach { tableBody.innerHTML += _ }
    }
  }

  // TODO implicit class for tohtml?
  private def nodeToCell(node: Node): String =
    s"""<td class="${if (node.value) "full" else "empty"}"></td>"""

  private def networkToRow(network: Network): String =
    s"<tr>${(network.nodes map nodeToCell).mkString}</tr>"

  private def networksToHtml(networks: Seq[Network]): String = {
    (networks map networkToRow).mkString
  }

  // Mutates!
  private def loadMore(): Future[String] = {

    val newIterations = Future(getNewIterations(lastIteration))

    newIterations foreach { newIterations =>
      lastIteration = newIterations.last
    }

    newIterations map networksToHtml
  }

  private def getNewIterations(lastIteration: Network): List[Network] =
    lastIteration
      .iterations(truthTable)
      .take(OnLoadIterations)
      .toList
}

object MainController {
  private val InitialIterations = 100
  private val OnLoadIterations = 50
  private val OnScrollMargin = 800
}
