package lk.lakshan.slotmachine;

public interface ISymbol {

    //Method to set the image
    void setImage(String imgName);

    //Method to get the image URL
    String getImage();

    //Method to set value for a symbol
    void setValue(int v);

    //Method to get value of a symbol
    int getValue();

}
