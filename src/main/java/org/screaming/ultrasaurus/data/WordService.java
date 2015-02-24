package org.screaming.ultrasaurus.data;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.screaming.ultrasaurus.model.WordResult;
import org.screaming.ultrasaurus.model.WordSearchResult;

/**
 * A wrapper that provides an asynchronous interface for fetching word data, and a couple of easter eggs.
 *
 * @author pohl_longsine
 */
@Singleton
public class WordService {

    private static final WordSearchResult ULTRASAURUS = createUltrasaurus();
    private static final WordSearchResult HUDL = createHudl();

    private WordDao wordDao;
    private ExecutorService threadPool;

    @Inject
    WordService(WordDao wordDao, ExecutorService threadPool) {
        this.wordDao = wordDao;
        this.threadPool = threadPool;
    }

    public CompletionStage<Optional<WordSearchResult>> getWordAsync(String word) {
        final CompletableFuture<Optional<WordSearchResult>> future = CompletableFuture.supplyAsync(() -> {
            if (word.equalsIgnoreCase("ultrasaurus")) {
                return Optional.of(ULTRASAURUS);
            } else if (word.equalsIgnoreCase("hudl")) {
                return Optional.of(HUDL);
            } else {
                return wordDao.getWord(word);
            }
        }, threadPool);
        return future;
    }

    private static final WordSearchResult createUltrasaurus() {
        WordSearchResult.Builder b = WordSearchResult.builder("ultrasaurus");
        b.withPronunciation("'əltrəsɔrəs");
        WordResult.Builder site = WordResult.builder();
        site.withPartOfSpeech("noun");
        site.withDefinition("a web site containing a classified list of synonyms, antonyms, hypernyms, hyponyms, and holonyms.");
        site.withSynonym("thesaurus");
        b.withWord(site.build());
        WordResult.Builder dinosaur = WordResult.builder();
        dinosaur.withPartOfSpeech("noun");
        dinosaur.withDefinition("huge quadrupedal herbivorous dinosaur common in Asia in the early Cretaceous");
        dinosaur.withHypernym("sauropod");
        b.withWord(dinosaur.build());
        return b.build();
    }

    private static final WordSearchResult createHudl() {
        WordSearchResult.Builder b = WordSearchResult.builder("hudl");
        b.withPronunciation("'hədəl");
        WordResult.Builder hudl = WordResult.builder();
        hudl.withPartOfSpeech("noun");
        hudl.withDefinition("Hudl is a product and service of Agile Sports Technologies, Inc. - a Lincoln, Nebraska based company providing tools for coaches and athletes to review game footage to improve team play.");
        b.withWord(hudl.build());
        return b.build();
    }
    
}
