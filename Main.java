import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import syntaxtree.Goal;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length < 1){
            System.err.println("Usage: java Main <inputFile>");
            System.exit(1);
        }

        for(int i = 0; i < args.length;i++){
            FileInputStream fis = null;
            try{
                fis = new FileInputStream(args[i]);
                MiniJavaParser parser = new MiniJavaParser(fis);

                Goal root = parser.Goal();

                System.err.println("Program parsed successfully.");

                DeclarationsVisitor declarations = new DeclarationsVisitor();
                SymbolTable symbolTable = new SymbolTable();

                root.accept(declarations, symbolTable);
                // symbolTable.print();

                TypesVisitor typeCheck = new TypesVisitor();

                root.accept(typeCheck, symbolTable);


                for(Symbol s: symbolTable.peek().values()){
                    ClassDeclSymbol classSym = (ClassDeclSymbol)s;
                    int fieldOffset = computeClassSize(classSym.parentClass, symbolTable);
                    int methodOffset = 0;

                    for(Symbol field: classSym.fields.values()){
                        System.out.println(classSym.id + "." + field.id + ":" + fieldOffset);
                        fieldOffset += field.type.getSize();
                    }

                    for(Symbol method: classSym.methods.values()){
                        if(classSym.parentClass != null){
                            if(!classSym.parentClass.methods.containsKey(method.id)){
                                System.out.println(classSym.id + "." + method.id + ":" + methodOffset);
                            }
                        } else {
                            System.out.println(classSym.id + "." + method.id + ":" + methodOffset);
                        }
                        methodOffset += method.type.getSize();
                    }
                }
            }
            catch(ParseException ex){
                System.out.println(ex.getMessage());
            }
            catch(FileNotFoundException ex){
                System.err.println(ex.getMessage());
            }
            finally{
                try{
                    if(fis != null) fis.close();
                }
                catch(IOException ex){
                    System.err.println(ex.getMessage());
                }
            }
        }

        
    }


    public static int computeClassSize(ClassDeclSymbol symbol, SymbolTable table){
        int size;
        if(symbol != null && symbol.size != 0){
            return symbol.size;
        } else {
            if(symbol == null){
                return 0;
            } else {
                size = computeClassSize(symbol.parentClass, table);
                for(Symbol field: symbol.fields.values()){
                    size += field.type.getSize();
                }
                for(Symbol s: symbol.methods.values()){
                    size += symbol.size;
                }
                symbol.size = size;
                return size;
            }
        }
        
    }

}
