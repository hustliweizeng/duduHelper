package com.dudu.helper3.widget;

public class WheelIndicatorItem 
{
	private float weight;
    private int color;
    private int width;

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public WheelIndicatorItem(){
        weight = 0;
    }

    public WheelIndicatorItem(float weight,int color,int width) 
    {
        if (weight < 0 )
            throw new IllegalArgumentException("weight value should be positive");

        this.weight = weight;
        this.color = color;
        this.width=width;
    }

    public void setWeight(float weight) {
        if (weight < 0 )
            throw new IllegalArgumentException("weight value should be positive");

        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

}
