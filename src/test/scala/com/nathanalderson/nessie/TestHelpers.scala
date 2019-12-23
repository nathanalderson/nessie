package com.nathanalderson.nessie

object TestHelpers {
  def toProgram(instructions: List[String]): String =
    ("start" :: instructions.map("    " + _)).mkString("\n") + "\n"

  def busWithProgram(prog: String): Bus =
    Bus(List(Ram(0 until 0xff, prog)))
}
