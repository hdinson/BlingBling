package com.dinson.blingbase.widget.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class AndroidXBlurImpl implements BlurImpl {
    static Boolean DEBUG = null;
    private Allocation mBlurInput;
    private Allocation mBlurOutput;
    private ScriptIntrinsicBlur mBlurScript;
    private RenderScript mRenderScript;

    public boolean prepare(Context context, Bitmap buffer, float radius) {
        if (this.mRenderScript == null) {
            try {
                this.mRenderScript = RenderScript.create(context);
                this.mBlurScript = ScriptIntrinsicBlur.create(this.mRenderScript, Element.U8_4(this.mRenderScript));
            } catch (RSRuntimeException e) {
                if (isDebug(context)) {
                    throw e;
                }
                release();
                return false;
            }
        }
        this.mBlurScript.setRadius(radius);
        this.mBlurInput = Allocation.createFromBitmap(this.mRenderScript, buffer, Allocation.MipmapControl.MIPMAP_NONE, 1);
        this.mBlurOutput = Allocation.createTyped(this.mRenderScript, this.mBlurInput.getType());
        return true;
    }

    public void release() {
        if (this.mBlurInput != null) {
            this.mBlurInput.destroy();
            this.mBlurInput = null;
        }
        if (this.mBlurOutput != null) {
            this.mBlurOutput.destroy();
            this.mBlurOutput = null;
        }
        if (this.mBlurScript != null) {
            this.mBlurScript.destroy();
            this.mBlurScript = null;
        }
        if (this.mRenderScript != null) {
            this.mRenderScript.destroy();
            this.mRenderScript = null;
        }
    }

    public void blur(Bitmap input, Bitmap output) {
        this.mBlurInput.copyFrom(input);
        this.mBlurScript.setInput(this.mBlurInput);
        this.mBlurScript.forEach(this.mBlurOutput);
        this.mBlurOutput.copyTo(output);
    }

    static boolean isDebug(Context ctx) {
        boolean z;
        if (DEBUG == null && ctx != null) {
            if ((ctx.getApplicationInfo().flags & 2) != 0) {
                z = true;
            } else {
                z = false;
            }
            DEBUG = Boolean.valueOf(z);
        }
        return DEBUG == Boolean.TRUE;
    }
}