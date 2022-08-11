package com.example.finalproject.ui.chats.messagerv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.Message;
import com.example.finalproject.ui.chats.MessageStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int ITEM_SENDER = 1;
    private static final int ITEM_RECIPIENT = 2;
    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENDER) {
            return new SenderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_sender, parent, false));
        } else {
            return new RecipientViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recipient, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.messageTV.setText(message.text);

            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
            senderViewHolder.dateTimeTV.setText(formatter.format(new Date(message.dateTime)));
        } else {
            RecipientViewHolder ReViewHolder = (RecipientViewHolder) holder;
            ReViewHolder.messageTV.setText(message.text);

            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
            ReViewHolder.dateTimeTV.setText(formatter.format(new Date(message.dateTime)));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).status == MessageStatus.SENT) {
            return ITEM_SENDER;
        } else {
            return ITEM_RECIPIENT;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTV;
        private TextView dateTimeTV;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTV = itemView.findViewById(R.id.message_sent);
            dateTimeTV = itemView.findViewById(R.id.message_sent_time);
        }
    }

    class RecipientViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTV;
        private TextView dateTimeTV;

        public RecipientViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTV = itemView.findViewById(R.id.message_received);
            dateTimeTV = itemView.findViewById(R.id.message_received_time);
        }
    }
}
