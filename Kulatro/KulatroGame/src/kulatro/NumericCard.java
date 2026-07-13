package kulatro;

public class NumericCard extends Card {
	public int value;
	// Creates a new NumericCard instance.
	public NumericCard(String type, int value) {
		super(type);
		if (value < 1 || value > 9) {
            throw new IllegalArgumentException("Value must be between 1 and 9");
        }
        this.value = value;
	}
	// Returns the value.
	public int getValue() {
		return value;
		
	}
	// Returns the card text representation.
	public String toString() {
		return getType() +"-"+ getValue();
	}
	// Adds the bonus.
	public void addBonus(int bonus) {
	    this.value = Math.min(9, this.value + bonus);
	}
	private boolean locked = false;

	// Checks whether the card is locked.
	public boolean isLocked() {
	    return locked;
	}

	// Updates the locked state.
	public void setLocked(boolean locked) {
	    this.locked = locked;
	}

}
