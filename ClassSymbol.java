public class ClassSymbol extends ClassDeclSymbol{
    String className;

    public ClassSymbol(String id, String className) {
        super(id);
        this.className = className;
    }

    public ClassSymbol(String id, ClassDeclSymbol parentClass, String className) {
        super(id, parentClass);
        this.className = className;
    }
    

     @Override
    public String toString() {
        // TODO Auto-generated method stub
        String ret =  className + " " + id;
        return ret;
    }
}
