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
