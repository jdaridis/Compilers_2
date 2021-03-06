import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {

    Deque<Map<String, Symbol>> table;
    ClassDeclSymbol thisSymbol;

    public SymbolTable() {
        table = new ArrayDeque<Map<String, Symbol>>();
    }

    public void enter(){
        Map<String, Symbol> scope = new LinkedHashMap<String, Symbol>();
        table.push(scope);
    }

    public void enter(Map<String, Symbol> parentScope){
        Map<String, Symbol> scope = new LinkedHashMap<String, Symbol>();
        scope.putAll(parentScope);
        table.push(scope);
        
    }

    public Map<String, Symbol> exit(){
        return table.pop();
    }

    public Map<String, Symbol> peek(){
        return table.peek();
    }

    public Symbol insert(String name, Symbol symbol) throws Exception {
        Map<String, Symbol> scope = table.peek();
        return scope.putIfAbsent(name, symbol);
    }

    public Symbol lookup(String name){
        boolean found = false;
        for(Map<String, Symbol> scope: table){
            if(!scope.containsKey(name)){
                continue;
            } else {
                found = true;
                return scope.get(name);
            }
        }

        if(!found){
            return null;
        }
        return null;
    }

    public Symbol lookupField(String name){
        boolean found = false;
        for(Map<String, Symbol> scope: table){
            if(!scope.containsKey(name)){
                continue;
            } else {
                found = true;
                if(scope.get(name) instanceof FunctionSymbol){
                    continue;
                }
                return scope.get(name);
            }
        }

        if(!found){
            return null;
        }
        return null;
    }

    public FunctionSymbol lookupMethod(String name){
        boolean found = false;
        for(Map<String, Symbol> scope: table){
            if(!scope.containsKey(name)){
                continue;
            } else {
                found = true;
                if(!(scope.get(name) instanceof FunctionSymbol)){
                    continue;
                }
                return (FunctionSymbol)scope.get(name);
            }
        }

        if(!found){
            return null;
        }
        return null;
    }

    public ClassDeclSymbol lookupType(String name){
        if(table.size() > 0){
            Map<String, Symbol> scope = table.getLast();
            if(scope.containsKey(name)){
                return (ClassDeclSymbol)scope.get(name);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void insertThis(String name){
        thisSymbol = lookupType(name);
    }

    public void insertThis(ClassDeclSymbol symbol){
        thisSymbol = symbol;
    }

    public ClassDeclSymbol getThis(){
        return thisSymbol;
    }

    public void print(){
        for(Map<String, Symbol> scope: table){
            System.out.println(scope);
        }
    }
    
}
