
import java.net._
import java.io._
import java.util.Date

import com.typesafe.config.ConfigFactory

import scala.io._

class Talk2Me {

  val config = ConfigFactory.load("application.conf")

  def start(): Unit = {
    val server = new ServerSocket(9999)

    import java.net._
    val localhost = InetAddress.getLocalHost

    println("[INFO] starting a server at " + localhost.getHostAddress  + ":9999")
    while (true) {
      val clientConnection = server.accept()
      println(s"[INFO] connection established from ${clientConnection.getInetAddress}:${clientConnection.getPort} " +
        s"at ${new Date()}")
      val request = new BufferedSource(clientConnection.getInputStream).getLines()
      val response = new PrintWriter(clientConnection.getOutputStream)

      val data = Source.fromFile(config.getString("share.file1")).getLines()
         .foldLeft(new StringBuilder)((a, b) => a append b append "\n").toString()

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
