package com.nathanalderson.nessie

import com.nathanalderson.nessie.assembly.{A65, S19}
import com.nathanalderson.nessie.cpu.{Cpu, Registers}
import org.scalatest.{Matchers, WordSpec}

class TestSystem extends WordSpec with Matchers {
  def runInstructions(instructions: List[String]): System = {
    val program = TestHelpers.toProgram(instructions :+ "jmp *")
    val s19 = A65.compile(program)
    val system = System(
      Cpu(0L, Registers().loadStartAddr(s19)),
      Bus(List(Ram(0 until 0x400, s19))),
      0L
    )
    val run = system.runUntilHang().toList
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

    "halt" in {
      val ram = Ram(0 until 0xff, Map[Addr, Data]((0, 0xe8), (1, 0x03))) // inx; halt
      val system = System(Cpu(), Bus(List(ram)), 0L)
      val System(cpu, _, tick) = system.runUntilHalt().toList.last
      cpu.registers.pc should be (0x01)
      cpu.tick should be (2)
      tick should be (2)
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

    "JMP" in {
      val System(cpu, _, tick) = runInstructions(List("nop", "jmp *"))
      cpu.registers.pc should be (0x201)
    }
  }
}
