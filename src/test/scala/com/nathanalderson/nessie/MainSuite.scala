package com.nathanalderson.nessie

import org.scalatest.{Matchers, WordSpec}

class MainSuite extends WordSpec with Matchers {
  "A thing" should {
    "do stuff" in {
      1 should equal (1)
    }
  }
}
