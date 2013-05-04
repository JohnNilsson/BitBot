name := "bitbot-mtgoxapi"

version := "0.2"

scalaVersion := "2.10.1"

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

testOptions in Test += Tests.Argument("-oDS")

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.1.1"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.5"

//libraryDependencies += "org.apache.commons" % "commons-math3" % "3.2"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

//libraryDependencies += "org.mapdb" % "mapdb" % "0.9.1"

libraryDependencies += "com.higherfrequencytrading" % "chronicle" % "1.7"