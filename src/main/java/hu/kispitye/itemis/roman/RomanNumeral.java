package hu.kispitye.itemis.roman;

public enum RomanNumeral {
	I(1),
	V(5) {
	    @Override
		public boolean canBeSubtracted() {
	    	return false;
	    }
    },
	X(10),
	L(50) {
	    @Override
		public boolean canBeSubtracted() {
	    	return false;
	    }
    },
	C(100),
	D(500) {
	    @Override
		public boolean canBeSubtracted() {
	    	return false;
	    }
    },
	M(1000);
	
	private final int value;
    private RomanNumeral(int value) {
        this.value = value;
    }
    public int value() {
    	return value;
    }
    
    public int getMaxSuccession() {
    	return canBeSubtracted() ? 3 : 1;
    }
    
    public boolean canBeSubtracted() {
    	return true;
    }

}
