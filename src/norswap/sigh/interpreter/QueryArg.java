package norswap.sigh.interpreter;


import norswap.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryArg {
    public final String name;
    public final Integer arity;
    public final HashMap<Integer, String> terms;

    public final HashMap<Integer, String> logic_var;
    public final List<Object> arg_list;

    @SuppressWarnings("unchecked")
    public QueryArg (String name, Integer arity, HashMap<Integer, String> terms, HashMap<Integer, String> logic_var, List<Object> arg_list) {

        this.name = Util.cast(name, String.class);
        this.arity = arity;
        this.terms = terms;
        this.logic_var = logic_var;
        this.arg_list = arg_list;

    }

    public HashMap<Integer, String> getTerms () {return terms;}

    public List<String> getTermsAsList () {
        ArrayList<String> terms_list = new ArrayList<>();
        for (String term : terms.values()) {
            terms_list.add(term);
        }
        return terms_list;
    }

    public Integer getArity() {return arity;}
    public HashMap<Integer, String> getLogicVar () {return logic_var;}

    public String name () {
        return name;
    }

    public String toString() {
        String str = name + "(";
        for(int i = 0; i < arity; i++) {
            if (terms.containsKey(i)) {
                str += terms.get(i);
            }
            else {
                str += logic_var.get(i);
            }
            if (i != arity-1){
                str += " ";
            }
        }
        str += ")";
        return str;
    }
}
