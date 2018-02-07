import Dependencies.{SharedDependencies, _}
//import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.SbtScalariform.{ScalariformKeys, scalariformSettings}
import net.virtualvoid.sbt.graph.DependencyGraphSettings.graphSettings
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import webscalajs.ScalaJSWeb

object Shared {
  val projectId = "livelygig"
  val projectName = "Livelygig"

  lazy val commonSettings = Seq(
    version := Shared.Versions.app,
    scalaVersion := Shared.Versions.scala,
    organization := "Livelygig",
    scalacOptions ++= Seq(
      "-encoding", "UTF-8", "-feature", "-deprecation", "-unchecked", "–Xcheck-null" /*, "-Xfatal-warnings"*/ , "-Xlint",
      "-Ywarn-adapted-args", "-Ywarn-dead-code", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-numeric-widen", "-language:higherKinds",
      "-language:implicitConversions"
    ),
    scalacOptions in Test ++= Seq("-Yrangepos"),

    publishMavenStyle := false,

    // Prevent Scaladoc
    publishArtifact in(Compile, packageDoc) := false,
    publishArtifact in packageDoc := false,
    sources in(Compile, doc) := Seq.empty,

    // Code Quality
    //    scapegoatVersion := Utils.scapegoatVersion,
    //    scapegoatDisabledInspections := Seq("MethodNames", "MethodReturningAny", "DuplicateImport"),
    //    scapegoatIgnoredFiles := Seq(".*/JsonSerializers.scala"),
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
  ) ++ graphSettings ++ scalariformSettings

  object Versions {
    val app = "0.3"
    val scala = "2.11.11"
  }

  def withProjects(p: Project, includes: Seq[Project]) = includes.foldLeft(p) { (proj, inc) =>
    proj.aggregate(inc).dependsOn(inc)
  }

  lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).settings(commonSettings: _*).settings(
    libraryDependencies ++= Seq(
      "com.chuusai" %%% "shapeless" % SharedDependencies.shapelessVersion
    )
  )
    .jvmSettings(
      libraryDependencies ++= Seq(
        SharedDependencies.derivedCodecs,
        SharedDependencies.ficus,
        SharedDependencies.playEnumeratum
      )
    )
    .jsSettings(
      libraryDependencies ++= Seq(
        "com.typesafe.play" %%% "play-json" % SharedDependencies.playJsonVersion
      )
    )
    .jsConfigure(_ enablePlugins ScalaJSWeb)


  lazy val sharedJs = shared.js
  lazy val sharedJvm = shared.jvm

}
