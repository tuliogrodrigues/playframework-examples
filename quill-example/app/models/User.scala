package models

import io.getquill._
import play.api.libs.json._

/**
  * Created by trodrigues on 23/02/16.
  */
case class User(id:Long, firstName:String, lastName:String, email:String, phone:String)

object User {

  implicit val userReads = Reads[User]
  implicit val userWrites = Writes[User]

  lazy val db = source(new SqlMirrorSourceConfig("testSource"))

  def list = db.run(query[User]).extractor

  def findById(id:Long) = db.run(query[User].filter(u => u.id == id)).extractor

  def create(user:User) = db.run(query[User].insert(user)) -> User

  def update(user:User) = db.run(query[User].update(user))
}