package org.apache.spark

import org.apache.spark.rpc._
import org.apache.spark.rpc.netty.NettyRpcEnvFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


object RpcServerTest {

  def main(args: Array[String]): Unit = {
    val config = RpcEnvServerConfig(new RpcConf(), "hello-server", "localhost", 52345)
    // todo 非常重要 创建一个RpcServer
    val rpcEnv: RpcEnv = NettyRpcEnvFactory.create(config)
    val helloEndpoint: RpcEndpoint = new HelloEndpoint(rpcEnv)
    val helloEndpointRef = rpcEnv.setupEndpoint(HelloEndpoint.ENDPOINT_NAME, helloEndpoint)
    val f = Future {
      val future: Future[String] = helloEndpointRef.ask[String](SayHello("abc"))
      future.onComplete {
        case scala.util.Success(value) => println(s"client got result => $value")
        case scala.util.Failure(e) => e.printStackTrace
      }
    }
    Await.result(f, Duration.apply("240s"))
    println("waiting to be called...")
    rpcEnv.awaitTermination()
  }

}

class HelloEndpoint(realRpcEnv: RpcEnv) extends ThreadSafeRpcEndpoint {

  override def onStart(): Unit = {
    println("start hello endpoint")
  }

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    // Messages sent and received locally
    case SayHello(msg) => {
      println(s"receive $msg")
      context.reply(msg.toUpperCase)
    }
  }

  override def onStop(): Unit = {
    println("stop hello...")
  }

  /**
    * The [[RpcEnv]] that this [[RpcEndpoint]] is registered to.
    */
  override val rpcEnv: RpcEnv = realRpcEnv
}

object HelloEndpoint {
  val ENDPOINT_NAME = "my-hello"
}

case class SayHello(msg: String)
