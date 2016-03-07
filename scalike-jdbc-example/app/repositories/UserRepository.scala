package repositories

import java.util.UUID

import models.User
import scalikejdbc.{SQL, WrappedResultSet}

/**
  * Created by trodrigues on 26/02/16.
  */
object UserRepository {

  val parseToEntity = (rs:WrappedResultSet) => new User(
    rs.stringOpt("ID"),
    rs.string("FIRST_NAME"),
    rs.string("LAST_NAME"),
    rs.string("EMAIL"),
    rs.string("PHONE"))

  val querySchema = SQL(
    """CREATE TABLE USER(
      | ID VARCHAR(36),
      | FIRST_NAME VARCHAR(100),
      | LAST_NAME VARCHAR(100),
      | EMAIL VARCHAR(100),
      | PHONE VARCHAR(15)
      |) """.stripMargin
    ).map(rs => rs.string(0))

  Database.schema(querySchema)

  def list = {

    val query = SQL("SELECT * FROM USER")
      .map(parseToEntity)

    Database.listResult[User](query)
  }

  def findById(id:String) = {

    val query = SQL("SELECT * FROM USER WHERE ID = {userId}")
      .bindByName('userId -> id)
      .map(parseToEntity)

    Database.singleResult[User](query)
  }

  def save(user:User) = {

    val id = UUID.randomUUID().toString

    val query = SQL("INSERT INTO USER(ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE) VALUES({userId}, {firstName}, {lastName}, {email}, {phone})")
      .bindByName('userId -> id, 'firstName -> user.firstName, 'lastName -> user.lastName, 'email -> user.email, 'phone -> user.phone)
      .map(rs => rs.int(0))

    Database.update[Int](query) match {
      case count:Int if count > 0 => findById(id)
      case _ => Option.empty[User]
    }
  }

  def update(id:String, user:User) = {

    val query = SQL("UPDATE SET FIRST_NAME = {firstName}, LAST_NAME = {lastName}, EMAIL = {email}, PHONE = {phone} WHERE ID = {userId}")
      .bindByName('userId -> id, 'firstName -> user.firstName, 'lastName -> user.lastName, 'email -> user.email, 'phone -> user.phone)
      .map(rs => rs.int(0))

    Database.update[Int](query) match {
      case count:Int if count > 0 => findById(id)
      case _ => Option.empty[User]
    }
  }

  def delete(id:String) = {

    val query = SQL("DELETE FROM USER WHERE ID = {userId}")
      .bindByName('userId -> id)
      .map(rs => rs.int(0))

    Database.update[Int](query)
  }
}
