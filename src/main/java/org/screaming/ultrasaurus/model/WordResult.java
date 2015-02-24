package org.screaming.ultrasaurus.model;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableList;

/**
 * @author pohl_longsine
 */
public class WordResult {
    private final String definition;
    private final String partOfSpeech;
    private final ImmutableList<Nym> synonyms;
    private final ImmutableList<Nym> antonyms;
    private final ImmutableList<Nym> hypernyms;
    private final ImmutableList<Nym> hyponyms;
    private final ImmutableList<Nym> holonyms;
    
    public WordResult(Builder b) {
        this.definition = b.definition;
        this.partOfSpeech = b.partOfSpeech;
        this.synonyms = b.synonyms.build();
        this.antonyms = b.antonyms.build();
        this.hypernyms = b.hypernyms.build();
        this.hyponyms = b.hyponyms.build();
        this.holonyms = b.holonyms.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDefinition() {
        return definition;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public ImmutableList<Nym> getSynonyms() {
        return synonyms;
    }

    public ImmutableList<Nym> getAntonyms() {
        return antonyms;
    }

    public ImmutableList<Nym> getHypernyms() {
        return hypernyms;
    }

    public ImmutableList<Nym> getHyponyms() {
        return hyponyms;
    }

    public ImmutableList<Nym> getHolonyms() {
        return holonyms;
    }

    public static class Builder {
        private String definition;
        private String partOfSpeech;
        private ImmutableList.Builder<Nym> synonyms = ImmutableList.builder();
        private ImmutableList.Builder<Nym> antonyms = ImmutableList.builder();
        private ImmutableList.Builder<Nym> hypernyms = ImmutableList.builder();
        private ImmutableList.Builder<Nym> hyponyms = ImmutableList.builder();
        private ImmutableList.Builder<Nym> holonyms = ImmutableList.builder();

        private Builder() {
        }

        public WordResult build() {
            return new WordResult(this);
        }

        public Builder withDefinition(String definition) {
            checkNotNull(definition);
            this.definition = definition;
            return this;
        }

        public Builder withPartOfSpeech(String partOfSpeech) {
            checkNotNull(partOfSpeech);
            this.partOfSpeech = partOfSpeech;
            return this;
        }

        public Builder withSynonym(String s) {
            checkNotNull(s);
            this.synonyms.add(new Nym(s));
            return this;
        }

        public Builder withAntonym(String s) {
            checkNotNull(s);
            this.antonyms.add(new Nym(s));
            return this;
        }

        public Builder withHypernym(String s) {
            checkNotNull(s);
            this.hypernyms.add(new Nym(s));
            return this;
        }

        public Builder withHyponym(String s) {
            checkNotNull(s);
            this.hyponyms.add(new Nym(s));
            return this;
        }

        public Builder withHolonym(String s) {
            checkNotNull(s);
            this.holonyms.add(new Nym(s));
            return this;
        }

    }

}
