package xyz.nkomarn.Bridge.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import org.slf4j.Logger;
import xyz.nkomarn.Bridge.api.BridgeAPI;

public class ProxyPingListener {
    private final ProxyServer proxy;
    private final Logger logger;

    public ProxyPingListener(final ProxyServer proxy, final Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyPing(final ProxyPingEvent event) {
        final ServerPing.Builder ping = event.getPing().asBuilder();
        ping.onlinePlayers(BridgeAPI.onlinePlayers.get());
        event.setPing(ping.build());
    }
}
