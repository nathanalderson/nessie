package com.nathanalderson.nessie

import com.nathanalderson.nessie.assembly.{A65, S19}

import scala.util.Try

trait Memory extends Device {}

object Ram {
  def apply(range: Range): Ram =
    apply(range, Map[Addr, Data]())

  def apply(range: Range, s19: S19): Ram =
    Ram(range, s19.memoryContents)

  def apply(range: Range, a65contents: String): Ram =
    apply(range, A65.compile(a65contents))
}
case class Ram(range: Range,
               contents: Map[Addr, Data],
              )
  extends Memory
{
  override def read(addr: Addr): Option[Data] =
    if (range.contains(addr))
      Some(Try(contents(addr)).getOrElse(0))
    else
      None

  override def write(data: Data, addr: Addr): Device =
    if (range.contains(addr)) {
      Ram(range, contents.updated(addr, data))
    } else {
      this
    }

  override def toString: String =
    f"Ram<base: ${range.start}%x, contents: " +
    range.take(10).flatMap(contents.get(_)).map(v => f"$v%02x").mkString(" ") +
    ">"
}
