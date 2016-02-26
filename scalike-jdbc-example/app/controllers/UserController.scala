package controllers

import models.User
import repositories.UserRepository
import play.api._
import play.api.mvc._

/**
  * Created by trodrigues on 26/02/16.
  */
class UserController extends Controller {

  def list = Action {
    Ok(UserRepository.list)
  }

  def find(id:String) = Action {
    UserRepository.findById(id) match {
      case Some(u) => Ok(u)
      case _ => NotFound
    }
  }

  def create = Action(parse.json[User]) {
    Ok("Your new application is ready.")
  }

  def update(is:String) = Action(parse.json[User]) {
    Ok("Your new application is ready.")
  }

  def delete(is:String) = Action {
    Ok("Your new application is ready.")
  }
}
