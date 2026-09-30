package net.bi4vmr.tool.android.ui.pieprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import kotlin.math.min

/**
 * 饼状进度条。
 *
 * 参考资料：
 *
 * - [EasyView - PieProgressBar](https://github.com/lilongweidev/EasyView)
 *
 * @author bi4vmr@outlook.com
 * @version 1.0
 */
class PieProgressBar @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(mContext, attrs) {

    companion object {

        private const val TAG: String = "PieProgressBar"

        /**
         * 最小进度（默认值：0）。
         */
        const val PROGRESS_MIN_DEFAULT: Float = 0.0F

        /**
         * 最大进度（默认值：100）。
         */
        const val PROGRESS_MAX_DEFAULT: Float = 100.0F

        /**
         * 初始进度（默认值：50）。
         */
        const val PROGRESS_DEFAULT: Float = 50.0F

        /**
         * 圆的半径（默认值：-1，与控件最短的边对齐。）。
         */
        const val RADIUS_DEFAULT: Int = -1

        /**
         * 起始绘制角度（默认值：12点位置）。
         */
        const val START_DEGREE_DEFAULT: Float = 270.0F

        /**
         * 是否逆时针绘制（默认值：顺时针绘制）。
         */
        const val REVERSE_DRAW_DEFAULT: Boolean = false

        /**
         * 饼图与圆环颜色（默认值：绿色）。
         */
        @ColorInt
        const val COLOR_DEFAULT: Int = Color.GREEN

        /**
         * 圆环宽度（默认值：4 px）。
         */
        @Px
        const val RING_WIDTH_DEFAULT: Int = 4

        /**
         * 是否显示圆环（默认值：显示）。
         */
        const val RING_DISPLAY_DEFAULT: Boolean = true
    }

    /**
     * 最小进度。
     */
    @get:JvmName("getMinProgress")
    @set:JvmName("setMinProgress")
    var mMinProgress: Float = PROGRESS_MIN_DEFAULT
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 最大进度。
     */
    @get:JvmName("getMaxProgress")
    @set:JvmName("setMaxProgress")
    var mMaxProgress: Float = PROGRESS_MAX_DEFAULT
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 起始绘制角度。
     */
    @get:JvmName("getStartDegree")
    @set:JvmName("setStartDegree")
    var mStartDegree: Float = START_DEGREE_DEFAULT
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 是否逆时针绘制。
     */
    @get:JvmName("isReverseDraw")
    @set:JvmName("setReverseDraw")
    var mReverseDraw: Boolean = REVERSE_DRAW_DEFAULT
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 饼图颜色。
     */
    @get:JvmName("getProgressColor")
    @get:ColorInt
    @set:JvmName("setProgressColor")
    var mPieColor: Int = COLOR_DEFAULT
        set(@ColorInt value) {
            field = value
            invalidate()
        }

    /**
     * 圆环颜色。
     */
    @get:JvmName("getRingColor")
    @get:ColorInt
    @set:JvmName("setRingColor")
    var mRingColor: Int = COLOR_DEFAULT
        set(@ColorInt value) {
            field = value
            invalidate()
        }

    /**
     * 圆环宽度。
     */
    @get:JvmName("getRingWidth")
    @get:Px
    @set:JvmName("setRingWidth")
    var mRingWidth: Int = RING_WIDTH_DEFAULT
        set(@Px value) {
            field = value
            invalidate()
        }

    /**
     * 是否显示圆环。
     */
    @get:JvmName("isRingDisplay")
    @set:JvmName("setRingDisplay")
    var mRingDisplay: Boolean = RING_DISPLAY_DEFAULT
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 最大饼图半径。
     *
     * 饼图只能是圆形，因此最大值为控件最短的边的一半。
     */
    private var mMaxRadius: Int = 0

    /**
     * 用户自定义的饼图半径。
     *
     * 特殊值 [RADIUS_DEFAULT] 表示与控件最短的边对齐，此时取 [mMaxRadius] 的值作为半径。
     */
    private var mRadius: Int = RADIUS_DEFAULT

    /**
     * 当前进度。
     */
    private var mProgress: Float = PROGRESS_DEFAULT

    /**
     * 当前进度对应的角度。
     */
    private var mDegree: Float = 0F

    // 饼图绘制区域
    private val pieRect = RectF()

