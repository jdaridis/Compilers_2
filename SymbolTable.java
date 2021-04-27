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

    public void exit(){
        table.pop();
    }

    public void insert(String name, Symbol symbol) throws Exception {
        Map<String, Symbol> scope = table.peek();
        if(scope.containsKey(name)){
            throw new Exception(); // TODO Fix message.
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

    public void print(){
        for(Map<String, Symbol> scope: table){
            System.out.println(scope);
        }
    }
    
}
