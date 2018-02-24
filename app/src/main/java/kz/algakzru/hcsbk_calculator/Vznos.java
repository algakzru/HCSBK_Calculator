package kz.algakzru.hcsbk_calculator;

public class Vznos {

    private String name;
    private int price;
    private boolean box;


    public Vznos(String name, int price, boolean box) {
        this.name = name;
        this.price = price;
        this.box = box;
    }

    public boolean isBox() {
        return box;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
