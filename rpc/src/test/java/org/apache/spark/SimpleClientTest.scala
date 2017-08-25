package org.apache.spark

import org.apache.spark.rpc.netty.NettyRpcEnvFactory
import org.apache.spark.rpc.{RpcAddress, RpcEndpointRef, RpcEnv, RpcEnvClientConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object SimpleClientTest {

  def main(args: Array[String]): Unit = {
    val config = RpcEnvClientConfig(new RpcConf(), "hello-client")
    val rpcEnv: RpcEnv = NettyRpcEnvFactory.create(config)
    val endPointRef: RpcEndpointRef = rpcEnv.setupEndpointRef(RpcAddress("localhost", 52345), HelloEndpoint.ENDPOINT_NAME)
    val future: Future[String] = endPointRef.ask[String](SayHello("abc"))
    future.onComplete {
      case scala.util.Success(value) => println(s"Got the result = $value")
      case scala.util.Failure(e) => e.printStackTrace
    }
    Await.result(future, Duration.apply("30s"))
  }
}
