package com.company

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scala.concurrent.Future
import scala.concurrent._
import scala.concurrent.duration._

class BaseApp {
   def block(ms: Long) = {
    Thread.sleep(ms)
  }
  
  val customEc = new ExecutionContext(){
    val log = LoggerFactory.getLogger("ec")
    def execute(runnable: Runnable): Unit = {
      log.info("execution scheduled: " + runnable)
      ExecutionContext.global.execute(runnable)
    }
    def reportFailure(t: Throwable): Unit = ExecutionContext.global.reportFailure(t)
  }
  val log = LoggerFactory.getLogger("threads")
}