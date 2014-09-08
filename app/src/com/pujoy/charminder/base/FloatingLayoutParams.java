package com.pujoy.charminder.base;

import android.view.Gravity;
import android.view.WindowManager;
import static com.pujoy.charminder.base.ViewBase.getScreenWidth;
import static com.pujoy.charminder.base.ViewBase.getScreenHeight;
import static com.pujoy.charminder.base.ViewBase.isRotated;

public class FloatingLayoutParams extends WindowManager.LayoutParams {
	public FloatingLayoutParams(){
		super();
        type = WindowManager.LayoutParams.TYPE_PHONE;   
        format = android.graphics.PixelFormat.RGBA_8888; 
        flags = FLAG_NOT_FOCUSABLE;  
        gravity = Gravity.LEFT | Gravity.TOP;
	}

	public FloatingLayoutParams(FloatingLayoutParams params){
		// Clone
		super();
        type = WindowManager.LayoutParams.TYPE_PHONE;   
        format = android.graphics.PixelFormat.RGBA_8888; 
        flags = FLAG_NOT_FOCUSABLE;  
        gravity = Gravity.LEFT | Gravity.TOP;
        x = params.y;
        y = params.x;
        width = params.height;
        height = params.width;
	}
	

	public void setX(int newX){
		if (isRotated()){
			y = xInsideScreen(newX);
		}else{
			x = xInsideScreen(newX);
		}
	}
	
	int xInsideScreen(int newX){
		return newX < 0? 0 : newX + getWidth() > getScreenWidth()? 
				getScreenWidth() - getWidth(): newX;
	}
	
	public void setY(int newY){
		if (isRotated()){
			x = yInsideScreen(newY);
		}else{
			y = yInsideScreen(newY);
		}
	}
	
	int yInsideScreen(int newY){
		return newY < 0? 0 : newY + getHeight() > getScreenHeight()? 
				getScreenHeight() - getHeight(): newY;
	}
	
	public int getX(){
		if (isRotated()) return y;
		return x;
	}
	
	public int getY(){
		if (isRotated()) return x;
		return y;
	}
	
	public int getWidth(){
		if (isRotated()) return height;
		return width;
	}
	
	public int getHeight(){
		if (isRotated()) return width;
		return height;
	}
	
	public void setWidth(int newWidth){
		if (isRotated()){
			height = newWidth;
		}else{
			width = newWidth;
		}
	}
	
	public void setHeight(int newHeight){
		if (isRotated()){
			width = newHeight;
		}else{
			height = newHeight;
		}
	}
	
}
