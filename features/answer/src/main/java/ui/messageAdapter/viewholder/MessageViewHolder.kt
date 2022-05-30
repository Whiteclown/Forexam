package ui.messageAdapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.answer.databinding.ItemMessageBinding
import com.bobrovskii.exam.domain.entity.Message

class MessageViewHolder(
	private val binding: ItemMessageBinding,
) : RecyclerView.ViewHolder(binding.root) {

	fun bind(
		item: Message,
		//onItemClicked: (Int) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvName.text = item.senderName
			tvMessage.text = item.text
			tvTime.text = item.sendTime

			//itemView.setOnClickListener { onItemClicked(item.id) }
			//imageButtonDelete.setOnClickListener { onDeleteClicked(item.id) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): MessageViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
			return MessageViewHolder(binding)
		}
	}
}