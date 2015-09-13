package com.prueba.utilidades;

public class Item {
    private int image;
    private int title;
 
    public Item() {
        super();
    }
 
    public Item(int image, int title, String url) {
        super();
        this.image = image;
        this.title = title;
    }
 
    public int getImage() {
        return image;
    }
 
    public void setImage(int image) {
        this.image = image;
    }
 
    public int getTitle() {
        return title;
    }
 
    public void setTitle(int title) {
        this.title = title;
    }
 
}
