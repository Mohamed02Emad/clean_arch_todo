package com.motodo.todo.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.motodo.todo.R


abstract class SwipeToDeleteCallback(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


    private var mContext: Context? = null
    private var mClearPaint: Paint? = null
    private var mBackground: ColorDrawable? = null
    private var backgroundColor = 0
    private var deleteDrawable: Drawable? = null
    private var intrinsicWidth = 0
    private var intrinsicHeight = 0


    init {
        mContext = context
        mBackground = ColorDrawable()
        backgroundColor = Color.parseColor("#FFE2E6")
        mClearPaint = Paint()
        mClearPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        deleteDrawable = ContextCompat.getDrawable(mContext!!, R.drawable.ic_delete)
        intrinsicWidth = deleteDrawable!!.intrinsicWidth
        intrinsicHeight = deleteDrawable!!.intrinsicHeight
    }


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        viewHolder1: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val itemHeight = itemView.height
        val isCancelled = dX == 0f && !isCurrentlyActive

        if (isCancelled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        val radius = 16.dp().toFloat()
        val rect = RectF(
            (itemView.right + dX - 30),
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat()
        )

//        val paint = Paint()
//        paint.color = backgroundColor
//        paint.style = Paint.Style.FILL
//        c.drawRoundRect(rect, radius, radius, paint)

        val path = Path()
        path.moveTo(rect.left, rect.top)
        path.lineTo(rect.right - radius, rect.top)
        path.arcTo(
            RectF(rect.right - 2 * radius, rect.top, rect.right, rect.top + 2 * radius),
            -90f,
            90f
        )
        path.lineTo(rect.right, rect.bottom)
        path.arcTo(
            RectF(rect.right - 2 * radius, rect.bottom - 2 * radius, rect.right, rect.bottom),
            0f,
            90f
        )
        path.lineTo(rect.left, rect.bottom)
        path.close()

        val paint = Paint()
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL
        c.drawPath(path, paint)

        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight
        deleteDrawable!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteDrawable!!.draw(c)

        val threshold = 0.2f // Set the threshold value (adjust as needed)

        if (dX < 0 && -dX > threshold * itemView.width) {
            // Limit the swipe distance to the threshold
            val adjustedDX = -threshold * itemView.width
            super.onChildDraw(c, recyclerView, viewHolder, adjustedDX, dY, actionState, isCurrentlyActive)
        }

    }

    fun Int.dp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()


    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, mClearPaint!!)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.2f
    }
}



