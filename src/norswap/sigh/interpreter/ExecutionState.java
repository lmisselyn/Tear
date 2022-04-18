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
    private List<QueryArgNode> goals;
    private List<Rule> rules;
    private List<BoundedPair> bindings;

    public ExecutionState(List<QueryArgNode> goals, List<Rule> rules, List<BoundedPair> bindings) {
        this.goals = goals;
        this.rules = rules;
        this.bindings = bindings;
    }

    public List<QueryArgNode> getGoals() {
        return goals;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<BoundedPair> getBindings() {
        return bindings;
    }

}
