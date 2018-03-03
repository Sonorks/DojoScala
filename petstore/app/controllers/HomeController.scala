package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.Pet




/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  var mascotas = List[Pet](
  Pet(1, "Neron", "Bulldog", "M", "Calle 48 No.48-12", "P"),
  Pet(2, "Bruno", "Beagle", "H", "Carrera 4 No.65-75", "E")
)

  def getMascotas = Action {
  val jsonAux = Json.toJson(mascotas) // Simplemente se toma la lista, se Jsifica
  Ok(jsonAux) // Y se retorna en la lista Jsificada
  }


  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
