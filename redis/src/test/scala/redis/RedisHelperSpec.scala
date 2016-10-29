package redis

import org.scalatest.FlatSpec

class RedisHelperSpec extends FlatSpec with RedisHelper {
  "RedisHelper" should "set key/value" in {
    redis.client.set("x", "y")
    assert(Option(redis.client.get("x")) === Some("y"))
  }

  "RedisHelper" should "flushAll afterEach" in {
    assert(Option(redis.client.get("x")) === None)
  }
}
