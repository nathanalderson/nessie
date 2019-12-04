import Dependencies._
import scala.sys.process._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.nathanalderson"
ThisBuild / organizationName := "Nathan Alderson"

val assembleTestRoms = taskKey[Unit]("Assemble the Test ROM(s)")

lazy val root = (project in file("."))
  .settings(
    name := "nessie",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.1" % "test",
    assembleTestRoms := {
      val as65 = baseDirectory.value / "tools/as65_142/as65"
      val inDir = baseDirectory.value / "src/test/6502_65C02_functional_tests"
      val inFiles = List(inDir / "6502_functional_test.a65")
      val outDir = target.value / "a65"
      s"mkdir -p $outDir".!
      inFiles.foreach { file =>
        Process(s"$as65 -l -l$outDir/${file.base}.list -o$outDir/${file.base}.s19 -m -w -h0 -s $file", inDir).!
      }
      ()
    },
    Test / test := (Test / test dependsOn assembleTestRoms).value
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
