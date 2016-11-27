package com.example.controller

import akka.actor.ActorSystem
import ch.qos.logback.classic.LoggerContext
import com.example.Settings
import org.slf4j.LoggerFactory
import slack.rtm.SlackRtmClient

import scala.language.postfixOps

object SlackController {
  suppressLogging()

  implicit val system = ActorSystem("slack")
  implicit val ec = system.dispatcher

  val client = SlackRtmClient(Settings.slack.token)

  // これがないと、初回のsendMessageが失敗する？
  Thread.sleep(5000L)

  private val channelId = client.state.getChannelIdForName(Settings.slack.postChName).get

  def sendMessage(text: String) = {
    client.sendMessage(channelId, text)
  }

  private def suppressLogging() = {
    val logger = LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]
    logger.stop()
  }
}
