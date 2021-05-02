public class TypeSymbol extends Symbol{

    public TypeSymbol(PrimitiveType type) {
        super(null, type);
    }

	public TypeSymbol(String id) {
		super(id, PrimitiveType.IDENTIFIER);
	}

    
}
