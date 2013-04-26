import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName			= "bitbot"
  val appVersion 		= "0.2"
  val appDependencies 	= Seq()

  val mtgoxApi		= Project("bitbot-mtgoxapi", file("mtgoxapi"))
  val bitbot		= play.Project(
  	appName, appVersion, appDependencies, file("bitbot")
  )
  .dependsOn(mtgoxApi)
  .settings(
  	requireJS += "main.js"
  )
}