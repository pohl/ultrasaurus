package org.screaming.ultrasaurus.server;

import static com.google.common.base.Throwables.propagate;
import static org.slf4j.LoggerFactory.getLogger;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.AbstractContainerLifecycleListener;
import org.glassfish.jersey.server.spi.Container;
import org.screaming.ultrasaurus.rest.WordResource;
import org.slf4j.Logger;

/**
 * The main Jersey application. This starts the dependency injection and exposes the single service.
 */
@ApplicationPath("/api/*")
public class Application extends ResourceConfig {
    private static final Logger LOG = getLogger(Application.class);

    @Inject
    public Application(ServiceLocator serviceLocator) {
        LOG.info("instantiated.");
        register(new DependencyBinder());
        register(new JacksonJsonProvider());
        register(new LifecycleListener());
        register(WordResource.class);
    }

    private static class LifecycleListener extends AbstractContainerLifecycleListener {

        @Override
        public void onStartup(Container container) {
            try {
                LOG.info("application started.");
            } catch (Throwable t) {
                LOG.error("exception during startup:", t);
                propagate(t);
            }
        }

        @Override
        public void onShutdown(Container container) {
            try {
                LOG.info("application stopped.");
            } catch (Throwable t) {
                LOG.error("exception during shutdown:", t);
                propagate(t);
            }
        }

    }
}