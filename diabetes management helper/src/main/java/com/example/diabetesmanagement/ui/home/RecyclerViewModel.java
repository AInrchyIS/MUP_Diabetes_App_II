package com.example.diabetesmanagement.ui.home;

public class RecyclerViewModel
{
    int img;
    String header, desc;

    public RecyclerViewModel(int img, String header, String desc) {
        this.img = img;
        this.header = header;
        this.desc = desc;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
