name := "rpgkata4"

version := "0.1"

scalaVersion := "2.12.3"
val versions = new {
  val scala = "2.12.4"
  val scalatest = "3.0.5"
  val mockito = "1.10.19"
}

lazy val commonSettings = Seq(
  version := "1.0",
  organization := "one.xingyi",
  publishMavenStyle := true,
  scalaVersion := versions.scala,
  scalacOptions ++= Seq("-feature"),
  libraryDependencies += "org.mockito" % "mockito-all" % versions.mockito % "test",
  libraryDependencies += "org.scalatest" %% "scalatest" % versions.scalatest % "test",
)
lazy val stage1 = (project in file("modules/stage1")).settings(commonSettings: _*)

lazy val stage2 = (project in file("modules/stage2")).settings(commonSettings: _*)

lazy val stage3 = (project in file("modules/stage3")).settings(commonSettings: _*)

lazy val stage4 = (project in file("modules/stage4")).settings(commonSettings: _*)

lazy val stage5 = (project in file("modules/stage5")).settings(commonSettings: _*)

lazy val stageFuture = (project in file("modules/stageFuture")).settings(commonSettings: _*)



