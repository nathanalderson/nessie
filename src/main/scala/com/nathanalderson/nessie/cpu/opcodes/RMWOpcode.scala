package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.cpu.Registers
import com.nathanalderson.nessie.cpu.opcodes.Opcode.OpcodeType
import com.nathanalderson.nessie.{Bus, Data, Tick}

object AddressingMode {
  def fromOpcode(opcode: OpcodeType, bus: Bus, registers: Registers): (AddressingMode, Bus, Registers) = {
    (opcode & 0x1c) >> 2 match {
      case 0x00 =>
        val (nextByte, bus2, regs2) = registers.readAndIncrementPC(bus)
        (Immediate(nextByte), bus2, regs2)
    }
  }
}

sealed abstract class AddressingMode(val ticks: Tick)
case class Immediate(value: Byte) extends AddressingMode(2L)

object RMWOpcode {
  def apply(byte: Data, bus: Bus): (Opcode, Bus) = {
    // first check for explicit match with single-byte opcodes
    val singleByteOp = byte match {
      case TXS.opcode => Some(TXS())
      case NOP.opcode => Some(NOP())
      case _ => None
    }
    // otherwise decode as the following RMW instructions
    val op = singleByteOp.getOrElse((byte & 0xe0) >> 5 match {
//      case 0x0 => ASL()
//      case 0x1 => ROL()
//      case 0x2 => LSR()
//      case 0x3 => ROR()
//      case 0x4 => STX()
      case 0x5 => LDX(byte)
//      case 0x6 => DEC()
//      case 0x7 => INC()
      case _ => NOP()
    })
    (op, bus)
  }
}

case class LDX(opcode: OpcodeType) extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) = {
    val (addrMode, bus2, regs2) = AddressingMode.fromOpcode(opcode, bus, registers)
    val (regs3, bus3) = addrMode match {
      case Immediate(value) =>
        val newStatus = regs2.s.updateNegAndZero(value)
        (regs2.copy(x=value, s=newStatus), bus2)
    }
    (regs3, bus3, addrMode.ticks)
  }
}

object TXS {
  val opcode: Byte = 0x9a.toByte
}
case class TXS() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) =
    (registers.copy(stk=registers.x), bus, 2L)
}
