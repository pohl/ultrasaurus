package org.screaming.ultrasaurus.data;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.slf4j.LoggerFactory.getLogger;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.screaming.ultrasaurus.model.WordResult;
import org.screaming.ultrasaurus.model.WordSearchResult;
import org.screaming.ultrasaurus.server.ConfigSource;
import org.slf4j.Logger;

/**
 * An implementation that uses Unirest to fetch data from WordsApi.com.
 *
 * @author pohl_longsine
 */
@Singleton
public class WordDaoImpl implements WordDao {
    private static final Logger LOG = getLogger(WordDaoImpl.class);
    private ConfigSource configSource;

    @Inject
    WordDaoImpl(ConfigSource configSource) {
        this.configSource = configSource;
        LOG.info("instantiated.");
    }

    @Override
    public Optional<WordSearchResult> getWord(String word) {
        LOG.info("fetching word info...");
        String serviceUrl = configSource.getString("wordsapi.url").orElseThrow(IllegalStateException::new);
        String serviceKey = configSource.getString("wordsapi.key").orElseThrow(IllegalStateException::new);
        try {
            HttpResponse<JsonNode> response = Unirest.get(serviceUrl)
                    .header("X-Mashape-Key", serviceKey)
                    .header("Accept", APPLICATION_JSON)
                    .routeParam("word", word)
                    .asJson();
            return transformJsonToResult(response.getBody());
        } catch (UnirestException ignored) {
            /*
             * The mashape service proxy returns an HTML document when wordsapi does not have an entry. We eat
             * this exception for the time being, since the Unirest API does not appear to have a good way
             * to detect this case before trying to parse the response as JSON.
             */
            //LOG.warn("exception while fetching from mashape.com:", e);
        }
        return Optional.empty();
    }

    private Optional<WordSearchResult> transformJsonToResult(JsonNode body) {
        JSONObject word = body.getObject();
        WordSearchResult.Builder wsrb = WordSearchResult.builder(word.getString("word"));
        Optional<JSONObject> pronunciation = getJSONObject(word,"pronunciation");
        if (pronunciation.isPresent())  {
            wsrb.withPronunciation(pronunciation.get().getString("all"));
        }
        Optional<JSONArray> results = getJSONArray(word,"results");
        if (results.isPresent()) {
            buildResults(wsrb, results.get());
            return Optional.of(wsrb.build());
        } else {
            return Optional.empty();
        }
    }

    private void buildResults(WordSearchResult.Builder wsrb, JSONArray results) {
        for (int i = 0; i < results.length(); i++) {
            JSONObject wordResult = results.getJSONObject(i);
            WordResult.Builder wrb = WordResult.builder();
            wrb.withDefinition(wordResult.getString("definition"));
            Optional<String> pos = getStringField(wordResult, "partOfSpeech");
            if (pos.isPresent()) {
                wrb.withPartOfSpeech(pos.get());
            }
            buildSynonyms(wordResult, wrb);
            buildAntonyms(wordResult, wrb);
            buildHypernyms(wordResult, wrb);
            buildHyponyms(wordResult, wrb);
            buildHolonyms(wordResult, wrb);
            wsrb.withWord(wrb.build());
        }
    }

    private void buildSynonyms(JSONObject wordResult, WordResult.Builder wrb) {
        if (wordResult.has("synonyms")) {
            JSONArray nyms = wordResult.getJSONArray("synonyms");
            for (int k = 0; k < nyms.length(); k++) {
                String s = nyms.getString(k);
                wrb.withSynonym(s);
            }
        }
    }

    private void buildAntonyms(JSONObject wordResult, WordResult.Builder wrb) {
        if (wordResult.has("antonyms")) {
            JSONArray nyms = wordResult.getJSONArray("antonyms");
            for (int k = 0; k < nyms.length(); k++) {
                String s = nyms.getString(k);
                wrb.withAntonym(s);
            }
        }
    }

    private void buildHypernyms(JSONObject wordResult, WordResult.Builder wrb) {
        if (wordResult.has("typeOf")) {
            JSONArray nyms = wordResult.getJSONArray("typeOf");
            for (int k = 0; k < nyms.length(); k++) {
                String s = nyms.getString(k);
                wrb.withHypernym(s);
            }
        }
    }

    private void buildHyponyms(JSONObject wordResult, WordResult.Builder wrb) {
        if (wordResult.has("hasTypes")) {
            JSONArray nyms = wordResult.getJSONArray("hasTypes");
            for (int k = 0; k < nyms.length(); k++) {
                String s = nyms.getString(k);
                wrb.withHyponym(s);
            }
        }
    }

    private void buildHolonyms(JSONObject wordResult, WordResult.Builder wrb) {
        if (wordResult.has("partOf")) {
            JSONArray nyms = wordResult.getJSONArray("partOf");
            for (int k = 0; k < nyms.length(); k++) {
                String s = nyms.getString(k);
                wrb.withHolonym(s);
            }
        }
    }
    
    private static final Optional<String> getStringField(JSONObject object, String field) {
        if (object.has(field)) {
            Object o = object.get(field);
            if (o != null && o instanceof String) {
                return Optional.of((String)o);
            }
        }
        return Optional.empty();
    }

    private static final Optional<JSONObject> getJSONObject(JSONObject object, String field) {
        if (object.has(field)) {
            Object o = object.get(field);
            if (o != null && o instanceof JSONObject) {
                return Optional.of((JSONObject)o);
            }
        }
        return Optional.empty();
    }

    private static final Optional<JSONArray> getJSONArray(JSONObject object, String field) {
        if (object.has(field)) {
            Object o = object.get(field);
            if (o != null && o instanceof JSONArray) {
                return Optional.of((JSONArray)o);
            }
        }
        return Optional.empty();
    }
}
