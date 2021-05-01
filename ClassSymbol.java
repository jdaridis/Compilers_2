public class ClassSymbol extends ClassDeclSymbol {
    String className;

    public ClassSymbol(String id, String className) {
        super(id);
        this.className = className;
    }

    public ClassSymbol(String id, String className, ClassDeclSymbol type) {
        super(id);
        this.className = className;

        this.parentClass = type.parentClass;
        this.fields = type.fields;
        this.methods = type.methods;

    }

    public ClassSymbol(String id, ClassDeclSymbol type) {
        super(id);
        this.className = type.id;
        this.parentClass = type.parentClass;
        this.fields = type.fields;
        this.methods = type.methods;

    }

    public ClassSymbol(String id, ClassDeclSymbol parentClass, String className) {
        super(id, parentClass);
        this.className = className;
    }

    
    @Override
    public boolean isInstanceOf(ClassDeclSymbol parent) {
        if (this.className == parent.id) {
            return true;
        } else {
            return super.isInstanceOf(parent);
        }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String ret = className + " " + id;
        return ret;
    }
}
