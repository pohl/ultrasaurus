package org.screaming.ultrasaurus.data;

import java.util.Optional;
import org.screaming.ultrasaurus.model.WordSearchResult;

/**
 * 
 * An interface for blocking access to information about words.
 *
 * @author pohl_longsine
 */
public interface WordDao {
    
    Optional<WordSearchResult> getWord(String word);
}
