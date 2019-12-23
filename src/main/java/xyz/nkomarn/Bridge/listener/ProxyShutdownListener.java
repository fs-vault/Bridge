package xyz.nkomarn.Bridge.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import xyz.nkomarn.Bridge.api.BridgeAPI;
import xyz.nkomarn.Kerosene.database.redis.RedisDatabase;

public class ProxyShutdownListener {
    private final ProxyServer proxy;
    private final Logger logger;

    public ProxyShutdownListener(final ProxyServer proxy, final Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyShutdown(final ProxyShutdownEvent event) {
        logger.info("Terminating Bridge.");
        try (final Jedis jedis = RedisDatabase.getResource()) {
            jedis.del(BridgeAPI.PROXY_KEY);
        }
        BridgeAPI.destroy();
    }
}
