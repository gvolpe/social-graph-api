package auth.repository.redis

import scredis._

object RedisConnectionManager {

  def connection: Redis = Redis() //("scredis.conf", "scredis")

  //  def closeConnection(redis: Redis): Unit = {
  //    // Shutdown all initialized internal clients along with the ActorSystem
  //    redis.get("whatever") map { _ => redis.quit }
  //  }

}
