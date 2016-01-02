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

public class EnemiesAdapter extends ArrayAdapter<EnemiesFragment.EnemyInfo>
{
    private Context context;
    private int resource;
    private ArrayList<EnemiesFragment.EnemyInfo> enemyInfos;

    public EnemiesAdapter(Context context, int resource, ArrayList<EnemiesFragment.EnemyInfo> objects)
    {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        enemyInfos = objects;
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

            holder.image = (ImageView) view.findViewById(R.id.enemies_list_item_image);
            holder.damage = (TextView) view.findViewById(R.id.enemies_list_item_damage);
            holder.percent = (TextView) view.findViewById(R.id.enemies_list_item_percent);

            view.setTag(holder);
        }
        else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        EnemiesFragment.EnemyInfo info = enemyInfos.get(position);

        holder.image.setImageBitmap(info.bitmap);
        holder.damage.setText(Integer.toString(info.damage));
        holder.percent.setText(info.percent + "%");

        return view;
    }

    @Override
    public int getCount()
    {
        return enemyInfos.size();
    }

    private static class ViewHolder
    {
        public ImageView image;
        public TextView damage;
        public TextView percent;
    }
}
