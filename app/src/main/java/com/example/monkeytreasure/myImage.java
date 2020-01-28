package com.example.monkeytreasure;

public class myImage {
    private int image;
    private int id;
    private int index;

    public myImage(int image, int id, int index) {
        this.image = image;
        this.id = id;
        this.index = index;
    }

    public int getId() {
        return id;
    }


    public int getIndex() {
        return index;
    }


    public int getImage() {
        return image;
    }
}
