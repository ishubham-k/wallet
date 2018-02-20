import com.lightbend.lagom.sbt.LagomImport.lagomScaladslServer
import com.lightbend.lagom.sbt.LagomPlay
import com.typesafe.sbt.digest.Import._
import com.typesafe.sbt.gzip.Import._
import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import com.typesafe.sbt.less.Import._
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.SbtWeb
import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.PlayImport.PlayKeys._
import play.sbt.PlayLayoutPlugin
import play.sbt.routes.RoutesKeys.routesGenerator
import sbt.Keys._
import sbt._
import webscalajs.WebScalaJS.autoImport.{devCommands, scalaJSPipeline, scalaJSProjects}

import scalajsbundler.sbtplugin.WebScalaJSBundlerPlugin

object Server {
  private[this] val dependencies = {
    import Dependencies._
    Seq(
      Cache.ehCache, Akka.actor, Akka.logging, Play.playFilters, Play.playWs, Play.playWebjars, Play.playBootstrap,
      Authentication.silhouette, Authentication.hasher, Authentication.persistence, Authentication.crypto,
      WebJars.fontAwesome, WebJars.selectize, WebJars.datepicker, WebJars.toastr, WebJars.blockies, WebJars.validator,
      Utils.crypto, Utils.commonsIo, SharedDependencies.macwire, Akka.testkit, Play.playTest,
      lagomScaladslServer, Play.scalajsScripts, SharedDependencies.scalaTest,Wallet.web3j
    )
  }

  private[this] lazy val serverSettings = Shared.commonSettings ++ Seq(
    name := "WebGateway",
    maintainer := "Ubunda Admin <admin@ubunda.com>",
    description := "Ubunda WebGateway",
    resolvers += Resolver.jcenterRepo,
    libraryDependencies ++= dependencies,
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    devCommands in scalaJSPipeline += "runAll",
    // connect to the client project
    scalaJSProjects := Seq(WalletClient.walletClient),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    routesGenerator := InjectedRoutesGenerator,
    externalizeResources := false,

    // Sbt-Web
    JsEngineKeys.engineType := JsEngineKeys.EngineType.Node,
    includeFilter in (Assets, LessKeys.less) := "*.less",
    excludeFilter in (Assets, LessKeys.less) := "_*.less",
    LessKeys.compress in Assets := true

  )

  lazy val webGateway = (project in file("web-gateway"))
    .enablePlugins(
      SbtWeb, play.sbt.PlayScala, LagomPlay, WebScalaJSBundlerPlugin)
    .disablePlugins(PlayLayoutPlugin) // use the standard directory layout instead of Play's custom
    .dependsOn(WalletApi.walletApi, Shared.sharedJvm)
    .settings(serverSettings: _*)
    .settings(Packaging.settings: _*)

  //    Shared.withProjects(ret, Seq(Shared.sharedJvm, Utilities.metrics))

}