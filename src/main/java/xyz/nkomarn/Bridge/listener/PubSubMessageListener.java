package xyz.nkomarn.Bridge.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import xyz.nkomarn.Bridge.event.PubSubMessageEvent;

public class PubSubMessageListener {
    private final ProxyServer proxy;
    private final Logger logger;

    public PubSubMessageListener(final ProxyServer proxy, final Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onPubSubMessage(final PubSubMessageEvent event) {

    }
}
