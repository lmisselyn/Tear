package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

import java.util.List;

public class RuleDeclarationNode extends DeclarationNode {

    public final String head;
    public final List<String> head_args;
    public final List<TailNode> tails;

    @SuppressWarnings("unchecked")
    public RuleDeclarationNode(Span span, Object head, Object head_args, Object tails){
        super(span);
        this.head = Util.cast(head, String.class);
        this.head_args = Util.cast(head_args, List.class);
        this.tails = Util.cast(tails, List.class);
    }

    public List<String> getHead_args() {
        return head_args;
    }

    public List<TailNode> getTails() {
        return tails;
    }

    @Override public String name () {
        return head;
    }

    @Override
    public String declaredThing() {
        return "rule";
    }

    @Override public String contents () {
        String terms = tails.size() == 0 ? "()" : "(...)";
        return head + terms;
    }
}

