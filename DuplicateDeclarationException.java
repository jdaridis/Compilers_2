public class DuplicateDeclarationException extends Exception {

    private static String message = "Duplicate use of name: ";

    public DuplicateDeclarationException(String name) {
        super(message + name);
    }
    

    
}
