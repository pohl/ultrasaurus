package org.screaming.ultrasaurus.server;

import java.util.Optional;

/**
 * @author pohl_longsine
 */
public interface ConfigSource {

    Optional<String> getString(String path);

}
