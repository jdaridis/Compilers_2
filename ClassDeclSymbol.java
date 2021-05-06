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

    private boolean isInstanceOfHelper(ClassDeclSymbol parent, ClassDeclSymbol child) {
        System.out.println("Parent: " + parent + ", Child: " + child);
        if (child == null) {
            return false;
        } else if (parent.id.equals(child.id)) {
            return true;
        } else  {
            return isInstanceOfHelper(parent, child.parentClass);
        }
    }

    public boolean isInstanceOf(ClassDeclSymbol parent){
        if (this.id.equals(parent.id)) {
            return true;
        } else {
            return isInstanceOfHelper(parent, this.parentClass);
        }

    }

    private boolean isInstanceOfHelper(ClassSymbol parent, ClassDeclSymbol child) {
        if (child == null) {
            return false;
        } else if (parent.className.equals(child.id)) {
            return true;
        } else  {
            return isInstanceOfHelper(parent, child.parentClass);
        }
    }

    public boolean isInstanceOf(ClassSymbol parent){
        if (this.id.equals(parent.className)) {
            return true;
        } else {
            return isInstanceOfHelper(parent, this.parentClass);
        }

    }

    public boolean isParentOf(ClassDeclSymbol child){
        if (this.id.equals(child.id)) {
            return true;
        } else {
            return isInstanceOfHelper(this, child.parentClass);
        }
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
