package com.nathanalderson.nessie

trait Memory extends Device {}

case class RAM(range: Range, contents: Map[Addr, Data] = Map()) extends Memory {
  override def read(addr: Addr): Option[Data] =
  if (range.contains(addr))
    Some(contents.getOrElse(addr, 0))
  else
    None

  override def write(data: Data, addr: Addr): Device =
    if (range.contains(addr))
      RAM(range, contents.updated(addr, data))
    else
      this
}
