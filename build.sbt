name := "BitBot"

version := "0.1"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
	"com.xeiam.xchange" % "xchange-core" % "1.5.0",
	"com.xeiam.xchange" % "xchange-mtgox" % "1.5.0")


mainClass := Some("nu.milsson.BitBot")

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")