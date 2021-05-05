import java.util.Map;

import syntaxtree.AllocationExpression;
import syntaxtree.AndExpression;
import syntaxtree.ArrayAllocationExpression;
import syntaxtree.ArrayAssignmentStatement;
import syntaxtree.ArrayLength;
import syntaxtree.ArrayLookup;
import syntaxtree.ArrayType;
import syntaxtree.AssignmentStatement;
import syntaxtree.BooleanType;
import syntaxtree.BracketExpression;
import syntaxtree.ClassDeclaration;
import syntaxtree.ClassExtendsDeclaration;
import syntaxtree.Clause;
import syntaxtree.CompareExpression;
import syntaxtree.Expression;
import syntaxtree.FalseLiteral;
import syntaxtree.FormalParameter;
import syntaxtree.Identifier;
import syntaxtree.IfStatement;
import syntaxtree.IntegerLiteral;
import syntaxtree.IntegerType;
import syntaxtree.MainClass;
import syntaxtree.MessageSend;
import syntaxtree.MethodDeclaration;
import syntaxtree.MinusExpression;
import syntaxtree.NotExpression;
import syntaxtree.PlusExpression;
import syntaxtree.PrimaryExpression;
import syntaxtree.PrintStatement;
import syntaxtree.ThisExpression;
import syntaxtree.TimesExpression;
import syntaxtree.TrueLiteral;
import syntaxtree.VarDeclaration;
import syntaxtree.WhileStatement;
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
        String typeName = type.getTypeName();

        String name = n.f1.accept(this, argu).getTypeName();
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

        Symbol symbol = argu.lookup(name);

        if(symbol == null){
            throw new Exception("Next time, do us the favor and declare the variable " + name);
        }

        PrimitiveType exprType = n.f2.accept(this, argu);

        if(symbol.type != exprType){
            throw new Exception("Wrong type assignment. Expected: " + symbol.type.typeName + " but got: " + exprType.typeName);
        } else if(symbol.type == PrimitiveType.IDENTIFIER){
            ClassSymbol classSymbol = (ClassSymbol)symbol;

            ClassDeclSymbol exprClass = argu.lookupType(exprType.getTypeName());

            if(!classSymbol.isParentOf(exprClass)){
                throw new Exception("Type " + exprClass.id + " not instance of " + classSymbol.className);
            }
        }

        return null;
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
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */

    @Override
    public PrimitiveType visit(IfStatement n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub

        PrimitiveType expression = n.f2.accept(this, argu);

        if(expression != PrimitiveType.BOOLEAN){
            throw new Exception("Only boolean expression allowed");
        }

        return null;
    }

    /**
     * Grammar production:
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */

    @Override
    public PrimitiveType visit(WhileStatement n, SymbolTable argu) throws Exception {
        PrimitiveType expression = n.f2.accept(this, argu);

        if(expression != PrimitiveType.BOOLEAN){
            throw new Exception("Only boolean expression allowed");
        }

        return null;
    }

    /**
     * Grammar production:
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */

    @Override
    public PrimitiveType visit(PrintStatement n, SymbolTable argu) throws Exception {
        PrimitiveType expression = n.f2.accept(this, argu);

        if(expression != PrimitiveType.INT){
            throw new Exception("Only int expression allowed");
        }

        return null;
    }

    /**
     * Grammar production:
     * f0 -> AndExpression()
     *       | CompareExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | Clause()
     */
    
    @Override
    public PrimitiveType visit(Expression n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        return n.f0.accept(this, argu);
    }

    
    /**
     * Grammar production:
     * f0 -> Clause()
     * f1 -> "&&"
     * f2 -> Clause()
     */
    @Override
    public PrimitiveType visit(AndExpression n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub

        PrimitiveType clause1 = n.f0.accept(this, argu);
        PrimitiveType clause2 = n.f2.accept(this, argu);

        if(clause1 != PrimitiveType.BOOLEAN || clause2 != PrimitiveType.BOOLEAN){
            throw new Exception("Types must be boolean");
        }

        return PrimitiveType.BOOLEAN;
    }

    /**
     * Grammar production:
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */

    @Override
    public PrimitiveType visit(CompareExpression n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        PrimitiveType expr1 = n.f0.accept(this, argu);
        PrimitiveType expr2 = n.f2.accept(this, argu);

        if(expr1 != PrimitiveType.INT || expr2 != PrimitiveType.INT){
            throw new Exception("Types must be integers");
        }

        return PrimitiveType.BOOLEAN;
    }

    /**
     * Grammar production:
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */

    @Override
    public PrimitiveType visit(PlusExpression n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        PrimitiveType expr1 = n.f0.accept(this, argu);
        PrimitiveType expr2 = n.f2.accept(this, argu);

        if(expr1 != PrimitiveType.INT || expr2 != PrimitiveType.INT){
            throw new Exception("Types must be integers");
        }

        return PrimitiveType.INT;
    }

    /**
     * Grammar production:
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */

    @Override
    public PrimitiveType visit(MinusExpression n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        PrimitiveType expr1 = n.f0.accept(this, argu);
        PrimitiveType expr2 = n.f2.accept(this, argu);

        if(expr1 != PrimitiveType.INT || expr2 != PrimitiveType.INT){
            throw new Exception("Types must be integers");
        }

        return PrimitiveType.INT;
    }

    /**
     * Grammar production:
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */

    @Override
    public PrimitiveType visit(TimesExpression n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        PrimitiveType expr1 = n.f0.accept(this, argu);
        PrimitiveType expr2 = n.f2.accept(this, argu);

        if(expr1 != PrimitiveType.INT || expr2 != PrimitiveType.INT){
            throw new Exception("Types must be integers");
        }

        return PrimitiveType.INT;
    }

        /**
     * Grammar production:
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */

    @Override
    public PrimitiveType visit(ArrayLookup n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        PrimitiveType expr1 = n.f0.accept(this, argu);
        PrimitiveType expr2 = n.f2.accept(this, argu);

        if(expr1 != PrimitiveType.ARRAY){
            throw new Exception("Type must be array");
        }

        if(expr2 != PrimitiveType.INT){
            throw new Exception("Array index must be an integer");
        }

        return PrimitiveType.INT;
    }

    /**
     * Grammar production:
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */

    @Override
    public PrimitiveType visit(ArrayLength n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        PrimitiveType expr1 = n.f0.accept(this, argu);

        if(expr1 != PrimitiveType.ARRAY){
            throw new Exception("Type must be array");
        }

        return PrimitiveType.INT;
    }

    @Override
    public PrimitiveType visit(MessageSend n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        return super.visit(n, argu);
    }

    /**
     * Grammar production:
     * f0 -> NotExpression()
     *       | PrimaryExpression()
     */

    @Override
    public PrimitiveType visit(Clause n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        return n.f0.accept(this, argu);
    }

    /**
     * Grammar production:
     * f0 -> "!"
     * f1 -> Clause()
     */

    @Override
    public PrimitiveType visit(NotExpression n, SymbolTable argu) throws Exception {
        // TODO Auto-generated method stub
        PrimitiveType expr1 = n.f1.accept(this, argu);

        if(expr1 != PrimitiveType.BOOLEAN){
            throw new Exception("Type must be boolean");
        }

        return PrimitiveType.BOOLEAN;
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


        PrimitiveType type = n.f0.accept(this, argu);
        String name = type.getTypeName();
        Symbol symbol;


        // System.out.println("PrimitiveType " + type + " "+ name);
        if(name.equals("this")){
            return type;
        }

        if(type == PrimitiveType.IDENTIFIER){
            symbol = argu.lookup(name);
            type = symbol.type;
        }

        return type;
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

        PrimitiveType expr1 = n.f3.accept(this, argu);

        if(expr1 != PrimitiveType.INT){
            throw new Exception("Array index must be an integer");
        }

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

    private void parentEnterHelper(ClassDeclSymbol parent, SymbolTable table){
        if(parent.parentClass == null){
            table.enter(parent.fields);
        } else {
            parentEnterHelper(parent.parentClass, table);
            table.enter(parent.fields);
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

        ClassDeclSymbol parent = argu.lookupType(parentName);

        if(parent == null){
            throw new Exception("Type " + parentName + " not declared in file");
        }

        ClassDeclSymbol symbol = argu.lookupType(className);
        
        if(parent != null){
            parentEnterHelper(parent, argu);
        }
        // argu.print();


        argu.enter();

        n.f5.accept(this, argu);

        argu.enter(symbol.methods);

        n.f6.accept(this, argu);
        
        argu.exit();
        argu.exit();
        
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
        PrimitiveType returnType = n.f1.accept(this, argu);
        String returnTypeName = returnType.getTypeName();
        ClassDeclSymbol classSym = null;
        if(returnType == PrimitiveType.IDENTIFIER){
            classSym = (ClassDeclSymbol)argu.lookupType(returnTypeName);
            if(classSym == null){
                throw new Exception("Type " + returnTypeName + " not defined");
            }
        }


        n.f2.accept(this, argu);

        argu.enter();

        n.f4.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        

        PrimitiveType expressionType = n.f10.accept(this, argu);

        if(expressionType != returnType){
            throw new Exception("Return type mismatch");
        } else if(expressionType == PrimitiveType.IDENTIFIER){
            ClassDeclSymbol exprClass = argu.lookupType(expressionType.getTypeName());
            ClassSymbol exprSymbol;
            if(exprClass == null){
                throw new Exception("Type " + expressionType.getTypeName() + " not defined");
            } else {
                exprSymbol = new ClassSymbol("return", exprClass);
            }
            
            if(!exprSymbol.isInstanceOf(classSym)) {
                throw new Exception("Type " + exprSymbol.className + " not instance of " + classSym.id);
            }
            
        }

        
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
