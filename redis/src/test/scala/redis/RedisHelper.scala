package redis

import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}
import redis.RedisClusterClient.Setting
import scala.collection.JavaConverters._

trait RedisHelper extends Suite with BeforeAndAfterEach with BeforeAndAfterAll {
  protected val redis = RedisClusterClient(Setting(ConfigFactory.load().getConfig("redis")))

  override protected def afterEach(): Unit = {
    super.afterEach()

    redis.client.getClusterNodes.asScala.foreach { case (_, node) =>
      val jedis = node.getResource
      try {
        if (jedis.info().contains("role:master")) {
          jedis.flushAll()
        }
      } finally {
        jedis.close()
      }
    }
  }

  override protected def afterAll(): Unit = {
    super.afterAll()

    redis.destroy()
  }
}
