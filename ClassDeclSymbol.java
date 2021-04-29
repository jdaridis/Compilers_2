import java.util.HashMap;
import java.util.Map;

public class ClassDeclSymbol extends Symbol {
    ClassDeclSymbol parentClass;

    Map<String,Symbol> fields;
    Map<String, Symbol> methods;

    public ClassDeclSymbol(String id) {
        super(id, PrimitiveType.IDENTIFIER);
        fields = new HashMap<String, Symbol>();
        methods = new HashMap<String, Symbol>();
    }

    public ClassDeclSymbol(String id, ClassDeclSymbol parentClass) {
        super(id, PrimitiveType.IDENTIFIER);
        this.parentClass = parentClass;
        fields = new HashMap<String, Symbol>();
        methods = new HashMap<String, Symbol>();

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String ret;
        if(parentClass != null){
            ret = id + ":" + parentClass.id + " " + fields.toString() + " " + methods.toString();
        } else {
            ret = id + " " + fields.toString() + " " + methods.toString();
        }
        return ret;
    }

    

    
}
