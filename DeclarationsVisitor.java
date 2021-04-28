import syntaxtree.*;
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
        argu.insert(name, new ClassSymbol(name, name));

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
        String type = n.f0.accept(this, argu);

        String name = n.f1.accept(this, argu);

        argu.insert(name, new Symbol(name, type));
        

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
        argu.insert(name, new ClassSymbol(name, name));

        argu.enter();
        super.visit(n, argu);


        argu.exit();
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
        argu.insert(methodName, new FunctionSymbol(methodName, type));

        argu.enter();
        super.visit(n, argu);
        argu.print();
        
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
        String type = n.f0.accept(this, argu);

        String name = n.f1.accept(this, argu);

        argu.insert(name, new Symbol(name, type));

        return super.visit(n, argu);
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
