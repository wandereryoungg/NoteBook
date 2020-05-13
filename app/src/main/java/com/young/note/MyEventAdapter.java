package com.young.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.young.note.constants.Constants;
import com.young.note.entity.Event;

import java.util.ArrayList;
import java.util.List;

import static com.young.note.EventDetailActivity.EXTRA_EVENT_DATA;
import static com.young.note.EventDetailActivity.EXTRA_IS_EDIT_EVENT;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.EventViewHolder> {

    private List<Event> mDatabases;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private boolean mIsDeleteMode = false;
    private List<Integer> mSelectedEventIds = new ArrayList<>();

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public boolean getmIsDeleteMode() {
        return mIsDeleteMode;
    }

    public void setmIsDeleteMode(boolean mIsDeleteMode) {
        mSelectedEventIds.clear();
        this.mIsDeleteMode = mIsDeleteMode;
        notifyDataSetChanged();
    }

    public List<Integer> getmSelectedEventIds() {
        return mSelectedEventIds;
    }

    public MyEventAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, int position) {
        final Event event = mDatabases.get(position);
        if (!mIsDeleteMode) {
            if (event.getmIsImportant() == Constants.EventFlag.IMPORTANT) {
                holder.ivIcon.setImageResource(R.drawable.ic_important_event);
                holder.tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                holder.ivIcon.setImageResource(R.drawable.ic_normal_event);
                holder.tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_circle);
            if (event.getmIsImportant() == Constants.EventFlag.IMPORTANT) {
                holder.tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                holder.tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
        holder.tvTitle.setText(event.getmTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsDeleteMode) {
                    if (Constants.IconTag.FIRST == (Integer) holder.ivIcon.getTag()) {
                        holder.ivIcon.setTag(Constants.IconTag.OTHER);
                        holder.ivIcon.setImageResource(R.drawable.ic_selected);
                        mSelectedEventIds.add(event.getmId());
                    } else {
                        mSelectedEventIds.remove(event.getmId());
                        holder.ivIcon.setTag(Constants.IconTag.FIRST);
                        holder.ivIcon.setImageResource(R.drawable.ic_circle);
                    }
                } else if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(v, holder.getLayoutPosition());
                }
                return false;
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, EventDetailActivity.class);
                intent.putExtra(EXTRA_IS_EDIT_EVENT, true);
                intent.putExtra(EXTRA_EVENT_DATA, event);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatabases == null ? 0 : mDatabases.size();
    }

    public void setmDatabases(List<Event> events) {
        mDatabases = events;
        mIsDeleteMode = false;
        notifyDataSetChanged();
    }

    public List<Event> getmDatabases() {
        return mDatabases;
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        TextView tvTitle;
        ImageView ivEdit;
        View itemView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_event);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            this.itemView = itemView;
            ivIcon.setTag(Constants.IconTag.FIRST);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
