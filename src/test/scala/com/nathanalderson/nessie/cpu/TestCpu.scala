package com.nathanalderson.nessie.cpu

import com.nathanalderson.nessie._
import org.scalatest.{Matchers, WordSpec}

class TestCpu extends WordSpec with Matchers {

  "The CPU" should {
    val cpu = Cpu(0L, Registers())

    def busWithRamContents(contents: Map[Addr, Data]): Bus = {
      val ram = Ram(0 until 0xff, contents)
      Bus(List(ram))
    }

    "run to the right tick" in {
      val nopBus = busWithRamContents(Map[Addr, Data]().withDefaultValue(0xea.toByte))
      cpu.runTo(1L, nopBus)._1.tick should be (1L)
      cpu.runTo(10L, nopBus)._1.tick should be (10L)
    }

    "jmp (absolute)" in {
      val program = TestHelpers.toProgram(List("jmp $000a"))
      val bus = TestHelpers.busWithProgram(program)
      cpu.runTo(1L, bus)._1.registers.pc should be (0x0a.toByte)
    }
  }
}
