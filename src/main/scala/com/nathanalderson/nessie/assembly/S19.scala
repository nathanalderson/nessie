package com.nathanalderson.nessie.assembly

import com.nathanalderson.nessie.{Addr, Data}

object S19 {
  def apply(contents: Iterator[String]): S19 = new S19(contents.mkString("\n"))
  def apply(contents: String): S19 = new S19(contents)
}

class S19(contents: String) {
  private val hex = "[0-9a-fA-F]"
  private val re_S1Record = s"S1$hex{2}($hex{4})($hex+)$hex{2}".r
  private val re_S9Record = s"S903($hex{4})$hex{2}".r

  def memoryContents: Map[Addr, Data] = contents.linesIterator.flatMap {
    case re_S1Record(addrHex, contentsHex) =>
      val addr = Integer.parseInt(addrHex, 16)
      contentsHex.grouped(2).map(Integer.parseInt(_, 16)).zipWithIndex.map {
        case (byte, i) => ((addr+i).toShort, byte.toByte)
      }
    case _ => List()
  }.toMap

  def startAddress: Addr = contents.linesIterator.flatMap {
    case re_S9Record(addr) => Some(Integer.parseInt(addr, 16).toShort)
    case _ => None
  }.toList.last
}
