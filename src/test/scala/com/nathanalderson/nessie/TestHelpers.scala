package com.nathanalderson.nessie

object TestHelpers {
  // Set up basic layout for a tiny program
  // we set up an unitialized data segment (bss) at 0, data segment at 0x100, code segment at 0x200
  // note: weirdly, "end" is how you specify the entrypoint
  private val preamble =
    """    end start
      |    bss
      |    org $0
      |    data
      |    org $100
      |label1   ds  10
      |    code
      |    org $200
      |start
      |""".stripMargin

  def toProgram(instructions: List[String]): String =
    preamble + toTrivialProgram(instructions)

  def toTrivialProgram(instructions: List[String]): String =
    instructions.map("    " + _).mkString("\n") + "\n"

  def busWithProgram(prog: String): Bus =
    Bus(List(Ram(0 until 0x400, prog)))
}
