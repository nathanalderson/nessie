package com.nathanalderson.nessie

import com.nathanalderson.nessie.cpu.{Cpu, Registers}
import org.scalatest.{Matchers, WordSpec}

class TestSystem extends WordSpec with Matchers {
  "The System" should {

    "run a really simple ROM" in {
      val program = "start inx\n"
      val ram = Ram(0x0 until 0x10, program)
      val system = System(List(ram))
      system.step() match {
        case System(cpu, _, tick) =>
          cpu.tick should be (2)
          cpu.registers.x should be (1)
          cpu.registers.s.zero should be (false)
          cpu.registers.s.negative should be (false)
          tick should be (1L)
      }
    }
  }
}
