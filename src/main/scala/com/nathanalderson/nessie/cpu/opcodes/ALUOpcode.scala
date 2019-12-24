package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.cpu.Registers
import com.nathanalderson.nessie.cpu.opcodes.Opcode.OpcodeType
import com.nathanalderson.nessie.{Bus, Data, Tick}

object ALUOpcode {
  def apply(byte: Data, bus: Bus): (Opcode, Bus) = {
    val op = (byte & 0xe0) >> 5 match {
//      case 0x00 => ORA()
      case 0x05 => LDA(byte)
      case _ => NOP()
    }
    (op, bus)
  }
}

case class LDA(opcode: OpcodeType) extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) = {
    val (addrMode, bus2, regs2) = AddressingMode.fromOpcode(opcode, bus, registers)
    val (value, extraTicks) = addrMode match {
      case Immediate(value) => (value, 0L)
      case Absolute(_, data) => (data, 1L)
    }
    val newRegs = regs2.copy(a=value, s=regs2.s.updateNegAndZero(value))
    (newRegs, bus2, addrMode.ticks + extraTicks)
  }
}

