package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.cpu.Registers
import com.nathanalderson.nessie.{Bus, Data, Tick}

object Opcode {
  def apply(byte: Data, bus: Bus): (Opcode, Bus) =
    (byte & 0x03) match {
      case 0x00 => ControlOpcode(byte, bus)
      case 0x01 => ALUOpcode(byte, bus)
      case 0x02 => RMWOpcode(byte, bus)
      case _ => UnofficialOpcode(byte, bus)
    }
  type OpcodeType = Byte
}

trait Opcode {
  def execute(registers: Registers, bus: Bus): (Registers, Bus, Tick)
}








