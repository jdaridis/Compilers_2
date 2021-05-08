public class TypeException extends Exception {

    public TypeException(String type, String wrongType) {
        super("Wrong type. Expected: " + type + ", got: " + wrongType);
    }

    
    
}
