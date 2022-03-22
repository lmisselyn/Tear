package norswap.sigh.interpreter;

import java.util.Objects;

/*
    DataStructure to manage the Keys of the hashmap in FactStorage
    @name is the name of a fact
    @arity is the number of terms in a fact
 */
public class Pair {
    private String name;
    private Integer arity;

    public Pair(String name, Integer arity) {
        this.name = name;
        this.arity = arity;
    }

    public Integer getArity() {
        return arity;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "(" + name + ", " + arity + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(name, pair.name) && Objects.equals(arity, pair.arity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arity);
    }
}
