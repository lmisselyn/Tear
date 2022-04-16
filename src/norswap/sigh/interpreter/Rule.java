package norswap.sigh.interpreter;
import norswap.sigh.ast.TailNode;
import norswap.utils.Util;

import java.util.List;
import java.util.Set;

/*
    DataStructure that represents a Fact
    @name is the name of the fact
    @terms are the terms of the fact
    @arity is the number of terms of a fact
 */
public class Rule {
    public final String head;
    public final List<String> head_args;
    public final List<TailNode> tails;

    @SuppressWarnings("unchecked")
    public Rule(Object head, Object head_args, Object tails){
        this.head = Util.cast(head, String.class);
        this.head_args = Util.cast(head_args, List.class);
        this.tails = Util.cast(tails, List.class);
    }

    @Override
    public String toString() {
        return head + '(' + head_args + ')' +":= " + tails;
    }
}