    // 饼图的画笔
    private val piePaint: Paint = Paint().apply {
        color = mPieColor
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // 圆环的画笔
    private val ringPaint: Paint by lazy {
        Paint().apply {
            color = mRingColor
            style = Paint.Style.STROKE
            strokeWidth = mRingWidth.toFloat()
            isAntiAlias = true
        }
    }

    init {
        if (attrs != null) {
            parseXMLAttrs(attrs)
        }
    }

    // 解析XML属性
    private fun parseXMLAttrs(attrs: AttributeSet, defStyleAttr: Int = 0, defStyleRes: Int = 0) {
        mContext.obtainStyledAttributes(attrs, R.styleable.PieProgressBar, defStyleAttr, defStyleRes).use {
            mProgress = it.getFloat(R.styleable.PieProgressBar_progress, PROGRESS_DEFAULT)
            mMinProgress = it.getFloat(R.styleable.PieProgressBar_minProgress, PROGRESS_MIN_DEFAULT)
            mMaxProgress = it.getFloat(R.styleable.PieProgressBar_maxProgress, PROGRESS_MAX_DEFAULT)
            mRadius = it.getDimensionPixelSize(R.styleable.PieProgressBar_radius, RADIUS_DEFAULT)
            mStartDegree = it.getFloat(R.styleable.PieProgressBar_startDegree, START_DEGREE_DEFAULT)
            mReverseDraw = it.getBoolean(R.styleable.PieProgressBar_reverseDraw, REVERSE_DRAW_DEFAULT)
            mPieColor = it.getColor(R.styleable.PieProgressBar_pieColor, COLOR_DEFAULT)
            mRingColor = it.getColor(R.styleable.PieProgressBar_ringColor, COLOR_DEFAULT)
            mRingWidth = it.getDimensionPixelSize(R.styleable.PieProgressBar_ringWidth, RING_WIDTH_DEFAULT)
            mRingDisplay = it.getBoolean(R.styleable.PieProgressBar_ringDisplay, RING_DISPLAY_DEFAULT)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // Log.d("PieProgressBar", "OnSizeChanged. Width:[$w] Height:[$h]")
        // 控件不是正方形时，以短边为最大直径绘制圆形。
        mMaxRadius = min(w, h) / 2

        if ((mRadius != RADIUS_DEFAULT) && (mRadius > mMaxRadius)) {
            Log.w(TAG, "Radius [$mRadius] is out of view bounds, reset to max value [$mMaxRadius].")
            mRadius = RADIUS_DEFAULT
        }

        val rectEdge: Float = (getRadius() * 2).toFloat()
        val left: Float = (w - rectEdge) / 2
        val right: Float = left + rectEdge
        val top: Float = (h - rectEdge) / 2
        val bottom: Float = top + rectEdge
        pieRect.set(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        // Log.d("PieProgressBar", "OnDraw. Width:[$width] Height:[$height]")

        drawPie(canvas)
        drawRing(canvas)
    }

    // 绘制饼图
    private fun drawPie(canvas: Canvas) {
        // 计算当前进度对应的角度值
        mDegree = (mProgress / (mMaxProgress - mMinProgress)) * 360.0F

        val startDegree: Float = if (mReverseDraw) {
            mStartDegree - 360.0F
        } else {
            mStartDegree
        }

        val sweepDegree: Float = mDegree * (if (mReverseDraw) -1 else 1)
        canvas.drawArc(pieRect, startDegree, sweepDegree, true, piePaint)
    }

    // 绘制圆环
    private fun drawRing(canvas: Canvas) {
        if (mRingDisplay) {
            val centerX: Float = width / 2.0F
            val centerY: Float = height / 2.0F
            // 为了避免圆环超出控件边界，圆环向圆心一侧绘制。
            val ringRadius: Float = getRadius() - (mRingWidth / 2.0F)
            canvas.drawCircle(centerX, centerY, ringRadius, ringPaint)
        }
    }

    /**
     * 获取当前进度。
     *
     * @return 进度值。
     */
    fun getProgress(): Float {
        return mProgress
    }

    /**
     * 设置当前进度。
     *
     * @param[progress] 进度值。
     */
    fun setProgress(progress: Float) {
        val newProgress = progress.coerceAtLeast(mMinProgress).coerceAtMost(mMaxProgress)
        if (mProgress != newProgress) {
            mProgress = newProgress
            invalidate()
        }
    }

    /**
     * 获取饼图半径。
     *
     * @return 半径，单位为像素。
     */
    fun getRadius(): Int {
        return if (mRadius == RADIUS_DEFAULT) mMaxRadius else mRadius
    }

    /**
     * 设置饼图半径。
     *
     * @param[radius] 半径，单位为像素。
     */
    fun setRadius(radius: Int) {
        // 如果最大半径为初始值，说明View还没有开始绘制，可以直接设置半径，有效性等到 `onSizeChanged()` 方法回调时进行判断。
        if (mMaxRadius == 0) {
            mRadius = radius
            return
        }

        if (radius > mMaxRadius) {
            Log.w(TAG, "Radius [$radius] is out of view bounds, reset to max value [$mMaxRadius].")
            mRadius = RADIUS_DEFAULT
        } else {
            mRadius = radius
        }

        invalidate()
    }
}
