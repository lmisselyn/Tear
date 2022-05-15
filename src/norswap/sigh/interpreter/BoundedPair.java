package norswap.sigh.interpreter;

import java.util.Objects;

/**
    BoundedPair linked a logic_var and a value. It's used to return a solution solver in the querySolver.solve
    @logic_var is the name of the logic_var
    @term is its value.
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

    public void changeLogicVar(String new_var) {
        this.logic_var = new_var;
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
