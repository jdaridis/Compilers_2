public class FunctionSymbol extends Symbol {
    PrimitiveType returnType;
    Symbol[] args; //Check this later.

    

    public FunctionSymbol(String id, PrimitiveType returnType) {
        super(id, PrimitiveType.IDENTIFIER);
        this.returnType = returnType;
    }


    public FunctionSymbol(String id, PrimitiveType returnType, Symbol[] args) {
        super(id, PrimitiveType.IDENTIFIER);
        this.returnType = returnType;
        this.args = args;
    }

    public FunctionSymbol(String id, String returnType) {
        super(id, PrimitiveType.IDENTIFIER);
        switch(returnType){
            case "int":
                this.returnType = PrimitiveType.INT;
                break;
            case "boolean":
                this.returnType = PrimitiveType.BOOLEAN;
                break;
            case "int[]":
                this.returnType = PrimitiveType.ARRAY;
                break;
            default:
                this.returnType = PrimitiveType.IDENTIFIER;
                break;
        }
    }


    public FunctionSymbol(String id, String returnType, Symbol[] args) {
        super(id, PrimitiveType.IDENTIFIER);
        this.args = args;
        switch(returnType){
            case "int":
                this.returnType = PrimitiveType.INT;
                break;
            case "boolean":
                this.returnType = PrimitiveType.BOOLEAN;
                break;
            case "int[]":
                this.returnType = PrimitiveType.ARRAY;
                break;
            default:
                this.returnType = PrimitiveType.IDENTIFIER;
                break;
        }
    }

    
}
