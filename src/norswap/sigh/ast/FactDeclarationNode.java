package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

import java.util.List;

public class FactDeclarationNode extends DeclarationNode {

    public final String name;
    public final List<StringLiteralNode> terms;

    @SuppressWarnings("unchecked")
    public FactDeclarationNode(Span span, Object name, Object terms){
        super(span);
        this.name = Util.cast(name, String.class);
        this.terms = Util.cast(terms, List.class);
    }

    public List<StringLiteralNode> getTerms () {return terms;}

    @Override public String name () {
        return name;
    }

    @Override
    public String declaredThing() {
        return "fact";
    }

    @Override public String contents () {
        return name;
    }
}
