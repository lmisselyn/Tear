package norswap.sigh.interpreter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RuleStorage {

    HashMap<String, List<Rule>> struct;

    @SuppressWarnings("unchecked")
    public RuleStorage(){
        struct = new HashMap<>();
    }

    public Null addRule (Rule rule) {
        if (this.contains(rule.head)) {
            this.getRule(rule.head).add(rule);
        } else {
            List<Rule> LL = new LinkedList<>();
            LL.add(rule);
            struct.put(rule.head, LL);
        }
        return null;
    }

    public List<Rule> getRule(String head) {
        return struct.get(head);
    }

    public boolean contains(String head) {
        return struct.containsKey(head);
    }

    @Override
    public String toString() {
        return struct.toString();
    }
}
