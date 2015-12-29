package utils.search;

public enum SearchType {
    WILD_CARD("wildcard"), REGULAR_EXPRESSION("regularExpression"), TEXT("text");
    
    public final String desc;
    
    private SearchType(String desc) {
        this.desc = desc;
    }
    
    @Override
    public String toString() {
        return desc;
    }
}
