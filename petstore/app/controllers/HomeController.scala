package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.Pet




@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  var mascotas = List[Pet](
  Pet(1, "Neron", "Bulldog", "M", "Calle 48 No.48-12", "P"),
  Pet(2, "Bruno", "Beagle", "H", "Carrera 4 No.65-75", "E")
)

  def getMascotas = Action {
  val jsonAux = Json.toJson(mascotas) 
  Ok(jsonAux) 
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def removerMascota(id: Int) = Action {
  mascotas.find(_.id == id) match {
    case Some(m) => mascotas = mascotas.filter(x => x.id != id)
                    Ok("La mascota ha sido eliminada exitosamente!")
    case _ => Ok("La mascota indicada no existe o ya fue eliminada!")
    }    
  }

  def actualizarMascota = Action { implicit request =>
  val cuerpoJson = request.body.asJson.get // En primer lugar se recupera el cuerpo del mensaje el cual debe contener el json con la información a actualizar

  // Luego, se valida que lo que obtuve si es un json que corresponda con un objeto tipo Pet
  cuerpoJson.validate[Pet] match {
    // En caso de éxito entonces
    case success: JsSuccess[Pet] =>

      // Creo un nuevo objeto mascota a partir de la información que me llego
      var nuevaMascota = Pet(success.get.id, success.get.name, success.get.kind, success.get.gender, success.get.location, success.get.state)

      // Ahora, se busca que la mascota a actualizar si exista, por lo que...
      mascotas.find(_.id == success.get.id) match {
        // Si de verdad existe entonces intercambio la mascota que acabo de crear con la mascota que tiene el mismo id
        case Some(m) => mascotas = mascotas.map(aux => if (aux.id == success.get.id) nuevaMascota else aux)
                        Ok("La mascota ha sido actualizada exitosamente!")
        // Sino entonces retorno un mensaje al respecto
        case _ => Ok("No se puede actualizar una mascota que no existe!")
      }
    // En caso de error entonces devuelvo un mensajito
    case e:JsError => BadRequest("No se pudo actualizar porque hay malos parametros!!")
    }
  }


  def insertarMascota = Action { implicit request =>
  val cuerpoJson = request.body.asJson.get // En primer lugar se recupera el cuerpo del mensaje el cual debe contener el json con la información de la mascota a ingresar a la lista

  // Luego, se valida que lo que obtuve si es un json que corresponda con un objeto tipo Pet
  cuerpoJson.validate[Pet] match {
    // En caso de exito entonces
    case success: JsSuccess[Pet] =>

      // Creó un nuevo objeto mascota a partir de la información que me llego
      var nuevaMascota = Pet(success.get.id, success.get.name, success.get.kind, success.get.gender, success.get.location, success.get.state)

      // Ahora, se busca que la mascota a insertar no repita id, por lo que...
      mascotas.find(_.id == success.get.id) match {
        // Si ya hay una mascota con tal id entonces se muestra un mensaje al respecto
        case Some(m) => Ok("No se puede insertar la mascota porque la clave indicada ya existe")
        // sino entonces se inserta la mascota y se muestra un mensaje de exito
        case _ =>  mascotas = mascotas :+ nuevaMascota
                   Ok("La mascota ha sido ingresada exitosamente!")
      }

    // En caso de error entonces devuelvo un mensajito
    case e:JsError => BadRequest("No se pudo actualizar porque hay malos parametros!!")
    }
  }

}


