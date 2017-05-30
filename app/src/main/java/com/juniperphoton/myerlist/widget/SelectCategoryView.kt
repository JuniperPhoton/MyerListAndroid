package com.juniperphoton.myerlist.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout

import com.juniperphoton.myerlist.R
import com.juniperphoton.myerlist.model.ToDoCategory
import com.juniperphoton.myerlist.realm.RealmUtils
import com.juniperphoton.myerlist.util.getDimenInPixel
import com.juniperphoton.myerlist.util.toResColor

class SelectCategoryView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    companion object {
        private val TAG = "SelectCategoryView"
    }

    var onSelectionChanged: ((Int) -> Unit)? = null

    var selectedIndex = 0
        set(value) {
            field = value
            updateUi(getChildAt(value) as CateCircleView)
            onSelectionChanged?.invoke(value)
        }

    fun toggleSelection(plus: Boolean) {
        Log.d(TAG, "toggleSelection:" + plus)
        var target = selectedIndex + if (plus) 1 else -1
        if (target >= childCount) target = 0
        if (target < 0) target = childCount - 1
        selectedIndex = target
        onSelectionChanged?.invoke(selectedIndex)
    }

    fun makeViews() {
        removeAllViews()
        val categories = RealmUtils.mainInstance.where(ToDoCategory::class.java).findAll()
        for (toDoCategory in categories) {
            if (toDoCategory.id >= 0) {
                val circleView = CateCircleView(context, null)
                val layoutParams = LinearLayout.LayoutParams(context.getDimenInPixel(24), context.getDimenInPixel(24))
                layoutParams.setMargins(context.getDimenInPixel(8), 0, 0, 0)
                circleView.layoutParams = layoutParams
                circleView.color = toDoCategory.intColor
                circleView.setOnClickListener {
                    selectedIndex = indexOfChild(it)
                }
                addView(circleView)
            }
        }
        val circleView = CateCircleView(context, null)
        val layoutParams = LinearLayout.LayoutParams(context.getDimenInPixel(24), context.getDimenInPixel(24))
        circleView.layoutParams = layoutParams
        circleView.color = R.color.MyerListBlue.toResColor()
        circleView.setOnClickListener {
            selectedIndex = indexOfChild(it)
        }
        addView(circleView, 0)
        getChildAt(0).isSelected = true
    }

    private fun updateUi(circleView: CateCircleView) {
        (0..childCount - 1)
                .map { getChildAt(it) as CateCircleView }
                .forEach { it.inSelected = it === circleView }
    }
}
