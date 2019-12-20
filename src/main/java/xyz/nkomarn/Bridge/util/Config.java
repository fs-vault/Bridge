package xyz.nkomarn.Bridge.util;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static ConfigurationNode node;

    /**
     * Loads and saves the configuration
     * @throws IOException Exception during configuration saving
     */
    public static void loadConfig() throws IOException {
        final File configFile = new File("plugins/Kerosene/config.yml");
        configFile.getParentFile().mkdirs();
        final Path path = configFile.toPath();
        if (!Files.exists(path)) {
            try {
                try (InputStream is = Config.class.getClassLoader()
                        .getResourceAsStream("config.yml")) {
                    Files.copy(is, path);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ConfigurationLoader loader = YAMLConfigurationLoader.builder()
                .setURL(path.toUri().toURL()).build();
        node = loader.load();
    }

    /**
     * Fetches a node from the configuration
     * @param location Configuration location of the string
     */
    public static ConfigurationNode getNode(final String location) {
        return node.getNode(location);
    }
}
