package com.android.internal.graphics.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import org.jetbrains.annotations.NotNull;

/**
 * BackgroundBlurDrawable。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
public class BackgroundBlurDrawable extends Drawable {

    public void setBlurRadius(int blurRadius) {
        throw new RuntimeException("Stub!");
    }

    public void setCornerRadius(float cornerRadius) {
        throw new RuntimeException("Stub!");
    }

    public void setColor(int color) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void draw(@NotNull Canvas canvas) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void setAlpha(int alpha) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getOpacity() {
        throw new RuntimeException("Stub!");
    }
}
