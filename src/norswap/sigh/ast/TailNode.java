package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

import java.util.List;

public class TailNode extends ExpressionNode {

    public final String name;
    public final List<String> args;

    @SuppressWarnings("unchecked")
    public TailNode(Span span, Object name, Object args){
        super(span);
        this.name = Util.cast(name, String.class);
        this.args = Util.cast(args, List.class);
    }

    public List<String> getArgs () {return args;}

    public String name () {
        return name;
    }

    public String contents () {
        String terms = getArgs().size() == 0 ? "()" : "(...)";
        return name + terms;
    }
}
