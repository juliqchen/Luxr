package com.example.luxr;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jenniferhu on 7/11/17.
 */

public class MakeHash {

    public static HashMap<String, HashMap<String, ArrayList<String>>> closet;
    private static HashMap<String, ArrayList<String>> topColor;
    private static HashMap<String, ArrayList<String>> bottomColor;
    private static HashMap<String, ArrayList<String>> dressColor;
    private static HashMap<String, ArrayList<String>> jumpsuitColor;
    private static HashMap<String, ArrayList<String>> shoeColor;

    public MakeHash() {
        closet = new HashMap<>();

        topColor = new HashMap<>();
        bottomColor = new HashMap<>();
        dressColor = new HashMap<>();
        jumpsuitColor = new HashMap<>();
        shoeColor = new HashMap<>();

        topColor = putColors(topColor);
        bottomColor = putColors(bottomColor);
        dressColor = putColors(dressColor);
        jumpsuitColor = putColors(jumpsuitColor);
        shoeColor = putColors(shoeColor);

        closet.put("Tops", topColor);
        closet.put("Bottoms", bottomColor);
        closet.put("Dresses", dressColor);
        closet.put("Jumpsuits", jumpsuitColor);
        closet.put("Shoes", shoeColor);
    }

    private HashMap<String, ArrayList<String>> putColors(HashMap<String, ArrayList<String>> map) {
        map.put("Red", new ArrayList<String>());
        map.put("Blue", new ArrayList<String>());
        map.put("Green", new ArrayList<String>());
        map.put("Yellow", new ArrayList<String>());
        map.put("Brown", new ArrayList<String>());
        map.put("Orange", new ArrayList<String>());
        map.put("Purple", new ArrayList<String>());
        map.put("Pink", new ArrayList<String>());
        map.put("Silver", new ArrayList<String>());
        map.put("White", new ArrayList<String>());
        map.put("Grey", new ArrayList<String>());
        map.put("Black", new ArrayList<String>());
        map.put("Gold", new ArrayList<String>());
        return map;
    }


}
