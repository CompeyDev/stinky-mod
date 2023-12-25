package xyz.devcomp.config;

import java.io.File;
import java.io.IOException;

import xyz.devcomp.Stinky;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.*;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigHandler {
    ConfigModel model;

    public ConfigHandler() {
        File config = FabricLoader.getInstance().getConfigDir().resolve("stinky.yaml").toFile();
        
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException e) {
                Stinky.LOGGER.error("Failed to create config file stinky.yaml. Consider retrying.");
                e.printStackTrace();
                System.exit(1);
            }
            
            Stinky.LOGGER.info("Created config file, stinky.yaml in config directory. Please edit the config file and restart Minecraft.");
            System.exit(0);
        } else {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try {
                this.model = mapper.readValue(config, ConfigModel.class);
            } catch (IOException e) {
                Stinky.LOGGER.error("Invalid configuration, failed to parse as YAML.");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public ConfigModel getConfig() {
        return this.model;
    }
}
