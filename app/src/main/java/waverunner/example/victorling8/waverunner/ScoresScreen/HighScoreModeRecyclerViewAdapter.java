package waverunner.example.victorling8.waverunner.ScoresScreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.GameAndTutorial.ScreenUtil;

public class HighScoreModeRecyclerViewAdapter extends RecyclerView.Adapter
{
    private ArrayList<Integer> drawableIds;
    private HighScoreModeRecyclerViewCallback callback;

    HashMap<Integer, Bitmap> bitmaps = new HashMap<>();

    private static final int itemHeightInDp = 40;

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
            notifyItemChanged(selectedIndex);;

            selectedIndex = position;
        }
    };

    public HighScoreModeRecyclerViewAdapter(ArrayList<Integer> items, HighScoreModeRecyclerViewCallback callback)
    {
        drawableIds = items;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.modes_recycler_view_item, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);

        view.setTag(holder);
        view.setOnClickListener(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        ViewHolder holder = (ViewHolder) viewHolder;

        ImageView image = holder.image;

        Bitmap bitmap = bitmaps.get(i);

        int width, height;

        if (bitmap == null)
        {
            bitmap = BitmapFactory.decodeResource(image.getContext().getResources(), drawableIds.get(i));

            height = Math.round(ScreenUtil.convertDpToPixel(itemHeightInDp, image.getContext()));
            width = (int) (bitmap.getWidth() * ((double) height / bitmap.getHeight()));

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

            bitmaps.put(i, bitmap);
        }
        else
        {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }

        image.setImageBitmap(bitmap);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) image.getLayoutParams();
        params.width = width;
        params.height = height;

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
        return drawableIds.size();
    }

    private void select(ViewHolder holder)
    {
        holder.image.setAlpha(1f);
    }

    private void deselect(ViewHolder holder)
    {
        if (holder == null) return;

        holder.image.setAlpha(0.3f);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView image;

        public ViewHolder(View view)
        {
            super(view);

            image = (ImageView) view.findViewById(R.id.modes_list_item_image);
        }

        @Override
        public void onClick(View view)
        {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface HighScoreModeRecyclerViewCallback
    {
        public void onClick(int index);
    }

    private interface OnItemClickListener
    {
        public void onItemClick(View view, int position);
    }
}
