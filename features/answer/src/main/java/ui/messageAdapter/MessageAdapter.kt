package ui.messageAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.exam.domain.entity.Message
import ui.messageAdapter.viewholder.MessageViewHolder

class MessageAdapter(
	private val onItemClicked: (Int) -> Unit,
) : RecyclerView.Adapter<MessageViewHolder>() {

	var messages: List<Message> = emptyList()
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder =
		MessageViewHolder.from(parent)

	override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
		holder.bind(
			messages[position],
			onItemClicked,
		)
	}

	override fun getItemCount(): Int = messages.size
}