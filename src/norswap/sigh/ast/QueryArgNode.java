package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;
import java.util.List;

public class QueryArgNode extends ExpressionNode {
    public final String name;
    public final List<StringLiteralNode> terms;

    @SuppressWarnings("unchecked")
    public QueryArgNode (Span span, Object name, Object terms) {
        super(span);
        this.name = Util.cast(name, String.class);
        this.terms = Util.cast(terms, List.class);
    }

    public List<StringLiteralNode> getTerms () {return terms;}

    public String name () {
        return name;
    }

    public String contents ()
    {
        String args = terms.size() == 0 ? "()" : "(...)";
        return name + args;
    }
}