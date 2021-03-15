package uk.radzi.rbn.controller

import uk.radzi.rbn.model._
import uk.radzi.rbn.controller.MainController._
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.window
import scala.util.Random
import org.scalajs.dom.raw.UIEvent

case class MainController(val seed: Long) {

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

  private var reachedEnd = false

  window.onscroll = (e: UIEvent) => {
    if (
      !reachedEnd && (window.innerHeight + window.pageYOffset + OnScrollMargin) >= document.body.offsetHeight
    ) {
      tableBody.innerHTML += loadMore()
    }
  }

  private def nodeToCell(node: Node): String =
    s"""<td class="${if (node.value) "full" else "empty"}"></td>"""

  private def networkToRow(network: Network): String =
    s"<tr>${(network.nodes map nodeToCell).mkString}</tr>"

  private def networksToHtml(networks: Seq[Network]): String = {
    (networks map networkToRow).mkString
  }

  // Mutates!
  private def loadMore(): String = {
    val t0 = window.performance.now()

    val newIterations = lastIteration
      .iterations(truthTable)
      .take(OnLoadIterations)
      .toList

    lastIteration = newIterations.last

    val t1 = window.performance.now()

    println(t1 - t0)

    networksToHtml(newIterations)
  }
}

object MainController {
  private val InitialIterations = 100
  private val OnLoadIterations = 50
  private val OnScrollMargin = 50
}
