package uk.radzi.rbn.model

import Node.{falseString, trueString}
import scala.util.Random

/* defines a node with its current boolean value and the indices of nodes connected to it */
case class Node(
    value: Boolean,
    inputs: (Int, Int, Int)
) {
  override def toString: String = if (value) trueString else falseString
}

object Node {
  private val trueString = " "
  private val falseString = "0"

}
