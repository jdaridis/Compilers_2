import java.util.HashMap;
import java.util.Map;

public class FunctionSymbol extends Symbol {
    PrimitiveType returnType;
    Map<String, Symbol> args; //Check this later.

    

    public FunctionSymbol(String id, PrimitiveType returnType) {
        super(id, PrimitiveType.IDENTIFIER);
        args = new HashMap<String, Symbol>();
        this.returnType = returnType;
    }


    public FunctionSymbol(String id, PrimitiveType returnType, Map<String, Symbol> args) {
        super(id, PrimitiveType.IDENTIFIER);
        this.returnType = returnType;
        this.args = args;
    }

    public FunctionSymbol(String id, String returnType) {
        super(id, PrimitiveType.IDENTIFIER);
        args = new HashMap<String, Symbol>();
        this.returnType = PrimitiveType.strToPrimitiveType(returnType);
    }


    public FunctionSymbol(String id, String returnType, Map<String, Symbol> args) {
        super(id, PrimitiveType.IDENTIFIER);
        this.args = args;
        this.returnType = PrimitiveType.strToPrimitiveType(returnType);

    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String ret;
        if(args.size() != 0){
            ret = returnType.typeName + " " + id + "(";
            for(Symbol s:args.values()){
                ret += s.toString();
            }
            ret += ")";
        } else {
            ret = returnType.typeName + " " + id + "()";
        }
        return ret;
    }

    
}
