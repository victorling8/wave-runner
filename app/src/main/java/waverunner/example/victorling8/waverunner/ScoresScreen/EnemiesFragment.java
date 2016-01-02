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

public class EnemiesFragment extends Fragment
{
    ListView listView;
    EnemiesAdapter adapter;

    private static int length;

    private ArrayList<EnemyInfo> enemyInfos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.enemies_fragment_layout, container, false);

        listView = (ListView) view.findViewById(R.id.enemies_list_view);

        length = Math.round(ScreenUtil.convertDpToPixel(60, getActivity()));

        initializeEnemyInfos();

        adapter = new EnemiesAdapter(getActivity(), R.layout.enemies_list_item, enemyInfos);

        listView.setAdapter(adapter);

        return view;
    }

    private void initializeEnemyInfos()
    {
        if (enemyInfos.size() > 0) return;

        Bitmap bitmap;
        int height;

        int drawableIds[] = new int[]
                {R.drawable.colliding_blocks, R.drawable.turret, R.drawable.missile1, R.drawable.laser_shooter,
                        R.drawable.path_shooter, R.drawable.all_around_shooter, R.drawable.boss};

        AppData.EnemyDamageSummary summary = AppData.getEnemyDamageSummary();

        int totalDamage = 0;

        for (Integer damage : summary.allDamages)
        {
            totalDamage += damage;
        }

        if (totalDamage == 0)
        {
            totalDamage = 1;
        }

        int counter = 0;

        for (int id : drawableIds)
        {
            bitmap = BitmapFactory.decodeResource(getResources(), id);
            height = (int) (bitmap.getHeight() * ((double) length / bitmap.getWidth()));
            bitmap = Bitmap.createScaledBitmap(bitmap, length, height, false);

            enemyInfos.add(new EnemyInfo(bitmap,
                    summary.allDamages.get(counter),
                    (int) Math.round(100 * ((double) summary.allDamages.get(counter) / totalDamage))));

            counter++;
        }
    }

    public static class EnemyInfo
    {
        public Bitmap bitmap;
        public int damage;
        public int percent;

        public EnemyInfo(Bitmap bitmap, int damage, int percent)
        {
            this.bitmap = bitmap;
            this.damage = damage;
            this.percent = percent;
        }
    }
}
