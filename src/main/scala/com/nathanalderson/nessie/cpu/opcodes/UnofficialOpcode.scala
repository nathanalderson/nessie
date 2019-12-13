package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.{Bus, Data}

object UnofficialOpcode {
  def apply(byte: Data, bus: Bus): (Opcode, Bus) = (NOP(), bus)
}
