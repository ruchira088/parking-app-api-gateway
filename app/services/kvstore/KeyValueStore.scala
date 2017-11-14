package services.kvstore

import com.myob.types.FutureO
import play.api.libs.json.{Reads, Writes}

import scala.concurrent.Future

trait KeyValueStore
{
  def insert[A](key: String, value: A)(implicit writes: Writes[A]): Future[Boolean]

  def get[A](key: String)(implicit reads: Reads[A]): FutureO[A]

  def delete(keys: String*): Future[Long]
}
