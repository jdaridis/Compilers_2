import java.util.HashMap;
import java.util.Map;

public class ClassSymbol extends Symbol {
    String className;
    ClassSymbol parentClass;

    Map<String,Symbol> fields;
    Map<String, Symbol> methods;

    public ClassSymbol(String id, String className) {
        super(id, PrimitiveType.IDENTIFIER);
        this.className = className;
        fields = new HashMap<String, Symbol>();
        methods = new HashMap<String, Symbol>();
    }

    public ClassSymbol(String id, String className, ClassSymbol parentClass) {
        super(id, PrimitiveType.IDENTIFIER);
        this.className = className;
        this.parentClass = parentClass;
        fields = new HashMap<String, Symbol>();
        methods = new HashMap<String, Symbol>();

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String ret;
        if(parentClass != null){
            ret = className + ":" + parentClass.className + " " + id + " " + fields.toString() + " " + methods.toString();
        } else {
            ret = className + " " + id + " " + fields.toString() + " " + methods.toString();
        }
        return ret;
    }

    

    
}
