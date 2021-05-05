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

    
    @Override
    public boolean isInstanceOf(ClassDeclSymbol parent) {
        if (this.className.equals(parent.id)) {
            return true;
        } else {
            return super.isInstanceOf(parent);
        }
    }

    private boolean isParentOfHelper(ClassSymbol parent, ClassDeclSymbol child) {
        if (child == null) {
            return false;
        } else if (parent.className.equals(child.id)) {
            return true;
        } else  {
            return isParentOfHelper(parent, child.parentClass);
        }
    }

    @Override
    public boolean isParentOf(ClassDeclSymbol child) {
        // TODO Auto-generated method stub
        if (this.className.equals(child.id)) {
            return true;
        } else {
            return isParentOfHelper(this, child.parentClass);
        }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String ret = className + " " + id;
        return ret;
    }
}
