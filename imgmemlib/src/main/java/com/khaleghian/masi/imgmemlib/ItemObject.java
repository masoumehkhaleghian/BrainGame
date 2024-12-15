package com.khaleghian.masi.imgmemlib;

public class ItemObject {
    String content,imageResource;

    public ItemObject(String content, String imageResource) {
        this.content = content;
        this.imageResource = imageResource;
    }

    public String getContent() {
        return content;
    }

    public String getImageResource() {
        return imageResource;
    }
}
