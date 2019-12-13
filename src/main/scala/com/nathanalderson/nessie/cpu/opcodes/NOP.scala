package com.nathanalderson.nessie.cpu.opcodes

import com.nathanalderson.nessie.cpu.Registers
import com.nathanalderson.nessie.{Bus, Tick}

case class NOP() extends Opcode {
  override def execute(registers: Registers, bus:  Bus): (Registers, Bus, Tick) =
    (registers, bus, 1L)
}
