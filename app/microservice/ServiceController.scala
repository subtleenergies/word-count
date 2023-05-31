package microservice

import play.api.mvc._

import javax.inject.Inject

class ServiceController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action {
    Ok("It works!")
  }
}