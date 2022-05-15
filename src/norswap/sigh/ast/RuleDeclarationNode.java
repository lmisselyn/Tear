package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 DataStructure that represents a Rule
 @head is the name of the head part
 @head_args are the terms inside the head part
 @tails is a list of queryArgNode (have a name and a lsit of term)
 @logic_operands is the list of logic operands if present in the rule.
 */
public class RuleDeclarationNode extends DeclarationNode {

    public final String head;
    public final List<String> head_args;
    public final List<QueryArgNode> tails;
    public final List<String> logic_operand;
    public final List<Object> list_to_print;

    @SuppressWarnings("unchecked")
    public RuleDeclarationNode(Span span, Object head, Object head_args, Object tails){
        super(span);
        this.head = Util.cast(head, String.class);
        this.head_args = Util.cast(head_args, List.class);
        this.tails = new ArrayList<QueryArgNode>();
        this.logic_operand = new ArrayList<>();
        List<Object> tmp = (List<Object>) tails;
        this.list_to_print = tmp;

        for(int i = 0; i < tmp.size(); i++) {
            if(tmp.get(i).getClass().toString().equals("class norswap.sigh.ast.QueryArgNode")) {
                this.tails.add((QueryArgNode) tmp.get(i));
            } else {
                this.logic_operand.add((String) tmp.get(i));
            }
        }

    }

    public List<String> getHead_args() {
        return head_args;
    }

    @Override public String name () {
        return head;
    }

    @Override
    public String declaredThing() {
        return "rule";
    }

    @Override public String contents () {
        return head + list_to_print.toString();
    }
}

