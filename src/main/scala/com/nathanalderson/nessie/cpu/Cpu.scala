package com.nathanalderson.nessie.cpu

import com.nathanalderson.nessie._
import com.nathanalderson.nessie.cpu.opcodes.Opcode

object Cpu {
  type Register = Data
  type WideRegister = Short

  def apply(): Cpu = Cpu(0L, Registers())
}
import Cpu._

object StatusRegister {
  def apply(): StatusRegister = StatusRegister(0)
  val mask_negative: Byte = 0x80
  val mask_overflow: Byte = 0x40
  val mask_decimal: Byte = 0x08
  val mask_interrupt_disable: Byte = 0x04
  val mask_zero: Byte = 0x02
  val mask_carry: Byte = 0x01
}
case class StatusRegister(value: Register) {
  import StatusRegister._
  def negative: Boolean = 0 != (value & mask_negative)
  def overflow: Boolean = 0 != (value & mask_overflow)
  def decimal: Boolean = 0 != (value & mask_decimal)
  def interrupt_disable: Boolean = 0 != (value & mask_interrupt_disable)
  def zero: Boolean = 0 != (value & mask_zero)
  def carry: Boolean = 0 != (value & mask_carry)
  def set(bits: Byte*): StatusRegister =
    StatusRegister(bits.foldLeft(value)(_ | _))
  def clear(bits: Byte*): StatusRegister =
    StatusRegister(bits.foldLeft(value)(_ & ~_))
  def updateNegAndZero(value: Byte): StatusRegister =
    value match {
      case 0 => set(StatusRegister.mask_zero).clear(StatusRegister.mask_negative)
      case x if (x < 0) => set(StatusRegister.mask_negative).clear(StatusRegister.mask_zero)
      case _ => clear(StatusRegister.mask_zero, StatusRegister.mask_negative)
    }
}

object Registers {
  def apply(): Registers = Registers(0, 0, 0, 0, StatusRegister(), 0, 0)
}
case class Registers(pc: WideRegister,
                     a: Register,
                     x: Register,
                     y: Register,
                     s: StatusRegister,
                     p: Register,
                     stk: Register)
{
  def readAndIncrementPC(bus: Bus): (Data, Bus, Registers) = {
    val (data, bus2) = bus.read(pc)
    (data, bus2, this.copy(pc=pc+1))
  }
}

case class Cpu(tick: Tick,
               registers: Registers)
{
  def runTo(tock: Tick, bus: Bus): (Cpu, Bus) = {
    val (data, bus2, regs2) = registers.readAndIncrementPC(bus)
    val (opcode, bus3) = Opcode(data, bus2)
    val (regs3, bus4, ticks) = opcode.execute(regs2, bus3)
    (Cpu(tick+ticks, regs3), bus4)
  }
}
