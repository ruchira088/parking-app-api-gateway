package services.kvstore

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import com.myob.types.FutureO
import com.myob.types.FutureO.fromTry
import com.myob.utils.JsonUtils
import play.api.libs.json.{Json, Reads, Writes}
import redis.RedisClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RedisKeyValueStore @Inject()(redisClient: RedisClient)
            (implicit executionContext: ExecutionContext, actorSystem: ActorSystem) extends KeyValueStore
{
  override def insert[A](key: String, value: A)(implicit writes: Writes[A]): Future[Boolean] =
    redisClient.set[String](key, Json.toJson(value).as[String])

  override def get[A](key: String)(implicit reads: Reads[A]): FutureO[A] = for
    {
      jsonString <- FutureO(redisClient.get[String](key))

      json <- JsonUtils.parse(jsonString)

      value <- JsonUtils.deserialize[A](json)
    }
    yield value

  override def delete(keys: String*): Future[Long] = redisClient.del(keys: _*)
}
