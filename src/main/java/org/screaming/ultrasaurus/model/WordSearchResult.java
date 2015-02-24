package org.screaming.ultrasaurus.model;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableList;

/**
 * @author pohl_longsine
 */
public class WordSearchResult {
    private final String spelling;
    private final String pronunciation;
    private final ImmutableList<WordResult> wordResults;

    public WordSearchResult(Builder b) {
        this.spelling = b.spelling;
        this.pronunciation = b.pronunciation;
        this.wordResults = b.words.build();
    }

    public static Builder builder(String s) {
        return new Builder(s);
    }

    public ImmutableList<WordResult> getWordResults() {
        return wordResults;
    }

    public String getSpelling() {
        return spelling;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public static class Builder {
        private final String spelling;
        private String pronunciation;
        private ImmutableList.Builder<WordResult> words = ImmutableList.builder();

        private Builder(String s) {
            this.spelling = s;
        }

        public WordSearchResult build() {
            return new WordSearchResult(this);
        }

        public Builder withPronunciation(String pronunciation) {
            checkNotNull(pronunciation);
            this.pronunciation = pronunciation;
            return this;
        }

        public Builder withWord(WordResult w) {
            checkNotNull(w);
            this.words.add(w);
            return this;
        }
    }

}
