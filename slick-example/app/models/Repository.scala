package models

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

  private def db: Database = Database.forDataSource(DB.getDataSource())

  def insert(entity:T): Future[Int] = {
    try db.run(tableQuery += entity)
    finally db.close
  }

  def update(id: String, entity:T): Future[Int] = {
    try db.run(tableQuery.filter(_.id === id).update(entity))
    finally db.close
  }

  def delete(id: String): Future[Int] = {
    try db.run(tableQuery.filter(_.id === id).delete)
    finally db.close
  }

  def findById(id: String): Future[T] = {
    try db.run(tableQuery.filter(_.id === id).result.head)
    finally db.close
  }

  def list(page: Int = 0, pageSize: Int = 10): Future[Seq[T]] = {
    try db.run(tableQuery.sortBy(_.id.desc).drop(page).take(pageSize).result)
    finally db.close()
  }
}

object UserRepository extends RepositoryComponent[User] {
  def tableQuery = TableQuery(new Users(_))


}