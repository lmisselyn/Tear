package norswap.sigh.types;

import norswap.utils.NArrays;
import java.util.Arrays;

public final class FactType extends Type {
    public final Type[] termTypes;

    public FactType (Type... termTypes) {
        this.termTypes = termTypes;
    }

    @Override public String name() {
        String[] params = NArrays.map(termTypes, new String[0], Type::name);
        return String.format("(%s) -> %s", String.join(",", params));
    }

    @Override public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof FactType)) return false;
        FactType other = (FactType) o;

        return Arrays.equals(termTypes, other.termTypes);
    }

    @Override public int hashCode () {
        return Arrays.hashCode(termTypes);
    }
}
