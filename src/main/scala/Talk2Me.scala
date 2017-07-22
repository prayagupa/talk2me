
import java.net._
import java.io._

import com.typesafe.config.ConfigFactory

import scala.io._

class Talk2Me {

  val config = ConfigFactory.load("application.conf")

  val data = Source.fromFile(config.getString("share.file1")).getLines()
    .foldLeft(new StringBuilder)((a, b) => a append b append "\n").toString()

  def start(): Unit = {
    val server = new ServerSocket(9999)
    println("[INFO] starting a server.")
    while (true) {
      val clientConnection = server.accept()
      println(s"[INFO] connection established from ${clientConnection.getInetAddress}:${clientConnection.getPort}")
      val request = new BufferedSource(clientConnection.getInputStream).getLines()
      val response = new PrintWriter(clientConnection.getOutputStream)

      response.println("HTTP/1.1 200\r\n")
      response.println("Content-Type text/html\r\n")
      response.println(data)

      response.flush()
      clientConnection.close()
    }
  }

}

object Talk2MeServer {

  def main(args: Array[String]): Unit = {

    new Talk2Me().start()
  }

}
