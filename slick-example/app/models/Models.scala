package models


import play.api.libs.json._


/**
  * Created by trodrigues on 2/14/16.
  */

trait Entity {
  val id: Option[Long]
}

case class User(
                 id: Option[Long],
                 firstName: String,
                 lastName: String,
                 phone:String,
                 email:String
               ) extends Entity {
}

object User {
  implicit val userReads = Json.reads[User]
  implicit val userWrites = Json.writes[User]
}

