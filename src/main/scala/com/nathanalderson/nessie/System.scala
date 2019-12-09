package com.nathanalderson.nessie

import com.nathanalderson.nessie.cpu.{Cpu, Registers}

object System {
  def apply(devices: List[Device]): System =
    System(Cpu(), Bus(devices), 0L)

  def apply(): System =
    apply(List())
}
case class System(cpu: Cpu,
                  bus: Bus,
                  tick: Tick,
                 )
{
  def step(): System = {
    val tock = tick + 1
    val (newCpu, newBus) = cpu.runTo(tock, bus)
    System(newCpu, newBus, tock)
  }
}
