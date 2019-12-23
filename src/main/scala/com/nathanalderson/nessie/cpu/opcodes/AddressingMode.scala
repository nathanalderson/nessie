package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.{Addr, Bus, Tick}
import com.nathanalderson.nessie.cpu.Registers
import com.nathanalderson.nessie.cpu.opcodes.Opcode.OpcodeType

object AddressingMode {
  def fromOpcode(opcode: OpcodeType, bus: Bus, registers: Registers): (AddressingMode, Bus, Registers) = {
    (opcode & 0x1c) >> 2 match {
      case 0x00 => (opcode & 0x3) match { // Immediate
        case 0x01 => // for ALU opcodes: (zero page, X)
          ???
        case _ => // for control and RMW opcodes: #Immediate
          val (nextByte, bus2, regs2) = registers.readAndIncrementPC(bus)
          (Immediate (nextByte), bus2, regs2)
      }
      case 0x01 => ??? // zero page
      case 0x02 => (opcode & 0x3) match {
        case 0x01 => // for ALU opcodes: #immediate
          ???
        case _ => // for control and RMW opcodes: accumulator
          ???
      }
      case 0x03 => // absolute
        val (byte1, bus2, regs2) = registers.readAndIncrementPC(bus)
        val (byte2, bus3, regs3) = regs2.readAndIncrementPC(bus2)
        (Absolute((byte2 << 8 | byte1).toShort), bus3, regs3)
      case 0x04 => ??? // (zero page), Y
      case 0x05 => ??? // zero page, X
      case 0x06 => ??? // absolute, Y
      case 0x07 => ??? // absolute, X
    }
  }
}

sealed abstract class AddressingMode(val ticks: Tick)

case class Immediate(value: Byte) extends AddressingMode(2L)
case class Absolute(value: Addr) extends AddressingMode(3L)
