package com.company

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scala.concurrent.Future
import scala.concurrent._
import scala.concurrent.duration._

object App extends BaseApp{
  
  def testFutures() = {
    implicit val ec = customEc
    log.info("in main")
    val f1 = Future(log.info("in f1"), block(500), log.info("f1 done"))
    val f2 = Future(log.info("in f2"), block(500), log.info("f2 done"))
    val f3 = Future(log.info("in f3"), block(500), log.info("f3 done"))
    
    val fjoint = Future.sequence(Seq(f1, f2, f3))
    //val fj2 = fjoint.map {_ => log.info("in fj2"); block(500); log.info("fj2 done")}
    
    Await.result(f1, 1 second)
    Await.result(f2, 1 second)
    Await.result(f3, 1 second)
    Await.result(fjoint, 1 second)
    //Await.result(fj2, 1 second)
    log.info("after await")
    
  }
  
  def main(args : Array[String]) {

    testFutures()
  
  
  }

}
