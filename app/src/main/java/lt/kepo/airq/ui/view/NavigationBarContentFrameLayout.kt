package lt.kepo.airq.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.WindowInsets
import android.view.Window
import android.widget.FrameLayout
import lt.kepo.airq.R
import kotlin.LazyThreadSafetyMode.NONE

/**
 * A [FrameLayout] which will draw a divider on the edge of the navigation bar. Similar in
 * functionality to what [Window.setNavigationBarDividerColor] provides, but works with translucent
 * navigation bar colors.
 */
class NavigationBarContentFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var lastInsets: WindowInsets? = null

    private val dividerDrawable by lazy(NONE) {
        ColorDrawable(resources.getColor(android.R.color.transparent, null)).apply {
            callback = this@NavigationBarContentFrameLayout
            alpha = 255
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateDividerBounds()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val d = dividerDrawable
        if (!d.bounds.isEmpty) {
            d.draw(canvas)
        }
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        lastInsets = insets
        updateDividerBounds()
        return insets
    }

    private fun updateDividerBounds() {
        val d = dividerDrawable
        val insets = lastInsets

        val bottomInset = insets?.systemWindowInsetBottom ?: 0
        val leftInset = insets?.systemWindowInsetLeft ?: 0
        val rightInset = insets?.systemWindowInsetRight ?: 0

        when {
            bottomInset > 0 -> {
                // Display divider above bottom inset
                d.setBounds(
                    left,
                    bottom - bottomInset,
                    right,
                    bottom + 1 - bottomInset
                )
            }
            leftInset > 0 -> {
                // Display divider to the right of the left inset
                d.setBounds(
                    leftInset - 1,
                    top,
                    leftInset,
                    bottom
                )
            }
            rightInset > 0 -> {
                // Display divider to the left of the right inset
                d.setBounds(
                    right - rightInset,
                    top,
                    right + 1 - rightInset,
                    bottom
                )
            }
            else -> {
                // Set an empty size which will remove the drawable
                d.setBounds(0, 0, 0, 0)
            }
        }

        // Update our will-not-draw flag
        setWillNotDraw(d.bounds.isEmpty)
    }
}