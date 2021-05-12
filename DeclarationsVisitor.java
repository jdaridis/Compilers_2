import java.util.Arrays;
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
        String arg = n.f11.accept(this, argu);

        Symbol symbol = new ClassSymbol(arg, "String[]");
        argu.enter();

        argu.insert(arg, symbol);
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

        PrimitiveType type = PrimitiveType.strToPrimitiveType(strType);

        Symbol symbol;

        if(type != PrimitiveType.IDENTIFIER){
            symbol = new Symbol(name, type);
        } else {
            symbol = new ClassSymbol(name, strType);
        }
        if(argu.insert(name, symbol) != null){
            throw new DuplicateDeclarationException(name);
            // throw new Exception("Duplicate use of name " + name); 
        }
        

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
            throw new DeclarationException(name);
            // throw new Exception("Next time, do us the favor and declare the variable " + name);
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
            throw new DeclarationException(name);
            // throw new Exception("Next time, do us the favor and declare the variable " + name);
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
        String name = n.f0.accept(this, argu);

        if(name != null){
            if(argu.lookup(name) == null){
                throw new DeclarationException(name);
                // throw new Exception("Next time, do us the favor and declare the variable " + name);
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
        if(argu.insert(name, symbol) != null){
            throw new DuplicateDeclarationException(name);
        }
        argu.insertThis(symbol);

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

    private void parentEnterHelper(ClassDeclSymbol parent, SymbolTable table, Map<String, Symbol> methods){
        if(parent.parentClass == null){
            table.enter(parent.fields);
            methods.putAll(parent.methods);
        } else {
            parentEnterHelper(parent.parentClass, table, methods);
            table.enter(parent.fields);
            methods.putAll(parent.methods);
        }
    }

    private void parentExitHelper(ClassDeclSymbol parent, SymbolTable table){
        if(parent.parentClass == null){
            table.exit();
        } else {
            parentExitHelper(parent.parentClass, table);
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

        if(parent == null){
            throw new Exception("Type " + parentName + " not declared in file");
        }

        ClassDeclSymbol symbol = new ClassDeclSymbol(className, parent);
        if(argu.insert(className, symbol) != null){
            throw new DuplicateDeclarationException(className);
        }

        argu.insertThis(symbol);
        
        if(parent != null){
            parentEnterHelper(parent, argu, symbol.methods);
        }
        // argu.print();


        argu.enter();

        n.f5.accept(this, argu);

        argu.enter(symbol.methods);

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

        if(argu.lookupType(methodName) != null){
            throw new Exception("Cannot have method with name of class");
        }
        FunctionSymbol method = new FunctionSymbol(methodName, type);
        Map<String, Symbol> args;
        Symbol oldMethod;

        oldMethod = argu.insert(methodName, method);

        argu.enter();
        n.f4.accept(this, argu);
        args = argu.peek();
        method.args.putAll(args);

        

        if(oldMethod != null) {
            if(argu.getThis().parentClass != null && (argu.getThis().parentClass.methods.containsKey(methodName))){
                method.checkOverride((FunctionSymbol)oldMethod);
            } else {
                throw new DuplicateDeclarationException(methodName);
                // throw new Exception("Duplicate use of name " + methodName);
            }

        }

        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        

        n.f10.accept(this, argu);

        // argu.print();
        
        argu.exit();

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

        PrimitiveType type = PrimitiveType.strToPrimitiveType(strType);

        Symbol symbol;

        if(type != PrimitiveType.IDENTIFIER){
            symbol = new Symbol(name, type);
        } else {
            symbol = new ClassSymbol(name, strType);
        }
        if(argu.insert(name, symbol) != null){
            throw new DuplicateDeclarationException(name);
            // throw new Exception("Duplicate use of name " + name);
        }
        

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
