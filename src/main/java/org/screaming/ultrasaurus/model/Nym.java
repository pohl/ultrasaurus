package org.screaming.ultrasaurus.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author pohl_longsine
 */
public class Nym {
    
    private final String entry;
    private final String uriEncodedEntry;
    private final boolean isLinkable;
    
    public Nym(String entry) {
        this.entry = entry;
        this.uriEncodedEntry = uriEncode(entry);
        isLinkable = isLinkable(entry);
    }

    public String getEntry() {
        return entry;
    }

    public boolean isLinkable() {
        return isLinkable;
    }

    public String getUriEncodedEntry() {
        return uriEncodedEntry;
    }

    private static final boolean isLinkable(CharSequence seq) {
        int len = seq.length();
        for(int i=0;i<len;i++) {
            char c = seq.charAt(i);
            if('0'<=c && c<='9') continue;
            if('a'<=c && c<='z') continue;
            if('A'<=c && c<='Z') continue;
            if(c=='-') continue;
            return false;
        }
        return true;
    }


    private static final String uriEncode(String entry) {
        try {
            return URLEncoder.encode(entry, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%27", "'");
        } catch (UnsupportedEncodingException e) {
            return entry;
        }
    }


}
