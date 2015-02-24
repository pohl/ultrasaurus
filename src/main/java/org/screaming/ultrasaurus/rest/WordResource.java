package org.screaming.ultrasaurus.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import com.google.common.base.Stopwatch;
import org.screaming.ultrasaurus.data.WordService;
import org.screaming.ultrasaurus.model.WordSearchResult;
import org.slf4j.Logger;

/**
 * The RESTful service that we expose to the browser.
 *
 */
@Singleton
@Path("/words/{word}")
public class WordResource {
    private static final Logger LOG = getLogger(WordResource.class);
    private WordService wordService;
    private ExecutorService workerPool;

    @Inject
    public WordResource(WordService wordService, ExecutorService workerPool) {
        this.wordService = wordService;
        this.workerPool = workerPool;
        LOG.info("instantiated.");
    }

    @GET
    @Produces(APPLICATION_JSON)
    public String getClichedMessage(@PathParam("word") String word,
                                    @Suspended AsyncResponse asyncResponse) throws Exception {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        CompletionStage<Optional<WordSearchResult>> future = wordService.getWordAsync(word);
        future.whenCompleteAsync((result, throwable) -> {
            if (throwable != null) {
                LOG.error("error getting info for " + word, throwable);
                asyncResponse.resume(throwable);
            } else {
                asyncResponse.resume(new WordSearchResponse(word, result));
                LOG.info("called: {} ({}ms)", word, stopwatch.elapsed(TimeUnit.MILLISECONDS));
            }
        }, workerPool);
        return null; // Jersey ignores this return value in Async case
    }
    
    private static class WordSearchResponse {
        private final boolean wasFound;
        private final WordSearchResult wordSearchResult;
        
        WordSearchResponse(String spelling, Optional<WordSearchResult> wsr) {
            wasFound = wsr.isPresent();
            wordSearchResult = wsr.orElse(createSearchResult(spelling));
        }

        private WordSearchResult createSearchResult(String spelling) {
            WordSearchResult.Builder b = WordSearchResult.builder(spelling);
            return b.build();        }

        public boolean isWasFound() {
            return wasFound;
        }

        public WordSearchResult getWordSearchResult() {
            return wordSearchResult;
        }
    }
}
