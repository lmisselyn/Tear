package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

import java.util.List;

public class QueryNode extends ExpressionNode {
    public final List<QueryArgNode> queryArgs;

    @SuppressWarnings("unchecked")
    public QueryNode(Span span, Object queryArgs) {
        super(span);
        this.queryArgs = Util.cast(queryArgs, List.class);

    }

    public List<QueryArgNode> getQueryArgs() {
        return queryArgs;
    }

    public String contents ()
    {
        String ret = "";
        for (int i = 0; i < queryArgs.size(); i++) {
            ret += queryArgs.get(i).contents();
        }
        return ret;
    }
}
