package com.nathanalderson.nessie.cpu

import com.nathanalderson.nessie.{Bus, Ram}
import org.scalatest.{Matchers, WordSpec}

class TestCpu extends WordSpec with Matchers {
  "The CPU" should {
    val ram = Ram(0x0 until 0xff)
    val bus = Bus(List(ram))
    val cpu = Cpu(bus, 0.toByte, Registers())

    "run to the right tick" in {
      cpu.runTo(1L).tick should be (1L)
    }
  }
}
