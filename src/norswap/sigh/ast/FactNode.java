package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

import java.util.List;

public class FactNode extends DeclarationNode {

    public final String name;
    public final List<ParameterNode> terms;

    public FactNode(Span span, Object name, List<ParameterNode> terms){
        super(span);
        this.name = Util.cast(name, String.class);
        this.terms = terms;
    }

    @Override public String name () {
        return name;
    }

    @Override
    public String declaredThing() {
        return "fact";
    }

    @Override
    public List<ParameterNode> contents() {
        return this.terms;
    }
}
