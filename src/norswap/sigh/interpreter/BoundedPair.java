package norswap.sigh.interpreter;

import norswap.sigh.ast.StringLiteralNode;

import java.util.Objects;

/*
    DataStructure to manage the Keys of the hashmap in FactStorage
    @name is the name of a fact
    @arity is the number of terms in a fact
 */
public class BoundedPair {
    private String logic_var;
    private String term;

    public BoundedPair(String variable, String term) {
        this.logic_var = variable;
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public String getLogicVar() {
        return logic_var;
    }

    @Override
    public String toString() {
        return "(" + logic_var + ", " + term + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundedPair pair = (BoundedPair) o;
        return Objects.equals(logic_var, pair.logic_var) && Objects.equals(term, pair.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logic_var, term);
    }
}
