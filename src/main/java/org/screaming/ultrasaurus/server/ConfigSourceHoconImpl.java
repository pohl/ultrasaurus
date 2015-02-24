package org.screaming.ultrasaurus.server;

import static com.google.common.base.Throwables.propagate;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

/**
 * @author pohl_longsine
 */
@Singleton
public class ConfigSourceHoconImpl implements ConfigSource {
    private Config config;

    @Inject
    public ConfigSourceHoconImpl() {
        Config root = ConfigFactory.load();
        this.config = root;
    }

    @Override
    public Optional<String> getString(String path) {
        try {
            return Optional.of(config.getString(path));
        } catch (ConfigException.Missing ignored) {
        } catch (ConfigException.WrongType e) {
            propagate(e);
        }
        return Optional.empty();
    }

}
