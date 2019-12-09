package com.nathanalderson.nessie

import org.scalatest.{Matchers, WordSpec}

import scala.io.Source

class TestRam extends WordSpec with Matchers {
  "RAM" should {
    val ram = Ram(0 until 0x10, Map[Addr, Data]())

    "return zero for valid, empty addresses" in {
      ram.read(0) should be (Some(0))
    }

    "return None for invalid addresses" in {
      ram.read(0x10) should be (None)
    }

    "return a written value" in {
      ram.write(0xff, 0x00).read(0x00) should be (Some(0xff.toByte))
    }

    "read a range of data" in {
      ram.read(0x0 until 0x03) should be (List(0, 0, 0))
      ram.read(0x0d until 0x10) should be (List(0, 0, 0))
      ram.read(0x0d until 0x11) should be (List(0, 0, 0))
    }

    "write a range of data" in {
      ram.write(List[Data](1, 2, 3), 0).read(0 until 3) should be(List[Data](1, 2, 3))
    }

    "create a Ram from s19 records" in {
      val s19 =
        """\
          |S1070000cafebabe00
          |S1011000ff00
          |S503000200
          |S903a5a500
          |""".stripMargin
      val ram = Ram(0 until 0x2000, Source.fromString(s19))
      ram.read(0x00 until 0x04) should be (List[Data](0xca, 0xfe, 0xba, 0xbe))
      ram.read(0x1000) should be (Some(0xff.toByte))
    }
  }
}
