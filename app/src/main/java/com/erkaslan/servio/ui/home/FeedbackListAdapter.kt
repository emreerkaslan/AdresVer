package com.erkaslan.servio.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erkaslan.servio.databinding.RowLayoutFeedbackBinding
import com.erkaslan.servio.model.Feedback

class FeedbackListAdapter (var feedbackList: List<Feedback>/*, var listener: FeedbackActionListener*/): RecyclerView.Adapter<FeedbackItemViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackItemViewHolder {
        if(!::layoutInflater.isInitialized){ layoutInflater = LayoutInflater.from(parent.context) }
        return FeedbackItemViewHolder.create(layoutInflater, parent)
    }

    override fun getItemCount(): Int { return feedbackList.size }

    override fun onBindViewHolder(holder: FeedbackItemViewHolder, position: Int) {
        holder.bind(feedbackList[position]/*, listener*/)
    }
}

class FeedbackItemViewHolder(val binding: RowLayoutFeedbackBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup): FeedbackItemViewHolder{
            return FeedbackItemViewHolder(RowLayoutFeedbackBinding.inflate(inflater, parent, false))
        }
    }

    fun bind(feedback: Feedback/*, listener: ServiceActionListener*/) {
        binding.comment = feedback.comment
        binding.rating = feedback.rating.toString()
    }
}