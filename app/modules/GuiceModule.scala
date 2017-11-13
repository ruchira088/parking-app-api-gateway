package modules

import com.google.inject.AbstractModule
import services.user.{UserService, UserServiceImpl}

class GuiceModule extends AbstractModule
{
  override def configure() =
  {
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
  }
}
