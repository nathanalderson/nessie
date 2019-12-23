package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.System.Halt
import com.nathanalderson.nessie.cpu.Cpu.Register
import com.nathanalderson.nessie.cpu.{Registers, StatusRegister}
import com.nathanalderson.nessie.{Bus, Data, Tick}

object ControlOpcode {
  def apply(byte: Data, bus: Bus): (Opcode, Bus) = {
    val singleByteOp = byte match {
      case BRK.opcode => Some(BRK())
      case CLD.opcode => Some(CLD())
      case INX.opcode => Some(INX())
      case _ => None
    }
    val op = singleByteOp.getOrElse((byte & 0xe0) >> 5 match {
//      case 0x0 =>
//      case 0x1 =>
//      case 0x2 =>
//      case 0x3 =>
//      case 0x4 =>
//      case 0x5 =>
//      case 0x6 =>
//      case 0x7 =>
      case _ => NOP()
    })
    (op, bus)
  }
}

object BRK {
  val opcode: Data = 0x00.toByte
}
case class BRK() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) =
    throw Halt()
}

object CLD {
  val opcode: Data = 0xd8.toByte
}
case class CLD() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) = {
    (registers.copy(s=registers.s.clear(StatusRegister.mask_decimal)), bus, 2L)
  }
}

object INX {
  val opcode: Data = 0xe8.toByte
}
case class INX() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) = {
    val newX: Register = (registers.x + 1.toByte).toByte
    val newStatusReg = registers.s.updateNegAndZero(newX)
    (registers.copy(x=newX, s=newStatusReg), bus, 2L)
  }
}
