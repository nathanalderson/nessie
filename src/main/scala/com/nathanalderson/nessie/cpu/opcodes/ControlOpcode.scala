package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.cpu.Cpu.Register
import com.nathanalderson.nessie.cpu.{Registers, StatusRegister}
import com.nathanalderson.nessie.{Bus, Data, Tick}

object ControlOpcode {
  def apply(byte: Data, bus: Bus): (Opcode, Bus) = {
    if (byte == 0xd8.toByte) (CLD(), bus)
    else if (byte == 0xe8.toByte) (INX(), bus)
    else (NOP(), bus)
  }
}

case class CLD() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) = {
    (registers.copy(s=registers.s.clear(StatusRegister.mask_decimal)), bus, 2L)
  }
}

case class INX() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) = {
    val newX: Register = (registers.x + 1.toByte).toByte
    val newStatusReg = newX match {
      case 0 => registers.s.set(StatusRegister.mask_zero)
        .clear(StatusRegister.mask_negative)
      case x if (x < 0) => registers.s.set(StatusRegister.mask_negative)
        .clear(StatusRegister.mask_zero)
      case _ => registers.s.clear(StatusRegister.mask_zero)
        .clear(StatusRegister.mask_negative)
    }
    (registers.copy(x=newX, s=newStatusReg), bus, 2L)
  }
}
