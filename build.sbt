name := "BitBot"

version := "0.1"

scalaVersion := "2.10.1"

mainClass := Some("nu.milsson.BitBot")

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

//testOptions in Test += Tests.Argument("-oDS")

//libraryDependencies ++= Seq(
//	"com.xeiam.xchange" % "xchange-core" % "1.5.0",
//	"com.xeiam.xchange" % "xchange-mtgox" % "1.5.0")

//libraryDependencies ++= Seq(
//	"com.dyuproject.protostuff" % "protostuff-core" % "1.0.7",
//	"com.dyuproject.protostuff" % "protostuff-json" % "1.0.7",
//	"com.dyuproject.protostuff" % "protostuff-runtime" % "1.0.7"
//)

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.1.1"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.11"

libraryDependencies += "org.apache.commons" % "commons-math3" % "3.2"