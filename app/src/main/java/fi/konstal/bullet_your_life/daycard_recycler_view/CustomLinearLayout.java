package fi.konstal.bullet_your_life.daycard_recycler_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import fi.konstal.bullet_your_life.R;


/**
 * This is an extension class for {@link LinearLayout} which provides little dot
 * to the background
 *
 * @author Konsta Lehtinen
 * @author KonstaL
 * @version 1.0
 * @since 1.0
 */
public class CustomLinearLayout extends LinearLayout {

    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * converts Drawable to a Bitmap
     * @param drawable the convertable Drawable
     * @return The converted Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Drawable d = getResources().getDrawable(R.drawable.circle);
        if (d != null) {
            Bitmap b = drawableToBitmap(d);
            BitmapDrawable bm = new BitmapDrawable(getResources(), b);
            bm.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

            bm.setBounds(canvas.getClipBounds());
            bm.draw(canvas);
        }
    }
}
