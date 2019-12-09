package com.nathanalderson.nessie

import java.io.{File, PrintWriter}
import java.nio.file.Files

import scala.io.Source
import scala.sys.process.Process

trait Memory extends Device {}

object Ram {
  def apply(range: Range): Ram =
    apply(range, Map[Addr, Data]())

  def apply(range: Range, s19contents: Source): Ram = {
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
    Ram(range, contents)
  }

  def apply(range: Range, a65contents: String): Ram = {
    val as65 = sys.env("AS65")
    val tmpDir = Files.createTempDirectory("nessie").toFile
    val sourceFile = new File(tmpDir, "in.a65")
    val outFile = new File(tmpDir, "out.s19")
    val listFile = new File(tmpDir, "out.list")
    val writer = new PrintWriter(sourceFile)
    writer.write(a65contents)
    writer.close()
    val rc = Process(s"$as65 -l -l$listFile -o$outFile -n -m -w -h0 -s $sourceFile", tmpDir).!
    assert(rc == 0)
    apply(range, Source.fromFile(outFile))
  }
}
case class Ram(range: Range,
               contents: Map[Addr, Data],
              )
  extends Memory
{
  override def read(addr: Addr): Option[Data] =
    if (range.contains(addr))
      Some(contents.getOrElse(addr, 0))
    else
      None

  override def write(data: Data, addr: Addr): Device =
    if (range.contains(addr)) {
      Ram(range, contents.updated(addr, data))
    } else {
      this
    }
}
