enum PrimitiveType {
    INT("int", 4),
    ARRAY("int[]", 8),
    BOOLEAN("boolean", 1),
    IDENTIFIER(8);


    String typeName;
    int size;
    PrimitiveType(int size){
        this.size = size;
    }
    PrimitiveType(String name, int size){
        this.typeName = name;
        this.size = size;
    }

    public static PrimitiveType strToPrimitiveType(String strType){
        PrimitiveType type;
        switch(strType){
            case "int":
                type = INT;
                break;
            case "boolean":
                type = BOOLEAN;
                break;
            case "int[]":
                type = ARRAY;
                break;
            default:
                type = IDENTIFIER;
                break;
        }
        return type;
    }

}

public class Symbol {
    String id;
    PrimitiveType type;
    int size;
    public Symbol(String id, PrimitiveType type) {
        this.id = id;
        this.type = type;
    }

    public Symbol(String id, String strType) {
        this.id = id;
        type = PrimitiveType.strToPrimitiveType(strType);
    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return type.typeName + " " + id;
    }

    

}
