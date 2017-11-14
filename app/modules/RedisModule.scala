package modules

import akka.actor.ActorSystem
import com.google.inject.AbstractModule
import com.myob.utils.ConfigUtils.getEnvValueAsTry
import com.myob.utils.ScalaUtils
import constants.EnvValueNames
import redis.RedisClient

import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._

class RedisModule extends AbstractModule
{
  private def redisConfiguration(): Try[(String, Int)] = for
    {
      redisHost <- getEnvValueAsTry(EnvValueNames.REDIS_HOST)

      redisPortString <- getEnvValueAsTry(EnvValueNames.REDIS_PORT)
      redisPort <- ScalaUtils.convert[String, Int](_.toInt)(redisPortString)
    }
    yield (redisHost, redisPort)

  override def configure() =
  {
    implicit val actorSystem: ActorSystem = ActorSystem("redis-actor-system")
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val response: Future[RedisClient] = for {
      (redisHost, redisPort) <- Future.fromTry(redisConfiguration())

      redisClient = RedisClient(host = redisHost, port = redisPort)

      pong <- redisClient.ping()

      consoleMessage =
        s"""$redisClient replied with $pong
           |Connection to the Redis key-value store has been verified.
         """.stripMargin

      _ = println(consoleMessage)
        }
      yield redisClient

    response.onComplete {

      case Success(redisClient) =>
        bind(classOf[RedisClient]).toInstance(redisClient)

      case Failure(throwable) => {
        System.err.println(throwable)
      }
    }

    Await.ready(response, 30 seconds)
  }
}
