package models

import play.api.libs.json.Json

/**
  * Created by trodrigues on 26/02/16.
  */
case class User(id:Option[String], firstName:String, lastName:String, email:String, phone:String)

object User {

  implicit val userReads = Json.reads[User]
  implicit val userWrites = Json.writes[User]

}