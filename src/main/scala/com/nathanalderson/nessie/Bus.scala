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
}
