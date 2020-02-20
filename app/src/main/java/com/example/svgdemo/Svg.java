package com.example.svgdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.widget.Toast;

public class Svg {
    /**
     * 绘制路径
     */
    protected Path path;
    /**
     * 绘制颜色
     */
    private  int drawColor;
    protected  String  svgName;

    public String getSvgName() {
        return svgName;
    }

    public void setSvgName(String svgName) {
        this.svgName = svgName;
    }

    public Svg(Path path, int drawColor,String svgName) {
        this.path = path;
        this .drawColor=drawColor;
        this.svgName=svgName;
    }

    public  void draw (Canvas canvas, Paint paint, boolean isSelect) {
            paint.setStrokeWidth(5);
            paint.setColor(drawColor);//设置黑色
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8, 0, 0, 0xFFFFFF00);
            canvas.drawPath(path, paint);
    }

    /**
     * 是否被选中
     */
    public boolean isSelect(int x, int y) {
        //构造一个区域对象
        RectF rectF = new RectF();
//        计算控制点的边界
        path.computeBounds(rectF,true);
        Region region = new Region();
        region.setPath(path, new Region((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom));
        return region.contains(x,y);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

}
