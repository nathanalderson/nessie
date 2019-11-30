package com.nathanalderson.nessie

trait Device {
  val range: Range
  def read(addr: Addr): Option[Data]
  def write(data: Data, addr: Addr): Device
}
