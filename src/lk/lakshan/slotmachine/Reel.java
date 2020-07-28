package lk.lakshan.slotmachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reel {

    //A List contains six symbols
    private List<Symbol> symbolList;

    //Constructor of Reel
    public Reel() {
        /*
        * If the symbolList is empty(First time), it will initialize, add Symbols and shuffle.
        * Otherwise, only shuffling will be done
        * */
        if(this.symbolList==null) {
            this.symbolList = new ArrayList<>();
            this.symbolList.add(new Symbol("cherry", 2));
            this.symbolList.add(new Symbol("lemon", 3));
            this.symbolList.add(new Symbol("plum", 4));
            this.symbolList.add(new Symbol("watermelon", 6));
            this.symbolList.add(new Symbol("bell", 8));
            this.symbolList.add(new Symbol("redseven", 10));
        }

        //Shuffling of symbolList
        Collections.shuffle(symbolList);
    }

    //Getter method for symbolList
    public List<Symbol> spin() {
        return symbolList;
    }
}
