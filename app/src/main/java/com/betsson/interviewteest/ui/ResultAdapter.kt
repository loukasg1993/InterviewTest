package com.betsson.interviewteest.ui
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.betsson.interviewteest.data.Bet
import com.betsson.interviewtest.R
import com.betsson.interviewtest.databinding.ListItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class ResultAdapter(private val bets: List<Bet>) :
    RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

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

            Glide.with(avatarImage)
                .load(bet.image)
                .apply(RequestOptions.circleCropTransform())
                .into(avatarImage)

            expandButton.setOnClickListener {

                val transition = AutoTransition()
                transition.duration = 120
                transition.interpolator = AccelerateInterpolator()
//                transition.excludeTarget(binding.titleLinearLayout, true)
                TransitionManager.beginDelayedTransition(itemView.parent as ViewGroup,transition)

                if (expandedScrollView.visibility == View.VISIBLE) {
                    expandedScrollView.visibility = View.GONE
                    expandButton.setImageResource(R.drawable.ic_expand_more)
                } else {
                    expandedScrollView.visibility = View.VISIBLE
                    expandButton.setImageResource(R.drawable.ic_expand_less)
                }
            }
        }
    }


}
