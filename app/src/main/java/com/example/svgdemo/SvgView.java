package com.example.svgdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SvgView extends View {
    public static final String TAG = "SvgView";
    private Context context;
    private List<Svg> svgList;
    private Paint paint;//画笔
    private int[] colorArray = new int[]{0xffffff00 ,0xFF239BD7, 0xffff0000};
    private Svg SvgBeenSelect;//当前选中的图形
    private String[] svgname=new String[]{"1","2","3"};

public void init(Context context){
    this.context = context;
    paint = new Paint();
    paint.setAntiAlias(true);

    loadSVG();
}
    private void loadSVG() {
        svgList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = context.getResources().openRawResource(R.raw.smaple);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//获取DocumentBuilderFactory
                DocumentBuilder builder = null;

                try {
                    builder = factory.newDocumentBuilder();//从factory中获取DocumentBuilder 实例
                    Document doc = builder.parse(inputStream);
                    Element rootElement = doc.getDocumentElement();//dom解析
                    NodeList items = rootElement.getElementsByTagName("path");//把所有包含path的节点拿出来

                    for (int i = 0; i < items.getLength(); i++) {
                        Element element = (Element) items.item(i);
                        String pathData = element.getAttribute("android:pathData");//读取path的数据
                        Path path =  PathParser.createPathFromPathData(pathData);//通过工具类解析出Path

                        svgList.add(new Svg(path,colorArray[i],svgname[i]));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public SvgView(Context context) {
        super(context);
    }

    public SvgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (svgList != null) {
            Svg svg = null;
            for ( Svg item : svgList) {
                if (item.isSelect((int) (event.getX()), (int) (event.getY()))) {
                    svg = item;
                    Toast.makeText(context,svg.svgName,Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (svg != null) {
                SvgBeenSelect = svg;
                postInvalidate();
            }
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (svgList != null) {
            for (Svg item : svgList) {
                if (item != SvgBeenSelect) {
                    item.draw(canvas, paint, false);
                }
            }
            if (SvgBeenSelect != null) {
                SvgBeenSelect.draw(canvas, paint, true);
            }
        }
    }
}
