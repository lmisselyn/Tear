package norswap.sigh.interpreter;
import norswap.sigh.ast.QueryArgNode;
import norswap.utils.Util;

import java.util.List;

/**
    DataStructure that represents a Rule
    @head is the name of the head part
    @head_args are the terms inside the head part
    @tails is a list of queryArgNode (have a name and a lsit of term)
    @logic_operands is the list of logic operands if present in the rule.
    @fact is true if the Rule is a Fact here. (the tails are always true)
 */
public class Rule {
    public final String head;
    public final List<Object> head_args;
    public final List<QueryArgNode> tails;
    public final List<String> logic_operands;
    public final Boolean fact;
    public final Integer arity;


    @SuppressWarnings("unchecked")
    public Rule(Object head, Object head_args, Object tails, Object logic_operands, Boolean fact){
        this.fact = fact;
        this.head = Util.cast(head, String.class);
        this.head_args = Util.cast(head_args, List.class);
        this.arity = this.head_args.size();
        if(!fact) {
            this.tails = Util.cast(tails, List.class);
            this.logic_operands = (List<String>) logic_operands;
        }
        else {
            this.tails = null;
            this.logic_operands = null;
        }
    }

    public List<Object> get_head_args(){
        return head_args;
    }

    public Boolean is_fact() {
        return fact;
    }

    @Override
    public String toString() {
        if (fact) {
            return head + '(' + head_args + ')';
        }
        String str = "";
        Integer size = tails.size();
        for(int i = 0; i < size; i++) {
            str += tails.get(i) + " ";
            if (i < size-1) {
                str += logic_operands.get(i) + " ";
            }
        }
        return head + '(' + head_args + ')' +":= " + str;
    }
}
