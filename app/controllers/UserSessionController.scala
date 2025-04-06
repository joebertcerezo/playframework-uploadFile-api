package controllers

import javax.inject.*

import play.api.mvc.* 

@Singleton
class UserSessionController @Inject()(
  val cc: ControllerComponents

) extends AbstractController(cc){
  
}