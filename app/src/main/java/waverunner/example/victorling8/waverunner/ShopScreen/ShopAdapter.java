package waverunner.example.victorling8.waverunner.ShopScreen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.GameAndTutorial.ScreenUtil;

public class ShopAdapter extends ArrayAdapter<AnyShopItem>
{
    private Context context;
    private int resource;
    private List<AnyShopItem> shopItems;

    public ShopAdapter(Context context, int resource, List<AnyShopItem> objects)
    {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        shopItems = objects;
    }

    @Override
    public int getCount()
    {
        return shopItems.size();
    }

    public View getNewShopItemView(ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.shopItemImage = (ImageView) view.findViewById(R.id.shop_item_picture);
        viewHolder.shopItemTitle = (TextView) view.findViewById(R.id.shop_item_title);
        viewHolder.shopItemDescription = (TextView) view.findViewById(R.id.shop_item_description);
        viewHolder.shopItemCost = (TextView) view.findViewById(R.id.shop_item_cost);
        viewHolder.shopItemFail = (ImageView) view.findViewById(R.id.shop_item_fail);

        view.setTag(viewHolder);

        return view;
    }

    public View getNewShopTitleView(ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.shop_title_item, parent, false);

        ShopTitleViewHolder shopTitleViewHolder = new ShopTitleViewHolder();
        shopTitleViewHolder.textView = (TextView) view.findViewById(R.id.shop_title_item_coins);

        view.setTag(shopTitleViewHolder);

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        AnyShopItem anyShopItem = shopItems.get(position);

        if (convertView != null)
        {
            Object tag = convertView.getTag();

            if (anyShopItem instanceof ShopItem && ! (tag instanceof ViewHolder))
            {
                view = getNewShopItemView(parent);
            }
            else if (anyShopItem instanceof ShopTitleItem && ! (tag instanceof ShopTitleViewHolder))
            {
                view = getNewShopTitleView(parent);
            }
            else
            {
                view = convertView;
            }
        }
        else
        {
            if (anyShopItem instanceof ShopItem)
            {
                view = getNewShopItemView(parent);
            }
            else
            {
                view = getNewShopTitleView(parent);
            }
        }

        if (anyShopItem instanceof ShopTitleItem)
        {
            ShopTitleItem shopTitleItem = (ShopTitleItem) anyShopItem;

            ((ShopTitleViewHolder) view.getTag()).textView.setText(Integer.toString(shopTitleItem.coins));
        }
        else
        {
            ViewHolder viewHolder = (ViewHolder) view.getTag();

            ShopItem shopItem = (ShopItem) anyShopItem;

            viewHolder.shopItemImage.setImageBitmap(decodeSampledBitmapFromResource(context
                            .getResources(),
                    shopItem.imageId,
                    (int) ScreenUtil.convertDpToPixel(60, context),
                    (int) ScreenUtil.convertDpToPixel(40, context)));
            viewHolder.shopItemTitle.setText(shopItem.title);
            viewHolder.shopItemCost.setText(Integer.toString(shopItem.cost));
            viewHolder.shopItemFail.setAlpha(0f);

            if (shopItem.description.isEmpty())
            {
                viewHolder.shopItemDescription.setVisibility(View.GONE);
                ((LinearLayout.LayoutParams) viewHolder.shopItemTitle.getLayoutParams()).topMargin
                        = Math.round(ScreenUtil.convertDpToPixel(25, context));
            }
            else
            {
                viewHolder.shopItemDescription.setVisibility(View.VISIBLE);
                ((LinearLayout.LayoutParams) viewHolder.shopItemTitle.getLayoutParams()).topMargin
                        = Math.round(ScreenUtil.convertDpToPixel(10, context));

                viewHolder.shopItemDescription.setText(shopItem.description);
            }
        }

        return view;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static class ViewHolder
    {
        public ImageView shopItemImage;
        public TextView shopItemTitle;
        public TextView shopItemDescription;
        public TextView shopItemCost;
        public ImageView shopItemFail;
    }

    public static class ShopTitleViewHolder
    {
        public TextView textView;
    }
}
