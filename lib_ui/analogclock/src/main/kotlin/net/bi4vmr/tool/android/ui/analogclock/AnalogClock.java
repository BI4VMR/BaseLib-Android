package net.bi4vmr.tool.android.ui.analogclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.bi4vmr.tool.android.ui.analogclock.databinding.AnalogClockMainViewBinding;

import java.time.Instant;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 模拟时钟控件。
 * <p>
 * 支持秒级指针平滑显示，可以自由替换素材并设置缩放，用于取代原生的AnalogClock。
 * <p>
 * 设计参考：<a href="https://github.com/schorschii/FsClock-Android">FsClock-Android</a>
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
public class AnalogClock extends FrameLayout {

    private static final long INTERVAL_SLOW = 1000L;
    private static final long INTERVAL_FAST = 100L;

    private AnalogClockMainViewBinding binding;
    private ScheduledExecutorService executor = null;

    // 秒针是否需要显示
    private boolean needShowSecondPointer = true;
    // 秒针是否平滑移动
    private boolean secondPointerSmoothMove = true;

    public AnalogClock(@NonNull Context context) {
        this(context, null);
    }

    public AnalogClock(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    // 初始化视图
    private void initView() {
        binding = AnalogClockMainViewBinding.inflate(LayoutInflater.from(getContext()), this, true);

        updateSecondPointerShowState();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        updatePointer();
        startTasks();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTasks();
    }

    public void setDialBackgroundResource(@DrawableRes int resID) {
        binding.ivDial.setBackgroundResource(resID);
    }

    public void setDialBackgroundColor(@ColorInt int color) {
        binding.ivDial.setBackgroundColor(color);
    }

    public void setDialImageResource(@DrawableRes int resID) {
        binding.ivDial.setImageResource(resID);
    }

    public void setDialImageDrawable(@Nullable Drawable drawable) {
        binding.ivDial.setImageDrawable(drawable);
    }

    public void setHourImageResource(@DrawableRes int resID) {
        binding.ivHourPointer.setImageResource(resID);
    }

    public void setHourImageBitmap(Bitmap bm) {
        binding.ivHourPointer.setImageBitmap(bm);
    }

    public void setHourImageScale(float multiple) {
        setViewScale(binding.ivHourPointer, multiple);
    }

    public void setMinuteImageResource(@DrawableRes int resID) {
        binding.ivMinutePointer.setImageResource(resID);
    }

    public void setMinuteImageBitmap(Bitmap bm) {
        binding.ivMinutePointer.setImageBitmap(bm);
    }

    public void setMinuteImageScale(float multiple) {
        setViewScale(binding.ivMinutePointer, multiple);
    }

    public void setSecondImageResource(@DrawableRes int resID) {
        binding.ivSecondPointer.setImageResource(resID);
    }

    public void setSecondImageBitmap(Bitmap bm) {
        binding.ivSecondPointer.setImageBitmap(bm);
    }

    public void setSecondImageScale(float multiple) {
        setViewScale(binding.ivSecondPointer, multiple);
    }

    public boolean getSecondPointerShowState() {
        return needShowSecondPointer;
    }

    public void setSecondPointerShowState(boolean newState) {
        needShowSecondPointer = newState;
        // 该属性被更新后，需要刷新秒针控件的显示与隐藏。
        updateSecondPointerShowState();
    }

    public boolean isSecondPointerSmoothMove() {
        return secondPointerSmoothMove;
    }

    public void setSecondPointerSmoothMove(boolean newState) {
        secondPointerSmoothMove = newState;
        // 该属性被更新后，轮询任务能够取到最新数值，因此不必手动执行刷新操作。
    }

    // 更新指针位置
    private void updatePointer() {
        final LocalTime localTime = LocalTime.now();
        int second = localTime.getSecond();
        int minute = localTime.getMinute();
        int hour = localTime.getHour();

        /*
         * 计算旋转角度
         *
         * 时、分、秒的计算方法是相似的，此处以时针为例进行说明：
         * 首先"360/12"可以得出“每个小时的角度”，“当前小时 * 每个小时的角度”可以得出当前时针旋转的角度，但此时缺少
         * 分钟数所造成的额外偏转角度。“当前小时 + (当前分钟 / 每小时的分钟数)”即为包含小数的精确小时数，用它再乘以
         * “每个小时的角度”即为精确的偏转角度。
         */
        float hourDegree = (hour + (minute / 60.0F)) * (360.0F / 12);
        float minDegree = (minute + (second / 60.0F)) * (360.0F / 60);
        float secDegree;
        // 如果已开启秒针平滑移动，则需要计算毫秒带来的偏转角度。
        if (secondPointerSmoothMove) {
            long milli = Instant.now().toEpochMilli() % 1000;
            secDegree = (second + (milli / 1000.0F)) * (360.0F / 60);
        } else {
            secDegree = second * (360.0F / 60);
        }

        binding.ivSecondPointer.setRotation(secDegree);
        binding.ivMinutePointer.setRotation(minDegree);
        binding.ivHourPointer.setRotation(hourDegree);
    }

    // 更新秒针的显示状态
    private void updateSecondPointerShowState() {
        if (needShowSecondPointer) {
            binding.ivSecondPointer.setVisibility(View.VISIBLE);
        } else {
            binding.ivSecondPointer.setVisibility(View.GONE);
            // 无需显示秒针时，不必使用高频刷新，可以节约性能。
            secondPointerSmoothMove = false;
        }
    }

    // 注册定时任务
    private void startTasks() {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(1);
        }

        final long interval = secondPointerSmoothMove ? INTERVAL_FAST : INTERVAL_SLOW;
        executor.scheduleAtFixedRate(this::updatePointer, interval, interval, TimeUnit.MILLISECONDS);
    }

    // 注销定时任务
    private void stopTasks() {
        executor.shutdown();
        executor = null;
    }

    /**
     * Name        : 等比缩放控件尺寸
     * <p>
     * Description : 等比缩放控件的宽高尺寸。
     *
     * @param view     控件
     * @param multiple 缩放倍数
     */
    private void setViewScale(View view, float multiple) {
        view.setScaleX(multiple);
        view.setScaleY(multiple);
    }
}
