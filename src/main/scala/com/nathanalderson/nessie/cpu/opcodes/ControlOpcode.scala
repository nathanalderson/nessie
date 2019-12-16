package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.System.Halt
import com.nathanalderson.nessie.cpu.Cpu.Register
import com.nathanalderson.nessie.cpu.{Registers, StatusRegister}
import com.nathanalderson.nessie.{Bus, Data, Tick}

object ControlOpcode {
  def apply(byte: Data, bus: Bus): (Opcode, Bus) = {
    if (byte == 0x00.toByte) (BRK(), bus)
    else if (byte == 0xd8.toByte) (CLD(), bus)
    else if (byte == 0xe8.toByte) (INX(), bus)
    else (NOP(), bus)
  }
}

case class BRK() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) =
    throw Halt()
}

case class CLD() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) = {
    (registers.copy(s=registers.s.clear(StatusRegister.mask_decimal)), bus, 2L)
  }
}

case class INX() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) = {
    val newX: Register = (registers.x + 1.toByte).toByte
    val newStatusReg = registers.s.updateNegAndZero(newX)
    (registers.copy(x=newX, s=newStatusReg), bus, 2L)
  }
}
