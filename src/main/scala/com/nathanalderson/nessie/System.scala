package com.nathanalderson.nessie

import com.nathanalderson.nessie.cpu.Cpu

import scala.util.{Failure, Success, Try}

object System {
  sealed abstract class NessieError(msg: String) extends RuntimeException(msg)
  case class Halt() extends NessieError(s"halted")

  def apply(bus: Bus): System =
    System(Cpu(), bus, 0L)

  def apply(devices: List[Device]): System =
    apply(Bus(devices))

  def apply(): System =
    apply(Bus(List()))
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

  def tryStep(): Try[System] =
    Try(this.step())

  def run(): Iterator[Try[System]] =
    Iterator.iterate(Try(this))(_.flatMap(_.tryStep()))

  def runUntilHalt(): Iterator[System] =
    run().takeWhile {
      case Failure(Halt()) => false
      case _ => true
    }.map(_.get)

  def runUntilHang(): Iterator[System] =
    runUntilHalt().sliding(2).takeWhile {
      case Seq(s1, s2) =>
        (s1.cpu.tick == s2.cpu.tick) || (s1.cpu.registers.pc != s2.cpu.registers.pc)
      case _ =>
        true
    }.flatten

  override def toString: String = f"System<tick: $tick $cpu>"
}
