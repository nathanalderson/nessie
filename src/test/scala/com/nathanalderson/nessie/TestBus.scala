package com.nathanalderson.nessie

import org.scalatest.{Matchers, WordSpec}

class TestBus extends WordSpec with Matchers {
  "A Bus" should {
    val ram1 = Ram(0 until 0x10, Map[Addr, Data]((0x0, 0x01)))
    val ram2 = Ram(0x10 until 0x20, Map[Addr, Data]((0x00, 0x02)))
    val bus = Bus(List(ram1, ram2), 0xa5)

    "read from the right device" in {
      bus.read(0x0)._1 should be (0x01)
      bus.read(0x10)._1 should be (0x02)
    }

    "return the last value read for an invalid address" in {
      val (firstVal, newBus) = bus.read(0x0)
      newBus.read(0x20)._1 should be (firstVal)
    }

    "write to the correct device" in {
      val newBus = bus.write(0x3, 0x0).write(0x4, 0x10)
      newBus.devices(0).read(0x0) should be (Some(0x3))
      newBus.devices(1).read(0x10) should be (Some(0x4))
    }

    "read the logical OR for overlapping devices" in {
      val ram1 = Ram(0x00 until 0x10).write(0x0f, 0x05)
      val ram2 = Ram(0x05 until 0x15).write(0xf0, 0x05)
      val bus = Bus(List(ram1, ram2))
      bus.read(0x05)._1 should be (0xff.toByte)
    }

    "read a range of data across devices" in {
      bus.read(0x0e until 0x12)._1 should be (List[Data](0, 0, 2, 0))
    }

    "write a range of data across devices" in {
      val newBus = bus.write(List[Data](1,2,3,4), 0x0e)
      newBus.read(0x0e until 0x12)._1 should be (List[Data](1,2,3,4))
    }
  }
}
