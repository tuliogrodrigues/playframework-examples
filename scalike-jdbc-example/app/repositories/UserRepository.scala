package repositories

import java.util.UUID

import models.User
import scalikejdbc.{SQL, WrappedResultSet}

/**
  * Created by trodrigues on 26/02/16.
  */
object UserRepository {

  val parseToEntity = (rs:WrappedResultSet) => new User(rs.stringOpt(0), rs.string(1), rs.string(2), rs.string(3), rs.string(4))

  def list = {

    val query = SQL("SELECT * FROM USER")
      .map(parseToEntity)

    Database.listResult[User](query)
  }

  def findById(id:String) = {

    val query = SQL("SELECT * FROM USER WHERE USER_ID = {userId}")
      .bindByName('userId -> id)
      .map(parseToEntity)

    Database.singleResult[User](query)
  }

  def save(user:User) = {

    val userId = UUID.randomUUID().toString

    val query = SQL("INSERT INTO USER(ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE) VALUES({userId}, {firstName}, {lastName}, {email}, {phone})")
      .bindByName('userId -> userId)
      .bindByName('firstName -> user.firstName)
      .bindByName('lastName -> user.lastName)
      .bindByName('email -> user.email)
      .bindByName('phone -> user.phone)
      .map(rs => rs.int(0))

    Database.update[Int](query)

    findById(userId)
  }

  def update(id:String, user:User) = {

    val query = SQL("UPDATE SET FIRST_NAME = {firstName}, LAST_NAME = {lastName}, EMAIL = {email}, PHONE={phone} WHERE ID = {userId}")
      .bindByName('userId -> id)
      .bindByName('firstName -> user.firstName)
      .bindByName('lastName -> user.lastName)
      .bindByName('email -> user.email)
      .bindByName('phone -> user.phone)
      .map(rs => rs.int(0))

    Database.update[Int](query)

    findById(id)
  }

  def delete(id:String) = {

    val query = SQL("DELETE FROM USER WHERE USER_ID = {userId}")
      .bindByName('userId -> id)
      .map(rs => rs.int(0))

    Database.update[Int](query)
  }
}
