package com.nathanalderson

package object nessie {
  type Devices = List[Device]
  type Data = Byte
  type Addr = Short
  type Tick = Long

  implicit def int2short(i: Int): Short = i.toShort
  implicit def int2byte(i: Int): Byte = i.toByte
}
