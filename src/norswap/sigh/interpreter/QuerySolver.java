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

    public Boolean solve(QueryNode node) {
        List<QueryArgNode> query_goals = new ArrayList<>(node.getQueryArgs());
        List<Rule> rules = rules_for(query_goals.get(0).name);

        if (rules == null) {
            System.out.println("false");
            return false;
        }
        List<List<BoundedPair>> bindings = new ArrayList<>();
        Stack<ExecutionState> backtrack = new Stack<ExecutionState>();
        List<List<BoundedPair>> result = satisfy(backtrack, bindings, rules, query_goals);
        System.out.println(result);
        if (result != null) {
            System.out.println("true");
        }
        else {System.out.println("false");};
        return false;
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
                                            List<Rule> rules, List<QueryArgNode> goals) {
        // 1: All goals solved: solution found.
        // 2: No more rules for first goal, backtrack to last choice point, or fail.
        // 3: Try the next rule for the current goal.
        if (goals.isEmpty()) {
            if (bindings != null) {store_result(bindings);}
            if (! backtrack.isEmpty()) {
                ExecutionState state = backtrack.elementAt(backtrack.size()-1);
                List<List<BoundedPair>> next = satisfy(new Stack<ExecutionState>(), state.getBindings(), state.getRules(), state.getGoals());
            }
            return final_bindings;
        }
        else if (rules.isEmpty()) {
            if (backtrack.isEmpty()) {return null;}
            else {doBacktrack(backtrack);}
        }
        else {tryRule(backtrack, bindings, rules, goals);}
        return bindings;
    }


    private List<List<BoundedPair>> succeed(List<List<BoundedPair>> bindings, Stack<ExecutionState> backtrack) {
        BacktrackStorage = backtrack;
        return bindings;
    }

    private void doBacktrack(Stack<ExecutionState> backtrack) {
        ExecutionState state = backtrack.pop();
        satisfy(backtrack, state.getBindings(), state.getRules(), state.getGoals());
    }

    private List<List<BoundedPair>> tryRule(Stack<ExecutionState> backtrack, List<List<BoundedPair>> bindings,
                                            List<Rule> rules, List<QueryArgNode> goals) {
        QueryArgNode goal = goals.get(0);
        List<List<BoundedPair>> new_bindings = bindings;

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
                    return satisfy(backtrack, new_bindings, rules_for_next_goal, goals);
                } return satisfy(backtrack, new_bindings, rules, goals);
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

    private List<List<BoundedPair>> unify(QueryArgNode goal, Rule rule, List<List<BoundedPair>> bindings) {
        int size = goal.arity;
        HashMap<Integer, StringLiteralNode> terms = goal.terms;
        HashMap<Integer, String> logic_vars = goal.logic_var;
        List<StringLiteralNode> rule_args = rule.get_head_args();

        if (rule.is_fact()) {
            List<BoundedPair> tmp = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (terms.containsKey(i)) {
                    if (! terms.get(i).value.equals(rule_args.get(i).value)) {
                        return null;
                    }
                }
                else if (logic_vars.containsKey(i)) {
                    tmp.add(new BoundedPair(logic_vars.get(i), rule_args.get(i).value));
                }
            }
            bindings.add(tmp);
        }
        else {
            List<QueryArgNode> tail_goals = rule.tails;
            List<Rule> tail_rules = new ArrayList<>();
            for(int i = 0; i < tail_goals.size(); i++) {
                List<Rule> rules = rules_for(tail_goals.get(i).name);
                if (rules != null) {
                    tail_rules.addAll(rules);
                }
            }
            //If no rules for the goals, then null
            if (tail_rules.isEmpty()) {
                return null;
            }
            List<List<BoundedPair>> tail_bindings = new ArrayList<>();
            Stack<ExecutionState> tail_backtrack = new Stack<ExecutionState>();
            List<List<BoundedPair>> bindings_for_this_rule = satisfy(tail_backtrack, tail_bindings, tail_rules, tail_goals);
            if (bindings_for_this_rule != null) {
                bindings.addAll(bindings_for_this_rule);
            } else {return null;}
        }
        return bindings;
    }


    /**
     * Take the name of a goal and return a list of rules which have
     * the predicate 'name' on the left side
     */
    private List<Rule> rules_for(String name) {
        if (ruleStorage.contains(name)) {
            return ruleStorage.getRule(name);
        } else {return null;}
    }

    private void store_result(List<List<BoundedPair>> bindings) {
        final_bindings.addAll(bindings);
    }
}


