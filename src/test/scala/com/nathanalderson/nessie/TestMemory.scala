package com.nathanalderson.nessie

import org.scalatest.{Matchers, WordSpec}

class TestMemory extends WordSpec with Matchers {
  "RAM" should {
    val ram = RAM(0 until 0x10, Map())

    "return zero for valid, empty addresses" in {
      ram.read(0) should be (Some(0))
    }

    "return None for invalid addresses" in {
      ram.read(0x10) should be (None)
    }

    "return a written value" in {
      ram.write(0xff, 0x00).read(0x00) should be (Some(0xff.toByte))
    }

    "ignore writes to invalid addresses" in {
      val newRAM = ram.write(0xff, 0x10)
      ram.range.foreach(newRAM.read(_) should be(Some(0)))
    }
  }
}
