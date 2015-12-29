package utils.search;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import utils.regex.WildcardToRegularExpression;

public class SearchMatcher {
    private SearchType searchType;
    private boolean ignoreCase;
    
    public SearchMatcher(SearchType searchType) {
        this(searchType, false);
    }
    
    public SearchMatcher(SearchType searchType, boolean ignoreCase) {
        this.searchType = searchType;
        this.ignoreCase = ignoreCase;
    }
    
    public void setSearchType(SearchType searchType) {
        if (searchType != null && this.searchType != searchType) {
            this.searchType = searchType;
        }
    }
    
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
    
    public boolean match(String searchKey, String text) {
        try {
            if (searchType == SearchType.TEXT) {
                if (ignoreCase) {
                    return searchKey.equalsIgnoreCase(text);
                } else {
                    return searchKey.equals(text);
                }
            } else {
                String regex = searchKey;
                int flag = Pattern.DOTALL;
                if (ignoreCase) {
                    flag |= Pattern.CASE_INSENSITIVE;
                }
                if (searchType == SearchType.WILD_CARD) {
                    regex = WildcardToRegularExpression.wildcardToRegex(searchKey);
                }
                
                return Pattern.compile(regex, flag).matcher(text).matches();
            }
        } catch (PatternSyntaxException pse) {
            return false;
        }
    }
}
