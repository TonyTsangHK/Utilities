package utils.string;

import java.util.ArrayList;
import java.util.Collection;

public class Synonyms {
    private ArrayList<String> synonyms;
    
    private String mostUsedWord = null;
    
    private boolean mostUsedWordSynonym = false;
    
    public Synonyms(String mostUsedWord, Collection<String> synonyms, boolean mostUsedWordSynonym) {
        this.synonyms = new ArrayList<String>(synonyms);
        if (!this.synonyms.contains(mostUsedWord)) {
            this.synonyms.add(mostUsedWord);
        }
        this.mostUsedWord = mostUsedWord;
        this.mostUsedWordSynonym = mostUsedWordSynonym;
    }
    
    public Synonyms(String mostUsedWord, boolean mostUsedWordSynonym, String ... synonyms) {
        this.synonyms = new ArrayList<String>();
        for (String syn : synonyms) {
            this.synonyms.add(syn);
        }
        if (!this.synonyms.contains(mostUsedWord)) {
            this.synonyms.add(mostUsedWord);
        }
        this.mostUsedWord = mostUsedWord;
        this.mostUsedWordSynonym = mostUsedWordSynonym;
    }
    
    public boolean isMostUsedWord(String word) {
        if (mostUsedWord != null) {
            return mostUsedWord.equals(word);
        } else {
            return false;
        }
    }
    
    public void setMostUsedWord(String word) {
        if (synonyms.contains(word)) {
            mostUsedWord = word;
        }
    }
    
    public String getMostUsedWord() {
        return mostUsedWord;
    }
    
    public boolean isSynonym(String word) {
        return StringUtil.stringMatchOnce(word, synonyms);
    }
    
    public ArrayList<String> getSynonyms() {
        return synonyms;
    }
    
    public ArrayList<String> getSynonyms(String word) {
        ArrayList<String> syns = new ArrayList<String>();
        if (isSynonym(word)) {
            for (String syn : synonyms) {
                if (!syn.equals(word)) {
                    syns.add(syn);
                }
            }
        }
        return syns;
    }
    
    public boolean isSynonyms(String word1, String word2) {
        if (mostUsedWordSynonym) {
            if (word1.equals(mostUsedWord)) {
                return isSynonym(word2);
            } else if (word2.equals(mostUsedWord)) {
                return isSynonym(word1);
            } else {
                return false;
            }
        } else {
            return isSynonym(word1) && isSynonym(word2);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof Synonyms) {
            Synonyms syns = (Synonyms) o;
            if (synonyms.size() == syns.synonyms.size()) {
                for (String syn : synonyms) {
                    boolean hasMatch = false;
                    for (String syn2 : syns.synonyms) {
                        if (syn2.equals(syn)) {
                            hasMatch = true;
                            break;
                        }
                    }
                    if (!hasMatch) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else if (o instanceof String) {
            return isSynonym((String)o);
        } else {
            return false;
        }
    }
}
