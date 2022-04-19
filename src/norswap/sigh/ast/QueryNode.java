package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

import java.util.List;

public class QueryNode extends ExpressionNode {
    public final QueryArgNode queryArgs;

    @SuppressWarnings("unchecked")
    public QueryNode(Span span, Object queryArgs) {
        super(span);
        this.queryArgs = (QueryArgNode) queryArgs;

    }

    public QueryArgNode getQueryArgs() {
        return queryArgs;
    }

    public String contents ()
    {
        return queryArgs.contents();
    }
}
