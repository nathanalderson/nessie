package com.nathanalderson.nessie

import com.nathanalderson.nessie.cpu.Cpu

import scala.util.{Failure, Success, Try}

object System {
  sealed abstract class NessieError(msg: String) extends RuntimeException(msg)
  case class Halt() extends NessieError(s"halted")

  def apply(devices: List[Device]): System =
    System(Cpu(), Bus(devices), 0L)

  def apply(): System =
    apply(List())
}
case class System(cpu: Cpu,
                  bus: Bus,
                  tick: Tick,
                 )
{
  import System._

  def step(): System = {
    val tock = tick + 1
    val (newCpu, newBus) = cpu.runTo(tock, bus)
    System(newCpu, newBus, tock)
  }

  def tryStep(): Try[System] = Try(this.step())

  def run(): Iterator[Try[System]] =
    Iterator.iterate(Try(this))(_.flatMap(_.tryStep()))

  def runUntilHalt: Iterator[System] =
    run().takeWhile {
      case Failure(Halt()) => false
      case _ => false
    }.map(_.get)

  override def toString: String = f"System<PC: 0x${cpu.registers.pc}%x>"
}
