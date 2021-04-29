import java.util.Map;

import syntaxtree.ArrayAssignmentStatement;
import syntaxtree.ArrayType;
import syntaxtree.AssignmentStatement;
import syntaxtree.BooleanType;
import syntaxtree.ClassDeclaration;
import syntaxtree.ClassExtendsDeclaration;
import syntaxtree.FormalParameter;
import syntaxtree.Goal;
import syntaxtree.Identifier;
import syntaxtree.IntegerType;
import syntaxtree.MainClass;
import syntaxtree.MethodDeclaration;
import syntaxtree.PrimaryExpression;
import syntaxtree.VarDeclaration;
import visitor.GJDepthFirst;

public class DeclarationsVisitor extends GJDepthFirst<String, SymbolTable> {

    /**
     * Grammar production:
     * f0 -> MainClass()
     * f1 -> ( TypeDeclaration() )*
     * f2 -> <EOF>
     */
    
    @Override
    public String visit(Goal n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        argu.enter();
        return super.visit(n, argu);
    }

    /**
     * Grammar production:
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */

    @Override
    public String visit(MainClass n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String name = n.f1.accept(this, argu);
        argu.insert(name, new ClassDeclSymbol(name));

        argu.enter();
        super.visit(n, argu);

        argu.exit();
        return null;
    }

    /**
     * Grammar production:
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */

    @Override
    public String visit(VarDeclaration n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String strType = n.f0.accept(this, argu);

        String name = n.f1.accept(this, argu);

        PrimitiveType type;

        switch(strType){
            case "int":
                type = PrimitiveType.INT;
                break;
            case "boolean":
                type = PrimitiveType.BOOLEAN;
                break;
            case "int[]":
                type = PrimitiveType.ARRAY;
                break;
            default:
                type = PrimitiveType.IDENTIFIER;
                break;
        }
        Symbol symbol;

        if(type != PrimitiveType.IDENTIFIER){
            symbol = new Symbol(name, type);
        } else {
            symbol = new ClassSymbol(name, strType);
        }
        argu.insert(name, symbol);
        

        return null;
    }


    /**
     * Grammar production:
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    @Override
    public String visit(AssignmentStatement n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String name = n.f0.accept(this, argu);

        if(argu.lookup(name) == null){
            throw new Exception("Next time, do us the favor and declare the variable " + name);
        }

        return super.visit(n, argu);
    }

    /**
     * Grammar production:
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */

    @Override
    public String visit(ArrayAssignmentStatement n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String name = n.f0.accept(this, argu);

        if(argu.lookup(name) == null){
            throw new Exception("Next time, do us the favor and declare the variable " + name);
        }
        return super.visit(n, argu);
    }

    /**
     * Grammar production:
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | BracketExpression()
     */

    @Override
    public String visit(PrimaryExpression n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String name = super.visit(n, argu);

        if(name != null){
            if(argu.lookup(name) == null){
                throw new Exception("Next time, do us the favor and declare the variable " + name);
            }
        }
        return null;
    }

    /**
     * Grammar production:
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    
    @Override
    public String visit(ClassDeclaration n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String name = n.f1.accept(this, argu);
        ClassDeclSymbol symbol = new ClassDeclSymbol(name);
        Map<String, Symbol> fields;
        Map<String, Symbol> methods;
        argu.insert(name, symbol);

        argu.enter();

        n.f3.accept(this, argu);

        argu.enter();

        n.f4.accept(this, argu);

        methods = argu.exit();
        fields = argu.exit();
        symbol.fields.putAll(fields);
        symbol.methods.putAll(methods);
        
        return null;

    }

    private void parentEnterHelper(ClassDeclSymbol parent, SymbolTable table){
        if(parent.parentClass == null){
            table.enter(parent.fields);
            table.enter(parent.methods);
        } else {
            parentEnterHelper(parent.parentClass, table);
            table.enter(parent.fields);
            table.enter(parent.methods);
        }
    }

    private void parentExitHelper(ClassDeclSymbol parent, SymbolTable table){
        if(parent.parentClass == null){
            table.exit();
            table.exit();
        } else {
            parentExitHelper(parent.parentClass, table);
            table.exit();
            table.exit();
        }
    }

    /**
     * Grammar production:
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */

    @Override
    public String visit(ClassExtendsDeclaration n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String className = n.f1.accept(this, argu); 
        String parentName = n.f3.accept(this, argu);
        Map<String, Symbol> fields;
        Map<String, Symbol> methods;

        ClassDeclSymbol parent = (ClassDeclSymbol) argu.lookup(parentName);
        ClassDeclSymbol symbol = new ClassDeclSymbol(className, parent);

        argu.insert(className, symbol);
        
        if(parent != null){
            parentEnterHelper(parent, argu);
        }
        // argu.print();


        argu.enter();

        n.f5.accept(this, argu);

        argu.enter();

        n.f6.accept(this, argu);
        
        // argu.print();
        methods = argu.exit();
        fields = argu.exit();
        symbol.fields.putAll(fields);
        symbol.methods.putAll(methods);
        
        parentExitHelper(parent, argu);
        
        return null;
    }

        /**
     * Grammar production:
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */

    @Override
    public String visit(MethodDeclaration n, SymbolTable argu) throws Exception {
        String type = n.f1.accept(this, argu);
        String methodName = n.f2.accept(this, argu);
        FunctionSymbol method = new FunctionSymbol(methodName, type);
        Map<String, Symbol> args;
        argu.insert(methodName, method);

        argu.enter();
        n.f4.accept(this, argu);

        args = argu.peek();
        method.args.putAll(args);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        

        n.f10.accept(this, argu);

        // argu.print();
        
        argu.exit();
        System.out.println("Args " + method.args.values());

        return null;
    }


    /**
     * Grammar production:
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public String visit(FormalParameter n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        // System.out.println("Parameter");
        String strType = n.f0.accept(this, argu);

        String name = n.f1.accept(this, argu);

        PrimitiveType type;

        switch(strType){
            case "int":
                type = PrimitiveType.INT;
                break;
            case "boolean":
                type = PrimitiveType.BOOLEAN;
                break;
            case "int[]":
                type = PrimitiveType.ARRAY;
                break;
            default:
                type = PrimitiveType.IDENTIFIER;
                break;
        }
        Symbol symbol;

        if(type != PrimitiveType.IDENTIFIER){
            symbol = new Symbol(name, type);
        } else {
            symbol = new ClassSymbol(name, strType);
        }
        argu.insert(name, symbol);
        

        return null;
    }


    @Override
    public String visit(ArrayType n, SymbolTable argu) {
        return "int[]";
    }

    public String visit(BooleanType n, SymbolTable argu) {
        return "boolean";
    }

    public String visit(IntegerType n, SymbolTable argu) {
        return "int";
    }

    @Override
    public String visit(Identifier n, SymbolTable argu) {
        return n.f0.toString();
    }

    
    
}
