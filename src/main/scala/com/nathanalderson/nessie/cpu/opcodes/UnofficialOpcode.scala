package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.System.Halt
import com.nathanalderson.nessie.cpu.Registers
import com.nathanalderson.nessie.{Bus, Data, Tick}

object UnofficialOpcode {
  def apply(byte: Data, bus: Bus): (Opcode, Bus) = (HALT(), bus)
}

case class HALT() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) =
    throw Halt()
}
