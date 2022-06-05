package ui.messageAdapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.answer.databinding.ItemMessageBinding
import com.bobrovskii.core.toDate
import com.bobrovskii.exam.domain.entity.Message

class MessageViewHolder(
	private val binding: ItemMessageBinding,
) : RecyclerView.ViewHolder(binding.root) {

	fun bind(
		item: Message,
		onItemClicked: (Int) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvName.text = item.senderName
			item.text?.let {
				tvMessage.text = item.text
			} ?: run {
				tvMessage.visibility = View.GONE
			}
			tvTime.text = item.sendTime.toDate()
			item.artefact?.let { artefact ->
				btnAttachment.visibility = View.VISIBLE
				tvId.apply {
					text = artefact.fileName.take(20).padEnd(23, '.')
					visibility = View.VISIBLE
				}
				btnAttachment.setOnClickListener { onItemClicked(artefact.id) }
			} ?: run {
				tvId.visibility = View.GONE
				btnAttachment.visibility = View.GONE
			}
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