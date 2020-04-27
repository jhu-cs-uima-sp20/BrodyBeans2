package com.example.brodybeans2;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PictureContent {
    static final List<PictureItem> ITEMS = new ArrayList<>();

    static final List<String> espressoImageList = new ArrayList<>(Arrays.asList("https://superawesomevectors.com/wp-content/uploads/2017/04/flat-coffee-cup-icon-800x566.jpg"
    ,"https://www.iconsdb.com/icons/preview/orange/coffee-6-xxl.png",
    "https://cdn5.vectorstock.com/i/thumbs/87/14/simple-round-icon-of-coffee-cup-vector-13978714.jpg",
    "https://www.pngfind.com/pngs/m/333-3331514_coffee-emoji-icon-transparent-background-coffee-icons-hd.png"));


    public static void loadImage(File file) {
        PictureItem newItem = new PictureItem();
        newItem.uri = Uri.fromFile(file);
        //newItem.date = getDateFromUri(newItem.uri);
        addItem(newItem);
    }

    private static void addItem(PictureItem item) {
        ITEMS.add(0, item);
    }
}
