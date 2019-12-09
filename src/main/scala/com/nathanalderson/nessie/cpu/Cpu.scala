package com.nathanalderson.nessie.cpu

import com.nathanalderson.nessie._

object Cpu {
  type Register = Data
  type WideRegister = Short

  def apply(): Cpu = Cpu(0L, Registers())
}
import Cpu._

object StatusRegister {
  def apply(): StatusRegister = StatusRegister(0)
}
case class StatusRegister(value: Register) {
  def negative: Boolean = 0 != (value | 0x80)
  def overflow: Boolean = 0 != (value | 0x40)
  def decimal: Boolean = 0 != (value | 0x08)
  def interrupt_disable: Boolean = 0 != (value | 0x04)
  def zero: Boolean = 0 != (value | 0x02)
  def carry: Boolean = 0 != (value | 0x01)
}

object Registers {
  def apply(): Registers = Registers(0, 0, 0, 0, StatusRegister(), 0)
}
case class Registers(pc: WideRegister,
                     a: Register,
                     x: Register,
                     y: Register,
                     s: StatusRegister,
                     p: Register)

case class Cpu(tick: Tick,
               registers: Registers)
{
  def runTo(tock: Tick, bus: Bus): (Cpu, Bus) = {
    val (data, newBus) = bus.read(registers.pc)
    val (newRegs, newBus2, ticks) = data match {
      case -24 => // 0xe8: INX
        val newX = registers.x + 1
        // TODO: status register
        (registers.copy(x=newX), newBus, 2)
      case _ =>
        (registers, newBus, 1)
    }
    (Cpu(tick+ticks, newRegs), newBus2)
  }
}
