package com.betsson.interviewteest.presentation.main.adapters
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betsson.interviewteest.model.Bet
import com.betsson.interviewtest.R
import com.betsson.interviewtest.databinding.ListItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class ResultAdapter(private var bets: ArrayList<Bet>) :
    RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {
    private var expandedPositions: MutableSet<Int> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = bets[position]
        holder.bind(result)
    }

    override fun getItemCount() = bets.size
    fun updateBetsList(updatedBets: List<Bet>) {
        //Use Diffutils to calculate the difference between the two lists and update the recycler
        val diffCallback = ResultsDiffCallback(bets, updatedBets)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        bets = ArrayList(updatedBets)
        diffResult.dispatchUpdatesTo(this)
    }
    init {
        expandedPositions.addAll(0 until  6)
    }

    inner class ResultViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val name: TextView = binding.nameValueTextView
        private val sellIn: TextView = binding.sellInValueTextView
        private val odds: TextView = binding.oddsValueTextView
        private val avatarImage: ImageView = binding.avatarImageView
        private val expandButton: ImageView = binding.expandButton
        private val expandedScrollView: ScrollView = binding.expandedScrollView

        fun bind(bet: Bet) {
            name.text = bet.type
            sellIn.text = bet.sellIn.toString()
            odds.text = bet.odds.toString()

            val isExpanded = expandedPositions.contains(layoutPosition)
            expandedScrollView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            expandButton.setImageResource(
                if (isExpanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more
            )

            Glide.with(avatarImage)
                .load(bet.image)
                .apply(RequestOptions.circleCropTransform())
                .into(avatarImage)

            expandButton.setOnClickListener {

                val isCurrentlyExpanded = expandedPositions.contains(layoutPosition)
                if (isCurrentlyExpanded) {
                    expandedPositions.remove(layoutPosition)
                } else {
                    expandedPositions.add(layoutPosition)
                }

                val transition = AutoTransition()
                transition.duration = 120
                transition.interpolator = AccelerateInterpolator()
                TransitionManager.beginDelayedTransition(itemView.parent as ViewGroup,transition)

                expandedScrollView.visibility =
                    if (isCurrentlyExpanded) View.GONE else View.VISIBLE
                expandButton.setImageResource(
                    if (isCurrentlyExpanded) R.drawable.ic_expand_more else R.drawable.ic_expand_less
                )
            }
        }

    }


}
