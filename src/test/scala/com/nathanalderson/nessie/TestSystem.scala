package com.nathanalderson.nessie

import com.nathanalderson.nessie.cpu.{Cpu, Registers}
import org.scalatest.{Matchers, WordSpec}

class TestSystem extends WordSpec with Matchers {

  def runInstructions(instructions: List[String], ticks: Option[Tick] = None): System = {
    val program = ("start" :: instructions.map("    " + _)).mkString("\n") + "\n"
    val ram = Ram(0x0 until 0x10, program)
    val system = System(List(ram))
    (0L until ticks).foldLeft(system)((s, _) => s.step())
  }

  "The System" should {

    "run a 1-instruction ROM" in {
      val System(cpu, _, tick) = runInstructions(List("inx"))
      cpu.tick should be (2)
      cpu.registers.x should be (1)
      cpu.registers.s.zero should be (false)
      cpu.registers.s.negative should be (false)
      tick should be (1L)
    }

    "LDX" in {
      val System(cpu, _, tick) = runInstructions(List("ldx #$ff"))
      cpu.tick should be (2)
      cpu.registers.x should be (0xff.toByte)
      cpu.registers.s.zero should be (false)
      cpu.registers.s.negative should be (true)
    }

    "TXS" in {
      val System(cpu, _, tick) = runInstructions(List("ldx #$ff", "txs"), 2L)
      cpu.tick should be (4)
      cpu.registers.stk should be (0xff.toByte)
    }
  }
}
