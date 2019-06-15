package dinson.customview.weight;

import android.content.Context;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class SrcAlignTopImageView extends AppCompatImageView {

    private Matrix mMatrix;

    public SrcAlignTopImageView(Context context) {
        super(context);
    }

    public SrcAlignTopImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SrcAlignTopImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (getScaleType() == ScaleType.MATRIX) transformMatrix();
        return changed;
    }

    private void transformMatrix() {
        Matrix matrix = getImageMatrix();
        matrix.reset();
        float h = getHeight();
        float w = getWidth();
        float ch = getDrawable().getIntrinsicHeight();
        float cw = getDrawable().getIntrinsicWidth();
        float widthScaleFactor = w / cw;
        float heightScaleFactor = h / ch;
       /* if (alignType == AlignType.LEFT) {
            matrix.postScale(heightScaleFactor, heightScaleFactor, 0, 0);
        } else if (alignType == AlignType.RIGHT) {*/
//        matrix.postTranslate(w - cw, 0);
//        matrix.postScale(heightScaleFactor, heightScaleFactor, w, 0);
       /* } else if (alignType == AlignType.BOTTOM) {
            matrix.postTranslate(0, h - ch);
            matrix.postScale(widthScaleFactor, widthScaleFactor, 0, h);
        } else { //default is top
            matrix.postScale(widthScaleFactor, widthScaleFactor, 0, 0);
        }*/

        float scale;
        float dx=0f;
        float dy=0f;


              scale = (float) w / (float) cw;
               dy = (h - ch * scale) * 0.5f;


        matrix.setScale(scale, scale);

        setImageMatrix(matrix);
    }

    public enum AlignType {
        LEFT(0),
        TOP(1),
        RIGHT(2),
        BOTTOM(3);

        AlignType(int ni) {
            nativeInt = ni;
        }

        final int nativeInt;
    }
}
