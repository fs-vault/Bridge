package xyz.nkomarn.Bridge;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;
import xyz.nkomarn.Bridge.listener.PubSubMessageHandler;
import xyz.nkomarn.Bridge.task.Heartbeat;
import xyz.nkomarn.Bridge.util.Config;
import xyz.nkomarn.Kerosene.database.redis.RedisDatabase;
import xyz.nkomarn.Kerosene.velocity.Kerosene;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Plugin(id = "bridge", name = "Bridge", version = "1.0",
    description = "Firestarter's RedisBungee alternative for Velocity!",
    authors = {"TechToolbox (@nkomarn)"},
    dependencies = {@Dependency(id = "kerosene")})
public class Bridge {
    private static Bridge bridge;
    private static ProxyServer server;
    private static Logger logger;

    @Inject
    public Bridge(final ProxyServer server, final Logger logger) throws IOException {
        bridge = this;
        Bridge.server = server;
        Bridge.logger = logger;
        Config.loadConfig();
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        server.getScheduler().buildTask(getInstance(), () -> {
            System.out.println("Running");
            try (final Jedis jedis = RedisDatabase.getResource()) {
                final PubSubMessageHandler handler;
                handler = new PubSubMessageHandler();
                jedis.subscribe(handler, Arrays.asList("bridge-data",
                        String.format("bridge-%s", Kerosene.getServerId()))
                        .toArray(new String[0]));
                logger.info("Subscribed to Pub/Sub channels.");
            }
        }).delay(1, TimeUnit.SECONDS).schedule();

        server.getScheduler().buildTask(getInstance(),
                new Heartbeat()).repeat(3, TimeUnit.SECONDS).schedule();
    }

    public static Bridge getInstance() {
        return bridge;
    }

    public static ProxyServer getProxy() {
        return server;
    }

    public static Logger getLogger() {
        return logger;
    }
}
