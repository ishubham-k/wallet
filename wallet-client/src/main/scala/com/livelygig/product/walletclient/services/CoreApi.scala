package com.livelygig.product.walletclient.services

import com.livelygig.product.shared.models.wallet.EtherTransaction
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.window
import play.api.libs.json.Json

import scala.concurrent.Future
import scala.scalajs.LinkingInfo
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

/**
 * Created by shubham.k on 22-03-2017.
 */
object CoreApi {
  private val apiVersion = if (LinkingInfo.productionMode) "http://52.32.124.115:9000/v1" else "/v1"

  private val infuraUrl = "https://api.infura.io/v1/jsonrpc/ropsten"

  private def ajaxPost(requestContent: String, apiUrl: String): Future[String] = {
    Ajax.post(
      url = apiUrl,
      data = requestContent,
      headers = Map("Content-Type" -> "application/json;charset=UTF-8" /*, "Csrf-Token" -> AppUtils.getCookie("livelygig-csrf").get*/ )).map(_.responseText)
  }

  private def ajaxGet(url: String): Future[String] = {
    println(s"Url being called ${url}")
    Ajax.get(
      url = url).map(_.responseText)
  }

  def authenticate = {
    ajaxGet(s"${apiVersion}/auth/")
  }

  def getUserProfileOfCurrentUser() = ajaxGet(s"${apiVersion}/user${window.location.pathname}/profile")

  def sendEtherTransaction(etherTransaction: EtherTransaction) = {
    ajaxPost(Json.stringify(Json.toJson[EtherTransaction](etherTransaction)), s"${apiVersion}/wallet/transaction")
  }

  //def getEtherBalance() = ajaxGet(s"${apiVersion}/wallet/balance")

  // def getAccountDetails() = ajaxGet(s"${apiVersion}/wallet/accountdetails")
  def getUserDetails() = ajaxGet(s"${apiVersion}/wallet/mobile/account/details")

  def getAccountDetails() = ajaxGet(s"${apiVersion}/wallet/account/erctoken/details")

  def getAccountHistoryDetails() = ajaxGet(s"${apiVersion}/wallet/transactions")

  def getLang(lang: String) = ajaxGet(s"${apiVersion}/wallet/mobile/language/${lang}")

  def getTransactionStatus(txnHash: String) = ajaxGet(s"${apiVersion}/wallet/mobile/status/${txnHash}")

  def getNotification() = ajaxGet(s"${apiVersion}/wallet/mobile/notifications")

  def getQRCode() = ajaxGet(s"${apiVersion}/wallet/wallet/qrcode")

  def signOut() = ajaxPost("{}", s"${apiVersion}/wallet/auth/signout")

  def getETHNetInfo() = ajaxGet(s"${apiVersion}/wallet/ethnet/info")

  def captureQRCode() = ajaxGet(s"${apiVersion}/wallet/qrcode")

  def getLivePrices() = ajaxGet(s"${apiVersion}/wallet/coin/prices")

  //  API path Call for Mobile-App

  def mobileGetUserDetails() = ajaxGet(s"${apiVersion}/wallet/mobile/${dom.window.localStorage.getItem("pubKey")}/account/details")

  def mobileGetAccountDetails() = ajaxGet(s"${apiVersion}/wallet/mobile/${dom.window.localStorage.getItem("pubKey")}/account/erctoken/details")

  def mobileGetAccountHistoryDetails() = ajaxGet(s"${apiVersion}/wallet/mobile/${dom.window.localStorage.getItem("pubKey")}/transactions")

  def mobileGetLivePrices() = ajaxGet(s"${apiVersion}/wallet/mobile/coin/prices")

  def mobileGetETHNetInfo() = ajaxGet(s"${apiVersion}/wallet/mobile/ethnet/info")

  def mobileGetLang(lang: String) = ajaxGet(s"${apiVersion}/wallet/mobile/language/${lang}")

  def mobileGetTransactionStatus(txnHash: String) = ajaxGet(s"${apiVersion}/wallet/mobile/status/${txnHash}")

  def mobileSendSignedTxn(signedTxn: String) =
    ajaxPost(Json.stringify(Json.toJson(signedTxn)), s"${apiVersion}/wallet/mobile/signedTxn")

}

