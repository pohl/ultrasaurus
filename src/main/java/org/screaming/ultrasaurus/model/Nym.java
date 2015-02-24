package org.screaming.ultrasaurus.model;

/**
 * @author pohl_longsine
 */
public class Nym {
    
    private final String entry;
    private final boolean isLinkable;
    
    public Nym(String entry) {
        this.entry = entry;
        isLinkable = isLinkable(entry);
    }

    public String getEntry() {
        return entry;
    }

    public boolean isLinkable() {
        return isLinkable;
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
}
