import java.util.HashMap;
import java.util.Map;

public class ClassSymbol extends Symbol {
    String className;
    ClassSymbol parentClass;

    Map<String,Symbol> members;

    public ClassSymbol(String id, String className) {
        super(id, PrimitiveType.IDENTIFIER);
        this.className = className;
        members = new HashMap<String, Symbol>();
    }

    public ClassSymbol(String id, String className, ClassSymbol parentClass) {
        super(id, PrimitiveType.IDENTIFIER);
        this.className = className;
        this.parentClass = parentClass;
        members = new HashMap<String, Symbol>();

    }

    
}
