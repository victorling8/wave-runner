package waverunner.example.victorling8.waverunner.ScoresScreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter
{
    private ArrayList<RecyclerViewItemInformation> items;
    private RecyclerViewCallback callback;

    private int selectedIndex = 0;
    private ViewHolder selectedHolder = null;

    private OnItemClickListener onItemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(View view, int position)
        {
            if (selectedIndex == position) return;

            if (callback != null)
            {
                callback.onClick(position);
            }

            notifyItemChanged(position);
            notifyItemChanged(selectedIndex);

            selectedIndex = position;
        }
    };

    public RecyclerViewAdapter(ArrayList<RecyclerViewItemInformation> items, RecyclerViewCallback callback)
    {
        this.items = items;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.high_score_recycler_view_item, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);

        view.setTag(holder);
        view.setOnClickListener(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        ViewHolder holder = (ViewHolder) viewHolder;

        holder.foreground.setText(items.get(i).string);
        holder.background.setText(items.get(i).string);

        holder.background.setTextColor(items.get(i).colour);

        if (selectedIndex == i)
        {
            select(holder);

            selectedHolder = holder;
        }
        else
        {
            deselect(holder);

            if (selectedHolder == holder)
            {
                selectedHolder = null;
            }
        }
    }

    public void setSelectedIndex(int index)
    {
        selectedIndex = index;
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    private void select(ViewHolder holder)
    {
        holder.foreground.setVisibility(View.INVISIBLE);
        holder.background.setVisibility(View.VISIBLE);
    }

    private void deselect(ViewHolder holder)
    {
        if (holder == null) return;

        holder.foreground.setVisibility(View.VISIBLE);
        holder.background.setVisibility(View.INVISIBLE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView foreground;
        public TextView background;

        public ViewHolder(View view)
        {
            super(view);

            foreground = (TextView) view.findViewById(R.id.high_score_recycler_view_item_foreground);
            background = (TextView) view.findViewById(R.id.high_score_recycler_view_item_background);
        }

        @Override
        public void onClick(View view)
        {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewCallback
    {
        public void onClick(int index);
    }

    private interface OnItemClickListener
    {
        public void onItemClick(View view, int position);
    }
}
