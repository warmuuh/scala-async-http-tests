package com.company
import java.util.concurrent._
import java.util.concurrent.CompletableFuture._
import java.util.concurrent.ForkJoinPool
import scala.concurrent._
import scala.concurrent.duration._
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.DefaultAsyncHttpClient
import org.asynchttpclient.Response
import org.asynchttpclient.AsyncCompletionHandler

object AppAsyncHttp extends BaseApp {

  def testAsnycWithFuture() = {
    log.info("in main")
    
    
    val asyncHttpClient = new DefaultAsyncHttpClient();
    val f1 = asyncHttpClient.prepareGet("http://www.ning.com/").execute();
    f1.addListener(new Runnable(){
      def run() = {
        log.info("f1 done")
      }
    }, new ForkJoinPool())
    
    
    val r = f1.get
    log.info("done in main")
  }

  
  def testAsyncWithListener() = {
    log.info("in main")
    
    
    val asyncHttpClient = new DefaultAsyncHttpClient();
    val f1 = asyncHttpClient.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler[Response](){
      override def onCompleted(r: Response): Response= {
        log.info("in response handler")
        r
      }
      
      override def onThrowable(t: Throwable) = {}
    });
    
    val r = f1.get
    log.info("done in main")
  }

    def testAsyncWithPromise() = {
      implicit val ec = customEc
    log.info("in main")
    
    val p = Promise[Response]()
    
    val f1 = p.future
    //with promises, we can even reverse the order of logic
    //map will be scheduled in a new thread
    val f2 = f1.map { _ =>
        log.info("in f2")
        block(500)
        log.info("f2 done")
    }
      
      
    val asyncHttpClient = new DefaultAsyncHttpClient();
    asyncHttpClient.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler[Response](){
      override def onCompleted(r: Response): Response= {
        log.info("in response handler")
        p.success(r)
        r
      }
      
      override def onThrowable(t: Throwable) = {}
    });

    Await.result(f1, 10 seconds)
    Await.result(f2, 10 seconds)
    
    log.info("done in main")
  }
  
  def main(args: Array[String]) {
    testAsyncWithPromise()
  }

}