import Enums.*;

public class Card {
    private final Type type;
    private final Value value;
    private Color color = null;

    public Card(Type type, Value value) {
        this.type = type;
        this.value = value;
        if (this.type == Type.CLUBS || this.type == Type.SPADES)
            color = Color.BLACK;
        else if (this.type == Type.DIAMONDS || this.type == Type.HEARTS)
            color = Color.RED;
    }
    public Value getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return value + " " + type;
    }
}
