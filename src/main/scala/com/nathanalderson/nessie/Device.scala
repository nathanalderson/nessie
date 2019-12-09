package com.nathanalderson.nessie

trait Device {
  val range: Range
  def read(addr: Addr): Option[Data]
  def write(data: Data, addr: Addr): Device

  // following are not used by the system, but are useful for test/debugging
  def read(addrs: Range): IndexedSeq[Data] =
    addrs.flatMap(read(_))

  def write(data: IterableOnce[Data], startAddr: Addr): Device =
    data.iterator.foldLeft((this: Device, startAddr)) {
      case ((device, addr), d) => (device.write(d, addr), addr+1)
    }._1
}
