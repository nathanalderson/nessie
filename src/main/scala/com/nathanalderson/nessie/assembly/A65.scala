package com.nathanalderson.nessie.assembly

import java.io.{File, PrintWriter}
import java.nio.file.Files

import scala.io.Source
import scala.sys.process.Process

object A65 {
  def compile(a65contents: String): S19 = {
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
    val source = Source.fromFile(outFile)
    val s19 = S19(source.getLines)
    source.close()
    s19
  }
}
