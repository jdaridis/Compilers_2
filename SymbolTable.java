import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    Deque<Map<String, Symbol>> table;

    public SymbolTable() {
        table = new ArrayDeque<Map<String, Symbol>>();
    }

    public void enter(){
        Map<String, Symbol> scope = new HashMap<String, Symbol>();
        table.push(scope);
    }

    public void enter(Map<String, Symbol> parentScope){
        Map<String, Symbol> scope = new HashMap<String, Symbol>();
        scope.putAll(parentScope);
        table.push(scope);
        
    }

    public Map<String, Symbol> exit(){
        return table.pop();
    }

    public Map<String, Symbol> peek(){
        return table.peek();
    }

    public void insert(String name, Symbol symbol) throws Exception {
        Map<String, Symbol> scope = table.peek();
        if(scope.containsKey(name)){
            throw new Exception("Duplicate use of name " + name); // TODO Fix message.
        }
        scope.put(name, symbol);
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

    public ClassSymbol lookupType(String name){
        boolean found = false;
        if(table.size() > 0){
            Map<String, Symbol> scope = table.getLast();
            if(scope.containsKey(name)){
                return (ClassSymbol)scope.get(name);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void print(){
        for(Map<String, Symbol> scope: table){
            System.out.println(scope);
        }
    }
    
}
