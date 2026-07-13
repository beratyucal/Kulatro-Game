package kulatro;

public abstract class SpecialCard extends Card {
    private String effectDescription;
    private boolean isUsed;  

    // Creates a new SpecialCard instance.
    public SpecialCard(String type, String effectDescription) {
        super(type);
        this.effectDescription = effectDescription;
        this.isUsed = false;
    }

    // Checks whether the special card has been used.
    public boolean isUsed() {
        return isUsed;
    }

    // Marks the special card as used.
    public void markAsUsed() {
        this.isUsed = true;
    }

    // Returns the effect description.
    public String getEffectDescription() {
        return effectDescription;
    }

    
    public abstract void applyEffect(Player player);

   
    // Returns the card text representation.
    public String toString() {
        return getType() + " [Special: " + effectDescription + "]";
    }
    // Resets the current state.
    public void reset() {
        this.isUsed = false;
    }
}