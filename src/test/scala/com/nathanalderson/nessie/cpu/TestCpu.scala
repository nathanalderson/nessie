package com.nathanalderson.nessie.cpu

import com.nathanalderson.nessie._
import org.scalatest.{Matchers, WordSpec}

class TestCpu extends WordSpec with Matchers {

  "The CPU" should {
    val ramRange = 0 until 0xff
    val ram = Ram(ramRange).write(ramRange.map(_=>0xea.toByte), 0)
    val bus = Bus(List(ram))
    val cpu = Cpu(0L, Registers())

    "run to the right tick" in {
      cpu.runTo(1L, bus)._1.tick should be (1L)
      cpu.runTo(10L, bus)._1.tick should be (10L)
    }
  }
}
