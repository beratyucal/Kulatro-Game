package kulatro;

public abstract class Card {
	private String type;
	
	// Creates a new Card instance.
	public Card(String type) {
		this.type=type;
	}
	// Returns the type.
	public String getType() {
		return type;
	
	}
	public abstract String toString() ;
	
		

}
