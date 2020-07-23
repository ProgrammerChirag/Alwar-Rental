package Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import Seller.ActivityPostNewProperty;

public class SellerActivityNavigation {

    Context context;

    public SellerActivityNavigation(final Context context )
    {
        this.context = context;
    }

    public void openPostNewPropertyActivity()
    {
        ((Activity)(context)).finish();
        Intent intent = new Intent(context.getApplicationContext() , ActivityPostNewProperty.class);
        ((Activity)(context)).startActivity(intent);
    }


}
