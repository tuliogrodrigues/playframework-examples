package repositories

import java.sql.Connection

import play.api.db
import play.api.Play.current
import scalikejdbc._

/**
  * Created by trodrigues on 26/02/16.
  */
object Database {

  implicit val session = AutoSession

  ConnectionPool.singleton(new DataSourceConnectionPool(db.DB.getDataSource()))

  def connection = ConnectionPool.borrow()

  def withTransaction[T,C](sql:SQL[T, HasExtractor], f:SQL[T, HasExtractor] => C) = {

    val conn:Connection = Database.connection

    try {
      f.apply(sql)
    } finally {
      conn.close()
    }
  }

  def singleResult[T](sql:SQL[T, HasExtractor]) = {
    withTransaction[T, Option[T]](sql, (sq:SQL[T, HasExtractor]) => sq.single().apply())
  }

  def listResult[T](sql:SQL[T, HasExtractor]) = {
    withTransaction[T, List[T]](sql, (sq:SQL[T, HasExtractor]) => sq.list().apply())
  }

  def update[T](sql:SQL[T, HasExtractor]) = {
    withTransaction[T, Int](sql, (sq:SQL[T, HasExtractor]) => sq.update().apply())
  }
}
