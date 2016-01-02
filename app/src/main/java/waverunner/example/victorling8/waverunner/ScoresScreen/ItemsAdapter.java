package waverunner.example.victorling8.waverunner.ScoresScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.R;

public class ItemsAdapter extends ArrayAdapter<ItemsFragment.ItemInfo>
{
    private Context context;
    private int resource;
    private ArrayList<ItemsFragment.ItemInfo> itemInfos;

    public ItemsAdapter(Context context, int resource, ArrayList<ItemsFragment.ItemInfo> objects)
    {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        itemInfos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        ViewHolder holder;

        if (convertView == null)
        {
            view = LayoutInflater.from(context).inflate(resource, parent, false);

            holder = new ViewHolder();

            holder.image = (ImageView) view.findViewById(R.id.items_list_item_image);
            holder.text = (TextView) view.findViewById(R.id.items_list_item_text);
            holder.value = (TextView) view.findViewById(R.id.items_list_item_value);

            view.setTag(holder);
        }
        else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        ItemsFragment.ItemInfo info = itemInfos.get(position);

        holder.image.setImageBitmap(info.bitmap);
        holder.text.setText(info.text);
        holder.value.setText(Integer.toString(info.value));

        return view;
    }

    @Override
    public int getCount()
    {
        return itemInfos.size();
    }

    private static class ViewHolder
    {
        public ImageView image;
        public TextView text;
        public TextView value;
    }
}
