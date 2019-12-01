package com.nathanalderson.nessie

case class Bus(devices: Devices, lastValRead: Data = 0.toByte) {
  def read(addr: Addr): (Data, Bus) = {
    val data = devices.flatMap(_.read(addr)) match {
      case List() => lastValRead
      case ds: List[Data] => ds.reduce((l, r) => (l | r).toByte)
    }
    (data, Bus(devices, data))
  }

  def write(data: Data, addr: Addr): Bus = {
    val new_devices = devices.map(_.write(data, addr))
    Bus(new_devices, lastValRead)
  }

  // following are not used by the system, but are useful for test/debugging
  def read(addrs: Range): (IndexedSeq[Data], Bus) =
    addrs.iterator.foldLeft(IndexedSeq[Data](), this) {
      case ((datas, bus), addr) =>
        val (data, newBus) = bus.read(addr)
        (datas :+ data, newBus)
    }

  def write(data: IterableOnce[Data], startAddr: Addr): Bus =
    data.iterator.foldLeft(this, startAddr) {
      case ((bus, addr), data) =>
        (bus.write(data, addr), addr+1)
    }._1
}
