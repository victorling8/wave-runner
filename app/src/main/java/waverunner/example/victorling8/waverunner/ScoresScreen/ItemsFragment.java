package waverunner.example.victorling8.waverunner.ScoresScreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.GameAndTutorial.ScreenUtil;

public class ItemsFragment extends Fragment
{
    ListView listView;
    ItemsAdapter adapter;

    private static int length;

    private ArrayList<ItemInfo> itemInfos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.items_fragment_layout, container, false);

        listView = (ListView) view.findViewById(R.id.items_list_view);

        length = Math.round(ScreenUtil.convertDpToPixel(60, getActivity()));

        initializeItemInfos();

        adapter = new ItemsAdapter(getActivity(), R.layout.items_list_item, itemInfos);

        listView.setAdapter(adapter);

        return view;
    }

    private void initializeItemInfos()
    {
        if (itemInfos.size() > 0) return;

        Bitmap bitmap;
        int height;

        int drawableIds[] = new int[]
                {R.drawable.coin, R.drawable.health_power_up, R.drawable.shield_power_up,
                R.drawable.touch_power_up, R.drawable.coin_power_up, R.drawable.clock_power_up};

        AppData.ItemSummary summary = AppData.getItemSummary();

        String string = "was collected";

        int counter = 0;

        for (int id : drawableIds)
        {
            bitmap = BitmapFactory.decodeResource(getResources(), id);
            height = (int) (bitmap.getHeight() * ((double) length / bitmap.getWidth()));
            bitmap = Bitmap.createScaledBitmap(bitmap, length, height, false);

            itemInfos.add(new ItemInfo(bitmap, string, summary.allItems.get(counter)));

            string = "was used";

            counter++;
        }
    }

    public static class ItemInfo
    {
        public Bitmap bitmap;
        public String text;
        public int value;

        public ItemInfo(Bitmap bitmap, String text, int value)
        {
            this.bitmap = bitmap;
            this.text = text;
            this.value = value;
        }
    }
}
