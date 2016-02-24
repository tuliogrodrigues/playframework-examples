package controllers

import models.User
import play.api.mvc._

class Application extends Controller {

  def list = Action {
    User.list match {
      case u: User => Ok(u)
      case _ => BadRequest("Cannot find users ")
    }
  }

  def findById(id: Long) = Action {
    User.findById(id) match {
      case u: User => Ok(u)
      case _ => BadRequest("Cannot find users ")
    }
  }

  def create = Action(parse.json[User]) { implicit request => {

    val user = request.body
    Created("Ok")
//    User.create(user) match {
//      case (_, u:User.type) => Created(u)
//      case _ => BadRequest("Cannot create users ")
//    }
  }}
}
