package org.screaming.ultrasaurus.data;

import static org.slf4j.LoggerFactory.getLogger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.screaming.ultrasaurus.model.WordSearchResult;
import org.slf4j.Logger;

/**
 * An implementation that uses a Guava LoadingCache to avoid dipping into the source service more than we have to.
 *
 * @author pohl_longsine
 */
@Singleton
public class WordDaoCacheImpl implements WordDao {
    private static final Logger LOG = getLogger(WordDaoCacheImpl.class);

    private final LoadingCache<String,Optional<WordSearchResult>> wordCache;

    @Inject
    public WordDaoCacheImpl(WordDaoImpl wordDao) {
        LOG.info("instantiated");
        this.wordCache = CacheBuilder.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS) // maximum allowed by https://www.wordsapi.com/terms_of_service
                .maximumSize(1000L)
                .build(new CacheLoader<String,Optional<WordSearchResult>>() {
                    @Override
                    public Optional<WordSearchResult> load(String s) throws Exception {
                        return wordDao.getWord(s);
                    }
                });
    }

    @Override
    public Optional<WordSearchResult> getWord(String word) {
        try {
            Optional<WordSearchResult> result = wordCache.get(word);
            if (!result.isPresent()) {
                wordCache.invalidate(word);
            }
            return result;
        } catch (ExecutionException e) {
            LOG.error("exception during cache lookup:", e);
        }
        return Optional.empty();
    }

}
