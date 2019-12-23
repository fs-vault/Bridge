package xyz.nkomarn.Bridge;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import xyz.nkomarn.Bridge.api.BridgeAPI;
import xyz.nkomarn.Bridge.command.NetworkListCommand;
import xyz.nkomarn.Bridge.command.PingCommand;
import xyz.nkomarn.Bridge.handler.JedisPubSubHandler;
import xyz.nkomarn.Bridge.listener.*;
import xyz.nkomarn.Kerosene.database.redis.RedisDatabase;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Plugin(id = "bridge", name = "Bridge", version = "1.0",
    description = "A multi-proxy connector for Velocity!",
    authors = {"TechToolbox (@nkomarn)"},
    dependencies = {@Dependency(id = "kerosene")})
public class Bridge {
    private final ProxyServer proxy;
    private final Logger logger;

    @Inject
    public Bridge(final ProxyServer proxy, final Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        logger.info("Initializing Bridge.");

        // Register events
        final EventManager eventManager = proxy.getEventManager();
        eventManager.register(this, new ProxyShutdownListener(proxy, logger));
        eventManager.register(this, new ProxyPingListener(proxy, logger));
        eventManager.register(this, new PlayerLoginListener(proxy, logger));
        eventManager.register(this, new PlayerDisconnectListener(proxy, logger));
        eventManager.register(this, new PubSubMessageListener(proxy, logger));

        // Register commands
        final CommandManager commandManager = proxy.getCommandManager();
        commandManager.register(new NetworkListCommand(), "networklist");
        commandManager.register(new PingCommand(), "ping");

        // Initialize Bridge API
        new BridgeAPI(new JedisPubSubHandler(proxy, logger));

        // Clear proxy key in case of previous crashes
        proxy.getScheduler().buildTask(this, () -> {
            try (final Jedis jedis = RedisDatabase.getResource()) {
                jedis.del(BridgeAPI.PROXY_KEY);
            }
        }).delay(5, TimeUnit.SECONDS).schedule();

        // Schedule player count updates
        proxy.getScheduler().buildTask(this, () -> {
            final AtomicInteger count = new AtomicInteger(0);
            try (final Jedis jedis = RedisDatabase.getResource()) {
                jedis.keys("proxy:*:online").stream().parallel().forEach(key ->
                        count.addAndGet(Math.toIntExact(jedis.scard(key))));
            }
            BridgeAPI.onlinePlayers = count;
        }).delay(5, TimeUnit.SECONDS).repeat(10, TimeUnit.SECONDS).schedule();
    }
}
