package norswap.sigh.interpreter;

import norswap.sigh.ast.QueryArgNode;
import norswap.sigh.ast.QueryNode;
import norswap.sigh.ast.StringLiteralNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;



public class QuerySolver {

    RuleStorage ruleStorage;
    private List<ExecutionState> BacktrackStorage;
    private List<List<BoundedPair>> final_bindings;
    public QuerySolver(RuleStorage ruleStorage) {
        this.ruleStorage = ruleStorage;
        this.BacktrackStorage = new ArrayList<>();
        this.final_bindings = new ArrayList<>();
    }

    public Pair solve(QueryNode node) {
        List<QueryArg> query_goals = get_queryArgs(node.getQueryArgs());
        List<Rule> rules = rules_for(query_goals.get(0).name);

        if (rules == null) {
            //System.out.println("false");
            return new Pair(false, null);
        }
        List<List<BoundedPair>> bindings = new ArrayList<>();
        Stack<ExecutionState> backtrack = new Stack<ExecutionState>();
        List<List<BoundedPair>> result = satisfy(backtrack, bindings, rules, query_goals);
        //System.out.println(result);
        if (result != null) {
            //System.out.println("true");
            return new Pair(true, result);
        }
        else {
            //System.out.println("false");
            return new Pair(false, null);
        }
    }

    private List<norswap.sigh.interpreter.QueryArg> get_queryArgs(List<QueryArgNode> query_args_node) {
        List<norswap.sigh.interpreter.QueryArg> query_args = new ArrayList<>();
        for (int i = 0; i < query_args_node.size(); i++) {
            QueryArgNode curr = query_args_node.get(i);
            HashMap<Integer, String> terms = new HashMap<Integer, String>();
            HashMap<Integer, StringLiteralNode> curr_terms = curr.terms;
            for (Integer key : curr_terms.keySet()) {
                terms.put(key, curr_terms.get(key).value);
            }
            query_args.add(new norswap.sigh.interpreter.QueryArg(curr.name, curr.arity, terms, curr.logic_var, curr.arg_list));
        }
        return query_args;
    }

    /**
     * @param backtrack : Stack of execution states
     * @param bindings : List of pairs (logicVariable, term)
     * @param goals : goals to achieve
     * @param rules : rules linked with the goals
     *
     * @return A List of bindings that satisfies the query
     */
    private List<List<BoundedPair>> satisfy(Stack<ExecutionState> backtrack, List<List<BoundedPair>> bindings,
                                            List<Rule> rules, List<QueryArg> goals) {
        // 1: All goals solved: solution found.
        // 2: No more rules for first goal, backtrack to last choice point, or fail.
        // 3: Try the next rule for the current goal.
        if (goals.isEmpty()) {
            if (bindings != null) {store_result(bindings);}
            if (! backtrack.isEmpty()) {
                ExecutionState state = backtrack.elementAt(0);
                List<List<BoundedPair>> next = satisfy(new Stack<ExecutionState>(), new ArrayList<>(), state.getRules(), state.getGoals());
            }
            return final_bindings;
        }
        else if (rules.isEmpty()) {
            if (backtrack.isEmpty()) {return null;}
            else {doBacktrack(backtrack);}
        }
        else {tryRule(backtrack, bindings, rules, goals);}
        return final_bindings;
    }


    private List<List<BoundedPair>> succeed(List<List<BoundedPair>> bindings, Stack<ExecutionState> backtrack) {
        BacktrackStorage = backtrack;
        return bindings;
    }

    private void doBacktrack(Stack<ExecutionState> backtrack) {
        ExecutionState state = backtrack.pop();
        if (!state.getBindings().isEmpty()) {
            state.getBindings().remove(state.getBindings().size()-1);
        }
        satisfy(backtrack, state.getBindings(), state.getRules(), state.getGoals());
    }

