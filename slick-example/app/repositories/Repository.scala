package repositories

import models.{Users, User, Entity}
import models.modelsunscanned.TableWithId
import play.api.Play.current
import play.api.db.DB
import slick.driver.H2Driver.api._

import scala.concurrent.Future

/**
  * Created by trodrigues on 2/14/16.
  */

trait RepositoryComponent[T <: Entity] {

  def tableQuery: TableQuery[TableWithId[T]]

  def db: Database = Database.forDataSource(DB.getDataSource())

  def insert(entity:T): Future[Int] = {
    db.run(tableQuery += entity)
  }

  def update(id: Long, entity:T): Future[Int] = {
    db.run(tableQuery.filter(_.id === id).update(entity))
  }

  def delete(id: Long): Future[Int] = {
    db.run(tableQuery.filter(_.id === id).delete)
  }

  def findById(id: Long): Future[T] = {
    db.run(tableQuery.filter(_.id === id).result.head)
  }

  def list(page: Int = 0, pageSize: Int = 10): Future[Seq[T]] = {
    db.run(tableQuery.sortBy(_.id.desc).drop(page).take(pageSize).result)
  }
}

object UserRepository extends RepositoryComponent[User] {
  def tableQuery = TableQuery(new Users(_))
  db.run(DBIO.seq(tableQuery.schema.create))
}