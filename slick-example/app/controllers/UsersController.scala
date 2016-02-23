package controllers

import models.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import repositories.UserRepository

/**
  * Created by trodrigues on 17/02/16.
  */
class UsersController extends Controller {

  def list = Action.async {
    UserRepository
      .list()
      .map(users => Ok(Json.toJson(users)))
      .recover{ case t:Throwable => BadRequest(t.toString)}
  }

  def find(userId:Long) = Action.async {
    UserRepository
      .findById(userId)
      .map(user => Ok(Json.toJson(user)))
      .recover{ case t:Throwable => BadRequest("Cannot find user ")}
  }

  def create = Action.async(parse.json[User]) { implicit request =>

    val user = request.body

    UserRepository
      .insert(user)
      .map(_ => Created(s"Created user: ${user.email}"))
      .recover{ case t:Throwable => BadRequest(s"Cannot create user: ${user.email}. Exception: $t")}
  }

  def update(userId:Long) = Action.async(parse.json[User]) { implicit request =>

    val user = request.body

    UserRepository
      .update(userId, user)
      .map(_ => Created(s"Updated user: $userId"))
      .recover{ case t:Throwable => BadRequest(s"Cannot update user: $userId. Exception: $t")}
  }
}