    private List<List<BoundedPair>> tryRule(Stack<ExecutionState> backtrack, List<List<BoundedPair>> bindings,
                                            List<Rule> rules, List<QueryArg> goals) {
        QueryArg goal = goals.get(0);
        List<List<BoundedPair>> new_bindings = bindings;
        System.out.println(bindings);

        Rule rule = rules.get(0);
        if (rule.arity.equals(goal.arity) && rule.head.equals(goal.name)) {
            List<List<BoundedPair>> uni = unify(goal, rule, new_bindings);
            if (uni != null) { // If we have a result, we store de execution state on the backtrack stack and call
                // satisfy with the next goal.
                new_bindings = uni;
                rules.remove(0);
                backtrack.push(new ExecutionState(new ArrayList<>(goals), rules, new_bindings));
                goals.remove(0);
                if (! goals.isEmpty()) {
                    List<Rule> rules_for_next_goal = rules_for(goals.get(0).name);
                    List<QueryArg> new_goals = look_for_linked_var(bindings, goals);
                    return satisfy(backtrack, new_bindings, rules_for_next_goal, new_goals);
                } else{return satisfy(backtrack, new_bindings, rules, goals);}
            }
            else {
                rules.remove(0);
                return satisfy(backtrack, bindings, rules, goals);
            }
        }
        else { //The rule fail, then we remove it and call satisfy
            rules.remove(0);
            return satisfy(backtrack, bindings, rules, goals);
        }

        //on essaye une règle, et on ajoute les bidings pour cette règle.
        //si la première règle échoue, on rappelle satisfy en retirant cette règle.
        //si on a un résultat, on stocke l'état d'éxécution, et on rappelle satisfy avec le goal suivant

    }

    /**
     * The goal and the rule is unified if we can find logic variables such that
     * the goal and the rule are equal
     *
     * @param goal : goal to unify with the rule
     * @param rule : rule to unify with the goal
     * @param bindings : List of pairs (logicVariable, term)
     *
     * @return The List of bindings enhanced with the bindings that
     * unify the goal and the rule
     */

