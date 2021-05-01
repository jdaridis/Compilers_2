import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassDeclSymbol extends Symbol {
    ClassDeclSymbol parentClass;

    Map<String,Symbol> fields;
    Map<String, Symbol> methods;

    public ClassDeclSymbol(String id) {
        super(id, PrimitiveType.IDENTIFIER);
        fields = new LinkedHashMap<String, Symbol>();
        methods = new LinkedHashMap<String, Symbol>();
        this.size = 0;
    }

    public ClassDeclSymbol(String id, ClassDeclSymbol parentClass) {
        super(id, PrimitiveType.IDENTIFIER);
        this.parentClass = parentClass;
        fields = new LinkedHashMap<String, Symbol>();
        methods = new LinkedHashMap<String, Symbol>();
        this.size = 0;
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
