public class TypeSymbol extends Symbol{

    public TypeSymbol(PrimitiveType type) {
        super(type.typeName, type);
    }

	public TypeSymbol(String id) {
		super(id, PrimitiveType.IDENTIFIER);
	}

    public String getTypeName(){
        return this.id;
    }

    
}
