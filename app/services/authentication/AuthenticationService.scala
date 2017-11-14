package services.authentication

import javax.inject.{Inject, Singleton}

import com.myob.models.User
import com.myob.utils.GeneralUtils.generateUuid
import com.myob.utils.ScalaUtils
import exceptions.{InvalidAuthenticationTokenException, KeyValueStoreInsertionException}
import org.joda.time.DateTime
import services.kvstore.KeyValueStore

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthenticationService @Inject()(keyValueStore: KeyValueStore)(implicit executionContext: ExecutionContext)
{
  def insert(user: User): Future[AuthToken] =
  {
    val authToken = AuthToken(user, generateUuid(), DateTime.now())

    for {
      result <- keyValueStore.insert(authToken.tokenString, authToken)

      _ <- ScalaUtils.predicate(result, KeyValueStoreInsertionException())
    }
    yield authToken
  }

  def verify(tokenString: String): Future[AuthToken] =
    keyValueStore.get[AuthToken](tokenString).flatten(InvalidAuthenticationTokenException)

}
