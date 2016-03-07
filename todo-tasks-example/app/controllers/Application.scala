package controllers

import java.util.UUID

import com.google.inject.Inject
import models.Task
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val taskForm = Form(
    mapping(
      "id" -> default(text, UUID.randomUUID().toString),
      "title" -> text,
      "description" -> text
    )(Task.apply)(Task.unapply)
  )

  def index = Action.async {

    for {
      result <- Task.list("#tasks")
    } yield {
      Ok(views.html.index("TO-DO Tasks", taskForm, result.values.toList))
    }
  }

  def save = Action.async(parse.form(taskForm)) { implicit  request => {

    for {
      result <- Task.store("#tasks", request.body)
    } yield {
      result match {
        case Some(v) => Redirect(routes.Application.index)
        case _ => BadRequest
      }
    }
  }}

  def remove = Action.async {  implicit  request => {

    val taskIdList = request.body.asFormUrlEncoded match {
      case Some(r) => r.toMap[String, Seq[String]]
      case _ => Map.empty[String, Seq[String]]
    }

    for {
      results <- Task.removeAll("#tasks", taskIdList("tasks"))
    } yield {
      Redirect(routes.Application.index)
    }
  }}
}
