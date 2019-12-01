package com.nathanalderson.nessie

trait Device {
  val range: Range
  def read(addr: Addr): Option[Data]
  def write(data: Data, addr: Addr): Device

  // following are not used by the system, but are useful for test/debugging
  def read(addrs: Range): IndexedSeq[Data]
  def write(data: IterableOnce[Data], startAddr: Addr): Device
}
