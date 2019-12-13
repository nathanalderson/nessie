package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.cpu.{Registers, StatusRegister}
import com.nathanalderson.nessie.{Bus, Data, Tick}

object RMWOpcode {
  def apply(byte: Data, bus: Bus): (Opcode, Bus) = {
    val op = (byte & 0xe0) >> 5 match {
//      case 0x0 => ASL()
//      case 0x1 => ROL()
//      case 0x2 => LSR()
//      case 0x3 => ROR()
//      case 0x4 => STX()
//      case 0x5 => LDX()
//      case 0x6 => DEC()
//      case 0x7 => INC()
      case _ => NOP()
    }
    (op, bus)
  }
}
