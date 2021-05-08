public class DeclarationException extends Exception {

    private static String message = "Next time do us the favor and declare the variable: ";

    public DeclarationException(String name) {
        super(message + name);
    }
    
    public DeclarationException(String name, String type) {
        super(message + name + "of type " + type);
    }
    
}
