package xyz.nkomarn.Bridge.task;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import xyz.nkomarn.Bridge.Bridge;
import xyz.nkomarn.Kerosene.database.redis.RedisDatabase;
import xyz.nkomarn.Kerosene.velocity.Kerosene;

import java.util.List;

public class Heartbeat implements Runnable {
    @Override
    public void run() {
        try (final Jedis jedis = RedisDatabase.getResource()) {
            final long redisTime = getRedisTime(jedis.time());
            jedis.hset("heartbeats", Kerosene.getServerId(),
                    String.valueOf(redisTime));
            Bridge.getLogger().info("Updated heartbeat.");
        } catch (JedisConnectionException connectionException) {
            Bridge.getLogger().error("Unable to update heartbeat- Redis might be down.", connectionException);
            return;
        }

        // TODO get servers ids
    }

    private long getRedisTime(final List<String> timeResource) {
        return Long.parseLong(timeResource.get(0));
    }
}
