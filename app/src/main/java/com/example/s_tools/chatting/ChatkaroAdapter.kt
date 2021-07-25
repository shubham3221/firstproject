package com.example.s_tools.chatting

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s_tools.R
import com.example.s_tools.chatting.messageModel.MessageModel
import com.example.s_tools.tools.GetDate
 class ChatkaroAdapter(var context: Context, var list: List<MessageModel>) :
    RecyclerView.Adapter<ChatkaroAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View
        view = if (viewType == ADMIN) {
            LayoutInflater.from(context).inflate(R.layout.received_msg, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.sent_msg, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[0].messages == null) {
            return
        }
        if (list[0].messages[position].sender_id == ADMIN) {
            holder.message.text =
                Html.fromHtml(list[0].messages[position].message["raw"].asString).toString()
                    .trim { it <= ' ' }
        } else {
            holder.message.text =
                list[0].messages[position].message["raw"].asString.trim { it <= ' ' }
        }
        holder.timeText.text =
            GetDate.covertTimeToTextForMyWebsite(list[0].messages[position].date_sent)

//
//        if (list.get(0).getMessages().get(position).getDate_sent().contains("sending")){
//            holder.message.setText(list.get(0).getMessages().get(position).getMessage().get("raw").getAsString().trim());
//            holder.timeText.setText(GetDate.covertTimeToTextForMyWebsite(list.get(0).getMessages().get(position).getDate_sent()));
//        }else if (list.get(0).getMessages().get(position).getDate_sent().contains("error")){
//            holder.message.setText(list.get(0).getMessages().get(position).getMessage().get("raw").getAsString());
//            holder.timeText.setText("error");
//        }
//
//        else {
//            holder.message.setText(list.get(0).getMessages().get(position).getMessage().get("raw").getAsString());
//            holder.timeText.setText("sending...");
//        }
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) {
            0
        } else list[0].messages.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: TextView
        var timeText: TextView

        init {
            message = itemView.findViewById(R.id.show_msg)
            timeText = itemView.findViewById(R.id.timestamp_text_view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        //condition
        return if (list[0].messages[position].sender_id == ADMIN) {
            1
        } else 0
    }

    companion object {
        const val ADMIN = 1
    }
}