package com.nathanalderson.nessie

import org.scalatest.{Matchers, WordSpec}

class TestBus extends WordSpec with Matchers {
  "A Bus" should {
    val ram1 = RAM(0 until 0x10, Map((0x0, 0x01)))
    val ram2 = RAM(0x10 until 0x20, Map((0x10, 0x02)))
    val bus = Bus(List(ram1, ram2), 0xa5)

    "read from the right device" in {
      bus.read(0x0)._1 should be (0x01)
      bus.read(0x10)._1 should be (0x02)
    }

    "return the last value read for an invalid address" in {
      val (firstVal, newBus) = bus.read(0x0)
      newBus.read(0x20)._1 should be (firstVal)
    }

    "write to the right device" in {
      val newBus = bus.write(0x3, 0x0)
      newBus.devices.head.read(0x0) should be (Some(0x3))
    }
  }
}
