package com.livelygig.product.walletclient.views.facades

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@JSGlobal("toastr")
@js.native
object Toastr extends js.Object {
  def success(message: String, title: Option[String] = None, overrideOptions: Option[String] = None): Unit = js.native
  def info(message: String, title: Option[String] = None, overrideOptions: Option[String] = None): Unit = js.native
  def warning(message: String, title: Option[String] = None, overrideOptions: Option[String] = None): Unit = js.native
  def error(message: String, title: Option[String] = None, overrideOptions: Option[String] = None): Unit = js.native

  def remove(): Unit = js.native
  def clear(): Unit = js.native

  def options: js.Dynamic = js.native

}

