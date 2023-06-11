package com.betsson.interviewteest.presentation.main.adapters

import androidx.recyclerview.widget.DiffUtil
import com.betsson.interviewteest.model.Bet

class ResultsDiffCallback(private var oldList: List<Bet>, private var newList: List<Bet>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].sellIn == newList[newItemPosition].sellIn
                && oldList[oldItemPosition].odds == newList[newItemPosition].odds
                && oldList[oldItemPosition].type == newList[newItemPosition].type
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].sellIn == newList[newItemPosition].sellIn
                && oldList[oldItemPosition].odds == newList[newItemPosition].odds
                && oldList[oldItemPosition].type == newList[newItemPosition].type
    }

}
