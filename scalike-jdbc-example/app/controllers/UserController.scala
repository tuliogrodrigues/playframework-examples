package controllers

import models.User
import play.api.libs.json.Json
import play.api.mvc._
import repositories.UserRepository

/**
  * Created by trodrigues on 26/02/16.
  */
class UserController extends Controller {

  def list = Action {
    Ok(Json.toJson(UserRepository.list))
  }

  def find(id:String) = Action {
    UserRepository.findById(id) match {
      case Some(u) => Ok(Json.toJson(u))
      case _ => NotFound
    }
  }

  def create = Action(parse.json[User]) { implicit request => {
    val user = request.body
    UserRepository.save(user) match {
      case Some(u) => Created(Json.toJson(u))
      case _ => BadRequest
    }
  }}

  def update(id:String) = Action(parse.json[User]) {implicit request => {
    val user = request.body
    UserRepository.update(id, user) match {
      case Some(u) => Ok(Json.toJson(u))
      case _ => BadRequest
    }
  }}

  def delete(id:String) = Action {
    UserRepository.delete(id) match {
      case count:Int if count > 0 => Ok("Entity deleted")
      case _ => NotFound
    }
  }
}
