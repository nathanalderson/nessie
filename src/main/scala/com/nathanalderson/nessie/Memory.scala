package com.nathanalderson.nessie

import scala.io.Source

trait Memory extends Device {}

object Ram {
  def apply(range: Range): Ram =
    apply(range, Map[Addr, Data](), List())

  def apply(range: Range, contents: Map[Addr, Data]): Ram =
    apply(range, contents, List())

  def apply(range: Range, s19contents: Source): Ram =
    apply(range, s19contents, List())

  def apply(range: Range, s19contents: Source, mirroredAt: List[Range]): Ram = {
    val hex = "[0-9a-fA-F]"
    val re_S1Record = s"S1$hex{2}($hex{4})($hex+)$hex{2}".r
    val contents: Map[Addr, Data] = s19contents.getLines().flatMap {
      case re_S1Record(addrHex, contentsHex) =>
        val addr = Integer.parseInt(addrHex, 16)
        contentsHex.grouped(2).map(Integer.parseInt(_, 16)).zipWithIndex.map {
          case (byte, i) => ((addr+i).toShort, byte.toByte)
        }
      case _ => List()
    }.toMap
    Ram(range, contents, mirroredAt)
  }
}
case class Ram(range: Range,
               contents: Map[Addr, Data],
               mirroredAt: List[Range],
              )
  extends Memory
{
  val ranges = range :: mirroredAt
  override def read(addr: Addr): Option[Data] =
    ranges.find(_.contains(addr)).map { range =>
      val offset = addr - range.start
      contents.getOrElse(offset, 0)
    }

  override def write(data: Data, addr: Addr): Device = {
    ranges.find(_.contains(addr)) match {
      case Some(range) =>
        val offset = addr - range.start
        val new_contents = contents.updated (offset, data)
        Ram(ranges.head, new_contents, ranges.tail)
      case None => this
    }
  }

  override def read(addrs: Range): IndexedSeq[Data] = addrs.flatMap(read(_))

  override def write(data: IterableOnce[Data], startAddr: Addr): Device =
    data.iterator.foldLeft((this: Device, startAddr)) {
      case ((device, addr), d) => (device.write(d, addr), addr+1)
    }._1

}
