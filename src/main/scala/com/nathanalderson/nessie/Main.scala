package com.nathanalderson.nessie

import com.nathanalderson.nessie.nessie.{Addr, Data}

trait Device {
  def read(addr: Addr): Data
  def write(data: Data, addr: Addr): Unit
}

trait Memory extends Device {
  override def read(addr: Addr): Data = ???

  override def write(data: Data, addr: Addr): Unit = ???
}

class Bus {
  val devices: Map[Int, Device] = Map()
}

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello")
  }
}
