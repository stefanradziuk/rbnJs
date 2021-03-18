package uk.radzi.rbn.model

import scala.util.Random

case class Network private (
    size: Int,
    nodes: Seq[Node]
) {
  def nextNode(node: Node, truthTable: TruthTable): Node = {
    val (x, y, z) = node.inputs
    val nextVal =
      truthTable.getValueFor(nodes(x).value, nodes(y).value, nodes(z).value)
    Node(nextVal, node.inputs)
  }

  def nextIteration(truthTable: TruthTable): Network =
    new Network(size, nodes map { node => nextNode(node, truthTable) })

  def configToString: String =
    s"""Node connections:
       |$nodesToString""".stripMargin

  def nodesToString: String = ((nodes map {
    _.inputs
  }).zipWithIndex map { case (t, i) =>
    String.format("%2d | %2d, %2d, %2d\n", i, t._1, t._2, t._3)
  }).mkString

  override def toString: String =
    nodes.foldLeft(new StringBuilder)(_ append _).append('\n').toString

  def iterations(truthTable: TruthTable): LazyList[Network] =
    this #:: iterations(truthTable) map (_.nextIteration(truthTable))
}

object Network {
  val defaultSize = 20

  def apply(size: Int = defaultSize, seed: Long): Network = {
    Random.setSeed(seed)

    val nodeConnections: Seq[(Int, Int, Int)] = Seq.fill(size) {
      (Random.nextInt(size), Random.nextInt(size), Random.nextInt(size))
    }

    val nodes: Seq[Node] =
      nodeConnections map (Node(Random.nextBoolean(), _))

    new Network(size, nodes)
  }

  // TODO move this into a separate class / obj?
  def generateSetup(seed: Long): (TruthTable, Network) = {
    (TruthTable(), Network(size = defaultSize, seed = seed))
  }

}
