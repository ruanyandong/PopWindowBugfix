package com.example.popuptest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {



    private Button showAs;
    private Button showAt;

    private fixPop popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAs = findViewById(R.id.showAsDropDown);
        showAt = findViewById(R.id.showAtLocation);

        popupWindow = new fixPop(this);
        popupWindow.setHeight(300);
        popupWindow.setWidth(300);
        final TextView view = new TextView(this);
        view.setText("Content");
        view.setGravity(Gravity.CENTER);
        popupWindow.setContentView(view);

        showAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //popupWindow.showAsDropDown(showAs);
                //popupWindow.showAsDropDown(showAs,100,100);
                //popupWindow.showAsDropDown(showAs,200,200, Gravity.RIGHT);
                //popupWindow.showAtLocation(v,Gravity.LEFT,0,0);
                //popupWindow.update();
                //showPopAtLocation(v,Gravity.BOTTOM,0,0);
                //popupWindow.update(v,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                //全屏显示解决方式一
//                if (Build.VERSION.SDK_INT >= 24){
//                    Rect rect = new Rect();
//                    showAs.getGlobalVisibleRect(rect);
//                    int height = showAs.getResources().getDisplayMetrics().heightPixels-rect.bottom;
//                    popupWindow.setHeight(height);
//                    popupWindow.showAsDropDown(showAs,0,0);
//                }else {
//                    popupWindow.showAsDropDown(showAs,0,0);
//                }

                // 全屏解决解决方式二
//                if (Build.VERSION.SDK_INT >= 24){
//                    int[] location = new int[2];
//                    showAs.getLocationOnScreen(location);
//                    int offSetY = location[1]+showAs.getHeight();
//                    if (Build.VERSION.SDK_INT >= 25){
//                        int screenHeight = showAs.getResources().getDisplayMetrics().heightPixels;
//                        popupWindow.setHeight(screenHeight-offSetY);
//                    }
//                    popupWindow.showAtLocation(showAs,Gravity.NO_GRAVITY,0,offSetY);
//                    //popupWindow.showAsDropDown(showAs,0,offSetY);
//                    // 两个都对
//                }else {
//                    popupWindow.showAsDropDown(showAs,0,0);
//                }
//                //showPopupWindow(showAs,10,10);

                showPopAtLocation(v,Gravity.RIGHT,0,0,300,300);


            }

        });

        showAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopAtLocation(v,Gravity.BOTTOM,0,0,100,100);
            }
        });


    }


    public void showPopupWindow(View view,int xoff,int yoff){
        //全屏显示解决方式一
//                if (Build.VERSION.SDK_INT >= 24){
//                    Rect rect = new Rect();
//                    showAs.getGlobalVisibleRect(rect);
//                    int height = showAs.getResources().getDisplayMetrics().heightPixels-rect.bottom-yoff;
//                    popupWindow.setHeight(height);
//                    popupWindow.showAsDropDown(showAs,xoff,yoff);
//                }else {
//                    popupWindow.showAsDropDown(showAs,xoff,yoff);
//                }

        // 全屏解决解决方式二
//        if (Build.VERSION.SDK_INT >= 24){
//            int[] location = new int[2];
//            showAs.getLocationOnScreen(location);
//            int offSetY = location[1]+showAs.getHeight();
//            if (Build.VERSION.SDK_INT >= 25){
//                int screenHeight = showAs.getResources().getDisplayMetrics().heightPixels;
//                popupWindow.setHeight(screenHeight-offSetY-yoff);
//            }
//            popupWindow.showAtLocation(showAs,Gravity.NO_GRAVITY,xoff,offSetY+yoff);
//            //popupWindow.showAsDropDown(showAs,0,offSetY);
//            // 两个都对
//        }else {
//            popupWindow.showAsDropDown(showAs,xoff,yoff);
//        }
    }


    public void showPopAtLocation(View parent,int gravity,int x,int y,int width,int height){
        if (popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        if (Build.VERSION.SDK_INT != 24){
            popupWindow.showAtLocation(parent,gravity,x,y);
            popupWindow.update();
            return;
        }
        //popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        int screenHeight = parent.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = parent.getResources().getDisplayMetrics().widthPixels;
        if (gravity == Gravity.BOTTOM){
            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY,screenWidth/2-popupWindow.getWidth()/2+x, screenHeight - popupWindow.getHeight()+y);
            popupWindow.update();
        }else if (gravity == Gravity.RIGHT){
            popupWindow.showAtLocation(parent,Gravity.NO_GRAVITY,screenWidth-popupWindow.getWidth()+x,screenHeight/2-popupWindow.getHeight()/2+y);
            popupWindow.update();
        }else {
            popupWindow.showAtLocation(parent,gravity,x,y);
            popupWindow.update();
        }

    }

    public void showPopAtLocationV2(View parent,int gravity,int x,int y){
        if (Build.VERSION.SDK_INT != 24){
            popupWindow.showAtLocation(parent,gravity,x,y);
            //popupWindow.update();
            return;
        }
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int screenHeight = parent.getResources().getDisplayMetrics().heightPixels;
        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY,x, screenHeight - popupWindow.getContentView().getMeasuredHeight()-y);
        popupWindow.update();
    }


    class fixPop extends PopupWindow{

        public fixPop(Context context) {
            super(context);
        }

        /**
         * 反射获取对象
         * @param paramName
         * @return
         */
        private Object getParam(String paramName) {
            if (TextUtils.isEmpty(paramName)) {
                return null;
            }
            try {
                Field field = PopupWindow.class.getDeclaredField(paramName);
                field.setAccessible(true);
                return field.get(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 反射赋值对象
         * @param paramName
         * @param obj
         */
        private void setParam(String paramName, Object obj) {
            if (TextUtils.isEmpty(paramName)) {
                return;
            }
            try {
                Field field = PopupWindow.class.getDeclaredField(paramName);
                field.setAccessible(true);
                field.set(this, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 反射执行方法
         * @param methodName
         * @param args
         * @return
         */
        private Object execMethod(String methodName, Class[] cls, Object[] args) {
            if (TextUtils.isEmpty(methodName)) {
                return null;
            }
            try {
                Method method = getMethod(PopupWindow.class, methodName, cls);
                method.setAccessible(true);
                return method.invoke(this, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 利用递归找一个类的指定方法，如果找不到，去父亲里面找直到最上层Object对象为止。
         *
         * @param clazz
         *            目标类
         * @param methodName
         *            方法名
         * @param classes
         *            方法参数类型数组
         * @return 方法对象
         * @throws Exception
         */
        private Method getMethod(Class clazz, String methodName,
                                 final Class[] classes) throws Exception {
            Method method = null;
            try {
                method = clazz.getDeclaredMethod(methodName, classes);
            } catch (NoSuchMethodException e) {
                try {
                    method = clazz.getMethod(methodName, classes);
                } catch (NoSuchMethodException ex) {
                    if (clazz.getSuperclass() == null) {
                        return method;
                    } else {
                        method = getMethod(clazz.getSuperclass(), methodName,
                                classes);
                    }
                }
            }
            return method;
        }


        /**
         * 处理update设置Gravity失效问题
         * @param x
         * @param y
         * @param width
         * @param height
         * @param force
         */
        @TargetApi(24)
        @Override
        public void update(int x, int y, int width, int height, boolean force) {
            if (Build.VERSION.SDK_INT != 24) {
                super.update(x, y, width, height, force);
                return;
            }
            if (width >= 0) {
                setParam("mLastWidth", width);
                setWidth(width);
            }

            if (height >= 0) {
                setParam("mLastHeight", height);
                setHeight(height);
            }

            Object obj = getParam("mContentView");
            View mContentView = null;
            if (obj instanceof View) {
                mContentView = (View) obj;
            }
            if (!isShowing() || mContentView == null) {
                return;
            }

            obj = getParam("mDecorView");
            View mDecorView = null;
            if (obj instanceof View) {
                mDecorView = (View) obj;
            }
            final WindowManager.LayoutParams p =
                    (WindowManager.LayoutParams) mDecorView.getLayoutParams();

            boolean update = force;

            obj = getParam("mWidthMode");
            int mWidthMode = obj != null ? (Integer) obj : 0;
            obj = getParam("mLastWidth");
            int mLastWidth = obj != null ? (Integer) obj : 0;

            final int finalWidth = mWidthMode < 0 ? mWidthMode : mLastWidth;
            if (width != -1 && p.width != finalWidth) {
                p.width = finalWidth;
                setParam("mLastWidth", finalWidth);
                update = true;
            }

            obj = getParam("mHeightMode");
            int mHeightMode = obj != null ? (Integer) obj : 0;
            obj = getParam("mLastHeight");
            int mLastHeight = obj != null ? (Integer) obj : 0;
            final int finalHeight = mHeightMode < 0 ? mHeightMode : mLastHeight;
            if (height != -1 && p.height != finalHeight) {
                p.height = finalHeight;
                setParam("mLastHeight", finalHeight);
                update = true;
            }

            if (p.x != x) {
                p.x = x;
                update = true;
            }

            if (p.y != y) {
                p.y = y;
                update = true;
            }

            obj = execMethod("computeAnimationResource",null,null);
            final int newAnim = obj == null ? 0 : (Integer) obj;
            if (newAnim != p.windowAnimations) {
                p.windowAnimations = newAnim;
                update = true;
            }

            obj = execMethod("computeFlags", new Class[]{int.class}, new Object[]{p.flags});
            final int newFlags = obj == null ? 0 : (Integer) obj;
            if (newFlags != p.flags) {
                p.flags = newFlags;
                update = true;
            }

            if (update) {
                execMethod("setLayoutDirectionFromAnchor",null,null);
                obj = getParam("mWindowManager");
                WindowManager mWindowManager = obj instanceof WindowManager ? (WindowManager) obj : null;
                if (mWindowManager != null) {
                    mWindowManager.updateViewLayout(mDecorView, p);
                }
            }
        }
    }

}
