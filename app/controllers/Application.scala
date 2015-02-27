package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Social Graph API"))
  }

  def login = TODO

}