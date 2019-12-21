package com.nathanalderson.nessie

import org.scalatest.{Matchers, WordSpec}
class TestSystem extends WordSpec with Matchers {

  def toProgram(instructions: List[String]): String = {
    ("start" :: instructions.map("    " + _)).mkString("\n") + "\n"
  }

  def runInstructions(instructions: List[String]): System = {
    val program = toProgram(instructions :+ "brk")
    val ram = Ram(0x0 until 0x10, program)
    val system = System(List(ram))
    val run = system.runUntilHalt().toList
    run.last
  }

  "The System" should {

    "run a 1-instruction ROM" in {
      val System(cpu, _, tick) = runInstructions(List("inx"))
      cpu.tick should be (2)
      tick should be (2)
      cpu.registers.x should be (1)
      cpu.registers.s.zero should be (false)
      cpu.registers.s.negative should be (false)
    }

    "LDX" in {
      val System(cpu, _, tick) = runInstructions(List("ldx #$ff"))
      cpu.tick should be (2)
      cpu.registers.x should be (0xff.toByte)
      cpu.registers.s.zero should be (false)
      cpu.registers.s.negative should be (true)
    }

    "TXS" in {
      val System(cpu, _, tick) = runInstructions(List("ldx #$ff", "txs"))
      cpu.registers.stk should be (0xff.toByte)
      tick should be (4)
      cpu.tick should be (4)
    }
  }
}
