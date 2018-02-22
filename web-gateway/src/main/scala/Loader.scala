

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.api.{ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.{ConfigurationServiceLocatorComponents, LagomServiceClientComponents}
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.livelygig.product.content.api.WalletService
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator, Mode}
import controllers.api.v1.wallet._

import scala.collection.immutable
import scala.concurrent.ExecutionContext
import com.softwaremill.macwire.wire
import controllers.api.v1.wallet.auth.MobileWalletController
import controllers.{Assets, ViewController, WebJarAssets}
import play.api.ApplicationLoader.Context
import play.api.http.{HttpErrorHandler, HttpRequestHandler}
import play.api.i18n.I18nComponents
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.EssentialFilter
import play.filters.gzip.GzipFilterComponents
import utils.web.{Filters, RequestHandler, WebGatewayErrorHandler}
import utils.AppLogger
import router.Routes

abstract class WebGateway(context: Context) extends BuiltInComponentsFromContext(context)
    with LagomServiceClientComponents
    with GzipFilterComponents with AhcWSComponents with I18nComponents {
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment)
  }

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "web-gateway",
    Map(
      "web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))
    )
  )

  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher
  lazy val routerOption = None
  override lazy val router = {
    // split route
    lazy val apiRoute: api.v1.Routes = {
      val prefix = "/v1"
      wire[api.v1.Routes]
    }
    val prefix = "/"
    wire[Routes]
  }

  // assets
  lazy val assets: Assets = wire[Assets]
  lazy val webjarAssets: WebJarAssets = wire[WebJarAssets]

  // services
  lazy val walletService = serviceClient.implement[WalletService]

  // controllers
  lazy val viewController: ViewController = wire[ViewController]
  lazy val walletController: WalletController = wire[WalletController]

  // For Mobile
  lazy val mobileWalletController: MobileWalletController = wire[MobileWalletController]
  lazy val mobileApiWalletController: MobileApiWalletController = wire[MobileApiWalletController]

  override lazy val httpRequestHandler: HttpRequestHandler = wire[RequestHandler]
  override lazy val httpErrorHandler: HttpErrorHandler = wire[WebGatewayErrorHandler]
  lazy val filtersWire = wire[Filters]
  override lazy val httpFilters: Seq[EssentialFilter] = filtersWire.filters
}

class WebGatewayLoader extends ApplicationLoader with AppLogger {
  log.info(s"Web gateway is loading.")
  override def load(context: Context) = context.environment.mode match {
    case Mode.Dev =>
      new WebGateway(context) with LagomDevModeComponents {}.application
    /* new WebGateway(context) {
        override def serviceLocator = NoServiceLocator
      }.application*/

    case _ =>
      (new WebGateway(context) with ConfigurationServiceLocatorComponents).application
  }
}

