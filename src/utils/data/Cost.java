package utils.data;

public interface Cost extends Comparable<Cost>, Cloneable {
    public Cost add(Cost cost);
    public Cost minus(Cost cost);
    public Cost clone();
}
