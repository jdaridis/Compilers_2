import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionSymbol extends Symbol {
    TypeSymbol returnType;
    Map<String, Symbol> args; //Check this later.

    

    public FunctionSymbol(String id, TypeSymbol returnType) {
        super(id, PrimitiveType.IDENTIFIER);
        args = new LinkedHashMap<String, Symbol>();
        this.returnType = returnType;
    }


    public FunctionSymbol(String id, TypeSymbol returnType, Map<String, Symbol> args) {
        super(id, PrimitiveType.IDENTIFIER);
        this.returnType = returnType;
        this.args = args;
    }

    public FunctionSymbol(String id, String returnType) {
        super(id, PrimitiveType.IDENTIFIER);
        args = new LinkedHashMap<String, Symbol>();
        PrimitiveType type = PrimitiveType.strToPrimitiveType(returnType);
        if(type == PrimitiveType.IDENTIFIER){
            this.returnType = new TypeSymbol(returnType);
        } else {
            this.returnType = new TypeSymbol(type);
        }
    }


    public FunctionSymbol(String id, String returnType, Map<String, Symbol> args) {
        super(id, PrimitiveType.IDENTIFIER);
        this.args = args;
        PrimitiveType type = PrimitiveType.strToPrimitiveType(returnType);
        if(type == PrimitiveType.IDENTIFIER){
            this.returnType = new TypeSymbol(returnType);
        } else {
            this.returnType = new TypeSymbol(type);
        }

    }

    public boolean checkOverride(FunctionSymbol override) throws Exception{
        if(this.returnType.type != override.returnType.type){
            throw new Exception("Declared method has a different return type than the superclass");
        } else if(this.returnType.type == PrimitiveType.IDENTIFIER){
            if(!this.returnType.id.equals(override.returnType.id)){
                throw new Exception("Declared method has a different type of args than the superclass");
            }
        }

        return checkArgsDecl(override.args);
    }


    private boolean checkArgsDecl(Map<String,Symbol> checkArgs) throws Exception {
        Symbol[] argsArray = Arrays.copyOf(args.values().toArray(), args.size(), Symbol[].class);
        Symbol[] checkArgsArray = Arrays.copyOf(checkArgs.values().toArray(), checkArgs.size(), Symbol[].class);

        if(argsArray.length != checkArgsArray.length){
            throw new Exception("Declared method has a different number of args than the superclass");
        }

        for(int i=0;i<argsArray.length;i++){
            if(argsArray[i].type != checkArgsArray[i].type){
                throw new Exception("Declared method has a different type of args than the superclass");
            } else if(argsArray[i].type == PrimitiveType.IDENTIFIER){
                ClassSymbol class1 = (ClassSymbol)argsArray[i];
                ClassSymbol class2 = (ClassSymbol)checkArgsArray[i];
                if(!class1.className.equals(class2.className)){
                    throw new Exception("Declared method has a different type of args than the superclass");
                }
            }
        }
        return true;
    }

    public boolean checkArgs(Map<String,Symbol> checkArgs, SymbolTable table) throws Exception {
        Symbol[] argsArray = Arrays.copyOf(args.values().toArray(), args.size(), Symbol[].class);
        TypeSymbol[] checkArgsArray = Arrays.copyOf(checkArgs.values().toArray(), checkArgs.size(), TypeSymbol[].class);

        if(argsArray.length != checkArgsArray.length){
            throw new Exception("Method call " + this.id + " has a different number of args than declared. Expected: " + argsArray.length + " but found: " + checkArgsArray.length);
        }

        for(int i=0;i<argsArray.length;i++){
            if(argsArray[i].type != checkArgsArray[i].type){
                throw new TypeException(argsArray[i].type.typeName, checkArgsArray[i].type.typeName);
                // throw new Exception("Declared method has a different type of args than the superclass");
            } else if(argsArray[i].type == PrimitiveType.IDENTIFIER){
                ClassSymbol class1 = (ClassSymbol)argsArray[i];
                ClassDeclSymbol class2 = table.lookupType(checkArgsArray[i].getTypeName());


                if(!class2.isInstanceOf(class1)){
                    throw new Exception("Type " + class2.id + " not instance of " + class1.className);
                    // throw new Exception("Declared method has a different type of args than the superclass");
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
            ret = returnType.getTypeName() + " " + id + "(";
            for(Symbol s:args.values()){
                ret += s.toString();
            }
            ret += ")";
        } else {
            ret = returnType.getTypeName() + " " + id + "()";
        }
        return ret;
    }

    
}
