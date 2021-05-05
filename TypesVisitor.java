import java.util.Map;

import syntaxtree.AllocationExpression;
import syntaxtree.ArrayAllocationExpression;
import syntaxtree.ArrayAssignmentStatement;
import syntaxtree.ArrayType;
import syntaxtree.AssignmentStatement;
import syntaxtree.BooleanType;
import syntaxtree.BracketExpression;
import syntaxtree.ClassDeclaration;
import syntaxtree.ClassExtendsDeclaration;
import syntaxtree.FalseLiteral;
import syntaxtree.FormalParameter;
import syntaxtree.Identifier;
import syntaxtree.IntegerLiteral;
import syntaxtree.IntegerType;
import syntaxtree.MainClass;
import syntaxtree.MethodDeclaration;
import syntaxtree.PrimaryExpression;
import syntaxtree.ThisExpression;
import syntaxtree.TrueLiteral;
import syntaxtree.VarDeclaration;
import visitor.GJDepthFirst;

public class TypesVisitor extends GJDepthFirst<PrimitiveType, SymbolTable>  {

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
    public PrimitiveType visit(MainClass n, SymbolTable argu) throws Exception {
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
    public PrimitiveType visit(VarDeclaration n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        PrimitiveType type = n.f0.accept(this, argu);

        String name = n.f1.accept(this, argu).getTypeName();;
        String typeName = type.getTypeName();;
        Symbol symbol;

        if(type != PrimitiveType.IDENTIFIER){
            symbol = new Symbol(name, type);
        } else {
            ClassDeclSymbol classSym = (ClassDeclSymbol)argu.lookupType(typeName);
            if(classSym != null){
                symbol = new ClassSymbol(name, classSym);
            } else {
                throw new Exception("Type " + typeName + " not defined");
            }
        }
        if(argu.insert(name, symbol) != null){
            throw new Exception("Duplicate use of name " + name); 
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
    public PrimitiveType visit(AssignmentStatement n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String name = n.f0.accept(this, argu).getTypeName();

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
    public PrimitiveType visit(ArrayAssignmentStatement n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String name = n.f0.accept(this, argu).getTypeName();

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
    public PrimitiveType visit(PrimaryExpression n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        return super.visit(n, argu);
    }
    
    /**
     * Grammar production:
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */

    @Override
	public PrimitiveType visit(ArrayAllocationExpression n, SymbolTable argu) throws Exception {
		// TODO Auto-generated method stub
		return PrimitiveType.ARRAY;
	}

    /**
     * Grammar production:
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */

    @Override
	public PrimitiveType visit(AllocationExpression n, SymbolTable argu) throws Exception {
		// TODO Auto-generated method stub
        PrimitiveType type = n.f1.accept(this, argu);
        String typeName = type.getTypeName();

        if(type != PrimitiveType.IDENTIFIER){
            throw new Exception("Cannot instantiate " + typeName);
        } else if(argu.lookupType(typeName) == null){
            throw new Exception("Type " + typeName + " not defined");
        }
		return type;
	}

    /**
     * Grammar production:
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    @Override
	public PrimitiveType visit(BracketExpression n, SymbolTable argu) throws Exception {
		// TODO Auto-generated method stub
		return n.f1.accept(this, argu);
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
    public PrimitiveType visit(ClassDeclaration n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String name = n.f1.accept(this, argu).getTypeName();
        ClassDeclSymbol symbol = argu.lookupType(name);

        argu.enter();

        n.f3.accept(this, argu);

        argu.enter(symbol.methods);

        n.f4.accept(this, argu);

        argu.exit();
        argu.exit();

        
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
    public PrimitiveType visit(ClassExtendsDeclaration n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        String className = n.f1.accept(this, argu).getTypeName(); 
        String parentName = n.f3.accept(this, argu).getTypeName();
        Map<String, Symbol> fields;
        Map<String, Symbol> methods;

        ClassDeclSymbol parent = (ClassDeclSymbol) argu.lookup(parentName);

        if(parent == null){
            throw new Exception("Type " + parentName + " not declared in file");
        }

        ClassDeclSymbol symbol = new ClassDeclSymbol(className, parent);
        
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
    public PrimitiveType visit(MethodDeclaration n, SymbolTable argu) throws Exception {
        n.f1.accept(this, argu);

        String methodName = n.f2.accept(this, argu).getTypeName();

        FunctionSymbol method = (FunctionSymbol)argu.lookup(methodName);
        Map<String, Symbol> args;

        argu.enter();
        n.f4.accept(this, argu);


        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        

        PrimitiveType returnType = n.f10.accept(this, argu);

        

        
        argu.exit();

        return null;
    }


    /**
     * Grammar production:
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public PrimitiveType visit(FormalParameter n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        // System.out.println("Parameter");
        PrimitiveType type = n.f0.accept(this, argu);

        String name = n.f1.accept(this, argu).getTypeName();;
        String typeName = type.getTypeName();;
        Symbol symbol;

        if(type != PrimitiveType.IDENTIFIER){
            symbol = new Symbol(name, type);
        } else {
            ClassDeclSymbol classSym = (ClassDeclSymbol)argu.lookupType(typeName);
            if(classSym != null){
                symbol = new ClassSymbol(name, classSym);
            } else {
                throw new Exception("Type " + typeName + " not defined");
            }
        }
        if(argu.insert(name, symbol) != null){
            throw new Exception("Duplicate use of name " + name);
        }
        

        return null;
    }
    
    @Override
	public PrimitiveType visit(IntegerLiteral n, SymbolTable argu) throws Exception {
		// TODO Auto-generated method stub
		return PrimitiveType.INT;
	}

	@Override
	public PrimitiveType visit(TrueLiteral n, SymbolTable argu) throws Exception {
		// TODO Auto-generated method stub
		return PrimitiveType.BOOLEAN;
	}

	@Override
	public PrimitiveType visit(FalseLiteral n, SymbolTable argu) throws Exception {
		// TODO Auto-generated method stub
		return PrimitiveType.BOOLEAN;
	}

	@Override
	public PrimitiveType visit(ThisExpression n, SymbolTable argu) throws Exception {
		// TODO Auto-generated method stub
        PrimitiveType type = PrimitiveType.IDENTIFIER;
        type.setTypeName(n.f0.toString());
        return type;
	}

    @Override
    public PrimitiveType visit(ArrayType n, SymbolTable argu) {
        return PrimitiveType.ARRAY;
    }

    public PrimitiveType visit(BooleanType n, SymbolTable argu) {
        return PrimitiveType.BOOLEAN;
    }

    public PrimitiveType visit(IntegerType n, SymbolTable argu) {
        return PrimitiveType.INT;
    }

    @Override
    public PrimitiveType visit(Identifier n, SymbolTable argu) {
        PrimitiveType type = PrimitiveType.IDENTIFIER;
        type.setTypeName(n.f0.toString());
        return type;
    }

    
    
}
