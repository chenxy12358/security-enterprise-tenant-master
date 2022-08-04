package io.renren.modules.common.utils;

 import org.opencv.core.Point;
 import org.opencv.core.Rect2d;

/**
 * 计算2个标记框的相似度
 *
 * @author cxy
 * @create 2022/6/10
 */
public class RectUtils {
    public static void main(String[] args) {
        Rect2d r1 = new Rect2d(775, 775, 274, 445);
        Rect2d r2 = new Rect2d(775, 775, 274, 500);
        float s = DecideOverlap(r1, r2);
        System.err.println(s);
        System.err.println(r1.contains(new Point(r2.x, r2.y)));

    }

    /**
     * 计算相似度 gpu-cpu
     * @param r1 标记框1
     * @param r2 标记框2
     * @return
     */
    public static float DecideOverlap(Rect2d r1, Rect2d r2) {
        //框1
        int x1 = (int) r1.x;
        int y1 = (int) r1.y;
        int width1 = (int) r1.width;
        int height1 = (int) r1.height;

        //框2
        int x2 = (int) r2.x;
        int y2 = (int) r2.y;
        int width2 = (int) r2.width;
        int height2 = (int) r2.height;

        int endx = Math.max(x1 + width1, x2 + width2);
        int startx = Math.min(x1, x2);
        int width = width1 + width2 - (endx - startx);

        int endy = Math.max(y1 + height1, y2 + height2);
        int starty = Math.min(y1, y2);
        int height = height1 + height2 - (endy - starty);

        float ratio = 0.0f;
        float Area, Area1, Area2;

        if (width <= 0 || height <= 0)
            return 0.0f;
        else {
            Area = width * height;
            Area1 = width1 * height1;
            Area2 = width2 * height2;
            ratio = Area / (Area1 + Area2 - Area);
        }

        return ratio;
    }


    /**
     * 计算rgb 权重规则 （与设备端一致）
     * <p>
     * topclass 当前识别到的物体的顺序
     * nclasses 物体种类数
     * int   offset = (topclass * 123457) % nclasses;
     * float red    = get_color(2, offset, nclasses);
     * float green  = get_color(1, offset, nclasses);
     * float blue   = get_color(0, offset, nclasses);
     *
     * @param c
     * @param x
     * @param max
     * @return
     */
    public static float getColor(int c, int x, int max) {
        float colors[][] = {{1, 0, 1}, {0, 0, 1}, {0, 1, 1}, {0, 1, 0}, {1, 1, 0}, {1, 0, 0}};
        float ratio = ((float) x / max) * 5;
        int i = (int) Math.floor(ratio);  //取整,返回的是小于或等于ratio的最大整数
        int j = (int) Math.ceil(ratio);   //取整,返回的是大于ratio的最小整数
        ratio -= i;
        float r = (1 - ratio) * colors[i][c] + ratio * colors[j][c];
        return r * 255;
    }
}
