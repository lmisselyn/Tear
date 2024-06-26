package norswap.sigh.interpreter;

import norswap.sigh.ast.QueryArgNode;

import java.util.List;
import java.util.Objects;

/**
 *
 *  DataStructure to store the execution state
 *  @goals is the goals left to be satisfied
 *  @rules is the rules left to try for the current goal
 *  @bindings is the current set of bindings
 *
 */
public class ExecutionState {
    private List<QueryArg> goals;
    private List<Rule> rules;
    private List<List<BoundedPair>> bindings;

    public ExecutionState(List<QueryArg> goals, List<Rule> rules, List<List<BoundedPair>> bindings) {
        this.goals = goals;
        this.rules = rules;
        this.bindings = bindings;
    }

    public List<QueryArg> getGoals() {
        return goals;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<List<BoundedPair>> getBindings() {
        return bindings;
    }

}
