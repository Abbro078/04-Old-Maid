import Enums.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();

        for (Type type : Type.values()) {

            if (type == Type.JOKER) {
                continue;
            }

            for (Value value : Value.values()) {
                if (value == Value.JOKER) {
                    continue;
                }

                cards.add(new Card(type, value));
            }
        }
        cards.add(new Card(Type.JOKER, Value.JOKER));
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card drawCard() {
        if (!isEmpty()) {
            return cards.remove(cards.size() - 1);
        }
        return null;
    }

}
