package norswap.sigh.interpreter;
import java.util.HashMap;

public class FactStorage {

    HashMap<Pair, Fact> struct;

    @SuppressWarnings("unchecked")
    public FactStorage(){
        struct = new HashMap<>();
    }

    public Null newFact(Fact fact) {
        Pair pair = new Pair(fact.getName(), fact.getArity());
        struct.put(pair, fact);
        return null;
    }

    public Fact getFact(Pair pair) {
        return struct.get(pair);
    }

    public boolean contains(Pair pair) {
        return struct.containsKey(pair);
    }

    @Override
    public String toString() {
        return struct.toString();
    }
}
