name := "rbnJs"
organization := "uk.radzi"
scalaVersion := "2.13.4"

Global / onChangedBuildSource := ReloadOnSourceChanges

enablePlugins(ScalaJSPlugin)

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0"

scalaJSUseMainModuleInitializer := true
