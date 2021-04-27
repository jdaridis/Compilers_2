enum PrimitiveType {
    INT("int", 4),
    ARRAY("int[]", 4),
    BOOLEAN("boolean", 1),
    IDENTIFIER;


    String typeName;
    int size;
    PrimitiveType(){

    }
    PrimitiveType(String name, int size){
        this.typeName = name;
        this.size = size;
    }

}

public class Symbol {
    String id;
    PrimitiveType type;
    public Symbol(String id, PrimitiveType type) {
        this.id = id;
        this.type = type;
    }

    public Symbol(String id, String strType) {
        this.id = id;
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
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return type.typeName + " " + id;
    }

    

}
