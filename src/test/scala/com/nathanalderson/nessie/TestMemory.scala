package com.nathanalderson.nessie

import org.scalatest.{Matchers, WordSpec}

class TestMemory extends WordSpec with Matchers {
  "RAM" should {
    val ram = Ram(0 until 0x10, Map())
    val ram2 = Ram(0x10 until 0x20, Map(), List(0x20 until 0x30))

    "return zero for valid, empty addresses" in {
      ram.read(0) should be (Some(0))
    }

    "return None for invalid addresses" in {
      ram.read(0x10) should be (None)
    }

    "return a written value" in {
      ram.write(0xff, 0x00).read(0x00) should be (Some(0xff.toByte))
      ram2.write(0xff, 0x10).read(0x10) should be (Some(0xff.toByte))
    }

    "properly mirror data" in {
      ram2.write(0xff, 0x10).read(0x20) should be (Some(0xff.toByte))
    }
  }
}
