package lk.lakshan.slotmachine;

public class Symbol implements ISymbol {

    private String imgURL;//image URL of Symbol
    private int value;//Value of the Symbol

    //Constructor for the Symbol
    public Symbol(String imgName, int value) {
        this.imgURL = "images/"+imgName+".png";
        this.value = value;
    }

    //Compare Method
    public static boolean compareSymbols(Symbol s1, Symbol s2){
        if(s1.value == s2.value){
            return true;
        }
        return false;
    }

    @Override
    public void setImage(String imgName) {
        this.imgURL = "images/"+imgName+".png";
    }

    @Override
    public String getImage() {
        return this.imgURL;
    }

    @Override
    public void setValue(int v) {
    this.value = v;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
