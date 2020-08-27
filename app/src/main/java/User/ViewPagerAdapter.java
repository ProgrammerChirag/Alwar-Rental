package User;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Bitmap> bitmapList;

    public ViewPagerAdapter(Context context , List<Bitmap> bitmaps)
    {
        this.context = context;
        this.bitmapList = bitmaps;
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmapList.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        return  imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);

        container.removeView((ImageView)object);
    }
}
