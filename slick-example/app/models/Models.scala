package models


import play.api.libs.functional.syntax._
import play.api.libs.json._


/**
  * Created by trodrigues on 2/14/16.
  */

case class User(
                 id: Option[String],
                 firstName: String,
                 lastName: String,
                 phone:String,
                 email:String
               ) extends Entity {
}

object User {
  implicit val userReads = (
      (__ \ "id").readNullable[String] and
      (__ \ "firstName").read[String] and
      (__ \ "lastName").read[String] and
      (__ \ "phone").read[String] and
      (__ \ "email").read[String]
    ) (User.apply _)

  implicit val userWrites = Json.writes[User]
}

trait Entity {
  val id: Option[String]
}

