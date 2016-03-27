package com.company
import java.util.concurrent._
import org.asynchttpclient.extras.rxjava._
import rx._
import java.util.concurrent.CompletableFuture._
import java.util.concurrent.ForkJoinPool
import rx.functions.Func0
import org.asynchttpclient.DefaultAsyncHttpClient
import rx.functions.Action0
import rx.functions.Action1
import org.asynchttpclient.Response
import scala.collection.JavaConversions._
import rx.functions.Func2
import rx.functions.Func1

object AppAsyncHttpRx extends BaseApp {
  
  implicit def ftoFunc0[T](f: () => T): Func0[T] = {
    new Func0[T](){
      def call() = f()
    }
  }
  implicit def ftoFunc1[T1,R](f: (T1) => R): Func1[T1,R] = {
    new Func1[T1,R](){
      def call(t1:T1) = f(t1)
    }
  }
  implicit def ftoFunc2[T1,T2, R](f: (T1,T2) => R): Func2[T1, T2,R] = {
    new Func2[T1,T2,R](){
      def call(t1:T1,t2:T2) = f(t1,t2)
    }
  }
  implicit def ftoAction[T](f: T => Unit): Action1[T] = {
    new Action1[T](){
      def call(t: T) = f(t)
    }
  }
  
   def testAsyncWithRx() = {
    log.info("in main")
    
    val o1:Observable[Response] = AsyncHttpObservable.toObservable( () => {
      log.info("triggering request1")
      new DefaultAsyncHttpClient().prepareGet("http://www.ning.com/") }).map((r:Response) => {
        log.info("request1 received")
        r
      })
    
    val o2:Observable[Response] = AsyncHttpObservable.toObservable( () => {
      log.info("triggering request2")
      new DefaultAsyncHttpClient().prepareGet("http://www.ning.com/") }).map((r:Response) => {
        log.info("request2 received")
        r
      })
    
    val o3:Observable[(Response,Response)] = Observable.zip(o1,o2, (r1:Response,r2:Response) => {
      log.info("zipping requests")
      r1 -> r2
      })
    
    o3.subscribe((rs:(Response,Response)) => log.info("in subscriber 3"))
    log.info("done in main")
  }

  
  
  
  def main(args: Array[String]) {
    testAsyncWithRx()
  }
}