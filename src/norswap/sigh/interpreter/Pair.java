package norswap.sigh.interpreter;

import java.util.Objects;

/**
    DataStructure to manage the Keys of the hashmap in FactStorage
    @succeed is the name of a fact
    @Bounds is the number of terms in a fact
 */
public class Pair {
    private Object succeed;
    private Object Bounds;

    public Pair(Object succeed, Object bounds) {
        this.succeed = succeed;
        this.Bounds = bounds;
    }

    public Object getBounds() {
        return Bounds;
    }

    public Object getSucceed() {
        return succeed;
    }

    @Override
    public String toString() {
        return "(" + succeed + ", " + Bounds + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(succeed, pair.succeed) && Objects.equals(Bounds, pair.Bounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(succeed, Bounds);
    }
}
