package norswap.sigh.ast;

import norswap.autumn.positions.Span;
import norswap.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryArgNode extends ExpressionNode {
    public final String name;
    public final Integer arity;
    public final HashMap<Integer, StringLiteralNode> terms;

    public final HashMap<Integer, String> logic_var;

    @SuppressWarnings("unchecked")
    public QueryArgNode (Span span, Object name, Object terms) {
        super(span);
        this.name = Util.cast(name, String.class);
        List<Object> terms_list = (List<Object>) terms;
        this.arity = terms_list.size();
        this.terms = new HashMap<>();
        this.logic_var = new HashMap<>();
        for (int i = 0; i < arity; i++) {
            if(terms_list.get(i).getClass().toString().equals("class norswap.sigh.ast.StringLiteralNode")) {
                this.terms.put(i, (StringLiteralNode) terms_list.get(i));
            }
            else if(terms_list.get(i).getClass().toString().equals("class java.lang.String")) {
                this.logic_var.put(i, (String) terms_list.get(i));
            }
        }
    }

    public HashMap<Integer, StringLiteralNode> getTerms () {return terms;}

    public List<StringLiteralNode> getTermsAsList () {
        ArrayList<StringLiteralNode> terms_list = new ArrayList<>();
        for (StringLiteralNode term : terms.values()) {
            terms_list.add(term);
        }
        return terms_list;
    }

    public Integer getArity() {return arity;}
    public HashMap<Integer, String> getLogicVar () {return logic_var;}

    public String name () {
        return name;
    }

    public String contents ()
    {
        String args = terms.size() == 0 ? "()" : "(...)";
        return name + args;
    }
}