package xyz.nkomarn.Bridge.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import xyz.nkomarn.Bridge.api.BridgeAPI;
import xyz.nkomarn.Kerosene.database.redis.RedisDatabase;

public class PlayerDisconnectListener {
    private final ProxyServer proxy;
    private final Logger logger;

    public PlayerDisconnectListener(final ProxyServer proxy, final Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onPlayerDisconnect(final DisconnectEvent event) {
        try (final Jedis jedis = RedisDatabase.getResource()) {
            jedis.srem(BridgeAPI.PROXY_KEY, event.getPlayer().getUniqueId().toString());
        }
    }
}
