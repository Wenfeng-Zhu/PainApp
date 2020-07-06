package com.example.painapp;

import java.util.ArrayList;

//Used to store cross-activity variables

public class Container {

    public static ArrayList<String> colorList = new ArrayList<String>();
    public static ArrayList<String> typeList= new ArrayList<String>();
    public static String filePath;
    public static boolean ifImport = false;
    public static int typeColor;
    public static String typeName;
    public static float proportion;
    public static boolean move_action = true;
    public static boolean ifDisplay = false;
    public static boolean ifRedraw = false;

    static final int SELECTOR_RADIUS_DP = 9;
}
