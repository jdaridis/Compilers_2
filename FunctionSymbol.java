import java.util.Arrays;
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

    public boolean checkOverride(FunctionSymbol override) throws Exception{
        if(this.returnType != override.returnType){
            throw new Exception("Declared method has a different return type than the superclass");
        } else if(this.returnType == PrimitiveType.IDENTIFIER){
            if(!this.returnType.typeName.equals(override.returnType.typeName)){
                throw new Exception("Declared method has a different type of args than the superclass");
            }
        }

        return checkArgs(override.args);
    }


    public boolean checkArgs(Map<String,Symbol> checkArgs) throws Exception {
        Symbol[] argsArray = Arrays.copyOf(args.values().toArray(), args.size(), Symbol[].class);
        Symbol[] checkArgsArray = Arrays.copyOf(checkArgs.values().toArray(), checkArgs.size(), Symbol[].class);

        if(argsArray.length != checkArgsArray.length){
            throw new Exception("Declared method has a different number of args than the superclass");
        }

        for(int i=0;i<argsArray.length;i++){
            if(argsArray[i].type != checkArgsArray[i].type){
                throw new Exception("Declared method has a different type of args than the superclass");
            } else if(argsArray[i].type == PrimitiveType.IDENTIFIER){
                if(!argsArray[i].type.typeName.equals(checkArgsArray[i].type.typeName)){
                    throw new Exception("Declared method has a different type of args than the superclass");
                }
            }
        }
        return true;
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
