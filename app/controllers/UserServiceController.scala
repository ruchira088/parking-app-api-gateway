package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class UserServiceController @Inject()(controllerComponents: ControllerComponents)
  extends AbstractController(controllerComponents)
{

}