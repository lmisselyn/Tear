package norswap.sigh.interpreter;
import norswap.utils.Util;

import java.util.Set;

/*
    DataStructure that represents a Fact
    @name is the name of the fact
    @terms are the terms of the fact
    @arity is the number of terms of a fact
 */
public class Fact {
    public final String name;
    public Set<String> terms;
    public Integer arity;

    @SuppressWarnings("unchecked")
    public Fact(Object name, Object terms, Integer arity){
        this.name = Util.cast(name, String.class);
        this.terms = Util.cast(terms, Set.class);
        this.arity = arity;
    }

    public Set<String> getTerms() {return terms;}

    public String getName() {
        return name;
    }

    public Integer getArity() {return arity;}

    public Null addTerm(String new_term) {
        terms.add(new_term);
        return null;
    }

    // Add for arity > 1
//    public Null addTerms(Object new_term) {
//        terms.add(new_term);
//        return null;
//    }

    @Override
    public String toString() {
        return name + ": " + terms;
    }
}
