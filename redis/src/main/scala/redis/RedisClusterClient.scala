package redis

import com.typesafe.config.Config
import redis.RedisClusterClient.Setting
import redis.clients.jedis.{HostAndPort, JedisCluster}

import scala.collection.JavaConverters._

case class RedisClusterClient(setting: Setting) {
  private[redis] val client = new JedisCluster(setting.nodes.asJava)

  def destroy(): Unit = client.close()
}

object RedisClusterClient {
  case class Setting(nodes: Set[HostAndPort])

  object Setting {
    def apply(config: Config): Setting = {
      Setting(nodes =
        config.getConfigList("nodes").asScala.map(c => new HostAndPort(c.getString("host"), c.getInt("port"))).toSet)
    }
  }
}
