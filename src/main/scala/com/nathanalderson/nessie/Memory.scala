package com.nathanalderson.nessie

trait Memory extends Device {}

case class Ram(range: Range,
               contents: Map[Addr, Data] = Map(),
               mirroredAt: List[Range] = List(),
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
