package models

import models.modelsunscanned.TableWithId
import slick.driver.H2Driver.api._
/**
  * Created by trodrigues on 2/20/16.
  */
class Users(tag:Tag) extends TableWithId[User](tag, "USERS") {

  def id = column[String]("id", O.PrimaryKey)
  def firstName = column[String]("FIRST_NAME")
  def lastName = column[String]("LAST_NAME")
  def phone = column[String]("PHONE")
  def email  = column[String]("EMAIL")
  def * = (id.?, firstName, lastName, phone, email) <> ((User.apply _).tupled, User.unapply)
}
