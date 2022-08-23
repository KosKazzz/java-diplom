package ru.netology.graphics.image;

public class ColorConverter implements TextColorSchema {
    char[] chars =  {'#', '$', '@', '%', '*', '+', '-', '\'' };
    @Override
    public char convert(int color){

        return  chars[(color/32)];
    }
}
