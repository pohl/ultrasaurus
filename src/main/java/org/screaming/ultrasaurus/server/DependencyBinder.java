package org.screaming.ultrasaurus.server;

import static com.google.common.base.Throwables.propagate;
import static org.slf4j.LoggerFactory.getLogger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.screaming.ultrasaurus.data.WordDao;
import org.screaming.ultrasaurus.data.WordDaoCacheImpl;
import org.screaming.ultrasaurus.data.WordDaoImpl;
import org.screaming.ultrasaurus.data.WordService;
import org.slf4j.Logger;

/**
 * @author pohl_longsine
 */
public class DependencyBinder extends AbstractBinder {
    private static final Logger LOG = getLogger(DependencyBinder.class);

    @Override
    protected void configure() {
        LOG.info("configuring bindings...");
        bindFactory(WorkerPoolFactory.class).to(ExecutorService.class).in(Singleton.class);
        bind(ConfigSourceHoconImpl.class).to(ConfigSource.class).in(Singleton.class);
        bind(WordDaoImpl.class).to(WordDaoImpl.class).in(Singleton.class);
        bind(WordDaoCacheImpl.class).to(WordDao.class).in(Singleton.class);
        bind(WordService.class).to(WordService.class).in(Singleton.class);
    }

    private static class WorkerPoolFactory implements Factory<ExecutorService> {

        @Override
        public ExecutorService provide() {
            LOG.info("creating new worker pool...");
            return Executors.newFixedThreadPool(100);
        }
        
        @Override
        public void dispose(ExecutorService threadPool) {
            LOG.info("shutting down worker pool...");
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(2, TimeUnit.SECONDS)) {
                    LOG.warn("worker pool did not shut down cleanly.");
                    System.exit(0);
                }
            } catch (InterruptedException e) {
                propagate(e);
            }
        }
       
    }
}