    private List<List<BoundedPair>> unify(QueryArg goal, Rule rule, List<List<BoundedPair>> bindings) {
        int size = goal.arity;
        HashMap<Integer, String> terms = goal.terms;
        HashMap<Integer, String> logic_vars = goal.logic_var;
        List<Object> rule_args = rule.get_head_args();

        if (rule.is_fact()) {
            List<BoundedPair> tmp = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (terms.containsKey(i)) {
                    if (! terms.get(i).equals(((StringLiteralNode) rule_args.get(i)).value)) {
                        return null;
                    }
                }
                else if (logic_vars.containsKey(i)) {
                    tmp.add(new BoundedPair(logic_vars.get(i), ((StringLiteralNode) rule_args.get(i)).value));
                }
            }
            bindings.add(tmp);
        }
        else {
            //On part du principe que toutes les variables sont différentes.
            //L'idée c'est de regarder dans les bidings si certaines variables présentes dans les
            // goals sont déjà liées. On va alors modifier les goals en remplacant les variables par leur
            //valeur
            List<QueryArg> tail_goals = get_queryArgs(rule.tails);

            if (!terms.isEmpty()) {
                tail_goals = link_var_terms(tail_goals, terms, rule_args);
            }
            List<Rule> tail_rules = rules_for(tail_goals.get(0).name);
            //If no rules for the goals, then null
            if (tail_rules == null) {
                return null;
            }

            List<List<BoundedPair>> tail_bindings = new ArrayList<>();
            Stack<ExecutionState> tail_backtrack = new Stack<ExecutionState>();
            List<List<BoundedPair>> bindings_for_this_rule = satisfy(tail_backtrack, tail_bindings, tail_rules, tail_goals);
            if (bindings_for_this_rule != null) {
                bindings.add(get_true_bindings(bindings_for_this_rule));
                //bindings.addAll(bindings_for_this_rule);
            } else {return null;}
        }
        return bindings;
    }

    private List<QueryArg> look_for_linked_var(List<List<BoundedPair>> bindings, List<QueryArg> goals) {
        List<QueryArg> new_goals = new ArrayList<>(goals);
        for (List<BoundedPair> binding : bindings) {
            for (BoundedPair bp : binding) {
                String curr_logic_var = bp.getLogicVar();
                for(int i= 0; i < new_goals.size(); i++) {
                    QueryArg curr_goal = new_goals.get(i);
                    HashMap<Integer, String> new_terms = new HashMap<>(curr_goal.terms);
                    HashMap<Integer, String> new_logic_var = new HashMap<>();
                    if(curr_goal.logic_var.containsValue(curr_logic_var)) {
                        for(Integer key : curr_goal.logic_var.keySet()) {
                            if (curr_goal.logic_var.get(key).equals(curr_logic_var)) {
                                new_terms.put(key, bp.getTerm());
                            } else {new_logic_var.put(key, curr_goal.logic_var.get(key));}
                        }
                    } else {new_logic_var = curr_goal.logic_var;}
                    new_goals.remove(i);
                    new_goals.add(i, new QueryArg(curr_goal.name, curr_goal.arity, new_terms, new_logic_var,
                            get_arg_list(curr_goal.arity, new_terms, new_logic_var)));
                }
            }
        }
        return new_goals;
    }

    private List<QueryArg> link_var_terms(List<QueryArg> goals, HashMap<Integer, String> terms, List<Object> rule_args) {
        List<QueryArg> new_goals = new ArrayList<>();
        for(int i = 0; i < goals.size(); i++) {
            QueryArg current_goal = goals.get(i);
            HashMap<Integer, String> new_terms = new HashMap<>(current_goal.terms);
            HashMap<Integer, String> new_logic_var = new HashMap<>();
            for (Integer key : terms.keySet()) {
                String var_to_change = (String) rule_args.get(key);
                for (Integer key_logic : current_goal.logic_var.keySet()) {
                    if (current_goal.logic_var.get(key_logic).equals(var_to_change)) {
                        new_terms.put(key_logic, terms.get(key));
                    }
                    else{new_logic_var.put(key_logic, current_goal.logic_var.get(key_logic));}
                }
            }
            new_goals.add(new QueryArg(current_goal.name, current_goal.arity, new_terms, new_logic_var,
                    get_arg_list(current_goal.arity, new_terms, new_logic_var)));
        }
        return new_goals;
    }

    private List<Object> get_arg_list(Integer arity, HashMap<Integer, String> terms, HashMap<Integer, String> logic_var) {
        List<Object> new_arg_list = new ArrayList<>();
        for(int i = 0; i < arity; i++) {
            if (terms.containsKey(i)) {
                new_arg_list.add(terms.get(i));
            }
            else {
                new_arg_list.add(logic_var.get(i));
            }
        }
        return new_arg_list;
    }

    private List<BoundedPair> get_true_bindings(List<List<BoundedPair>> bindings) {
        List<BoundedPair> true_bindings = new ArrayList<>();
        HashMap<String, String> hash = new HashMap<>();
        for (List<BoundedPair> binding : bindings) {
            for (BoundedPair bd : binding) {
                if (!hash.containsKey(bd.getLogicVar())){
                    hash.put(bd.getLogicVar(), bd.getTerm());
                }
            }
        }
        for (String key : hash.keySet()) {
            true_bindings.add(new BoundedPair(key, hash.get(key)));
        }
        return true_bindings;
    }

    /**
     * Take the name of a goal and return a list of rules which have
     * the predicate 'name' on the left side
     */
    private List<Rule> rules_for(String name) {
        if (ruleStorage.contains(name)) {
            List<Rule> rules = new ArrayList<>(ruleStorage.getRule(name));
            return rules;
        } else {return null;}
    }

    private void store_result(List<List<BoundedPair>> bindings) {
        final_bindings.addAll(bindings);
    }


}

