package com.droiddigger.adminapp;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mufad on 11/3/2016.
 */

public class Product {
    String name,price,tag, key,file;

    public Product() {
    }

    public Product(String name, String price, String tag, String key,String file) {
        this.name = name;
        this.price = price;
        this.tag = tag;
        this.key=key;
        this.file=file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return file;
    }

    public void setImage(Uri image) {
        this.file = file;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("price", price);
        result.put("photoUrl", file);
        result.put("tag", tag);
        result.put("key", key);
        return result;
    }
}
