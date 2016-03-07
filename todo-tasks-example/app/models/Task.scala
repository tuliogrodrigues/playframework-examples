package models

import scredis.Redis

import scala.concurrent.Future


/**
  * Created by trodrigues on 03/03/16.
  */

case class Task(id: String, title: String, description: String)

object Task {

  val redis = Redis()

  import redis.dispatcher
  import scredis.serialization._

  implicit val taskWriter = new Writer[Task] {
    private val utf16StringWriter = new StringWriter("UTF-16")
    override protected def writeImpl(value: Task): Array[Byte] = {
      utf16StringWriter.write(s"${value.id}:${value.title}:${value.description}")
    }
  }

  implicit val taskReader = new Reader[Task] {
    val utf16StringReader = new StringReader("UTF-16")

    override def readImpl(bytes: Array[Byte]): Task = {
      val split = utf16StringReader.read(bytes).split(":")
      Task(split(0), split(1), split(2))
    }
  }

  def list(key: String) = {
    for {
      taskList <- redis.hGetAll(key)
    } yield {
      taskList.getOrElse(Map.empty[String, Task])
    }
  }

  def store(key:String, value:Task) = redis.withTransaction { t =>
    t.hSet(key, value.id, value)
    t.hGet(key, value.id)
  }

  def remove(key:String, id:String) = redis.hDel(key, id)

  def removeAll(key:String, ids:Seq[String]) = redis.withTransaction { t =>
    Future.apply(ids.map(redis.hDel(key, _)))
  }
}


