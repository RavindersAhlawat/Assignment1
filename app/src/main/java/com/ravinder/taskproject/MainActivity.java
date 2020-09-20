package com.ravinder.taskproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.ravinder.taskproject.ModelClass.Example;
import com.ravinder.taskproject.sevices.APIClient;
import com.ravinder.taskproject.sevices.APIInterface;
import com.ravinder.taskproject.Notification.AppConstant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //    String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2MDAyNTUxOTIsImp0aSI6IkNmc0lJRmYxWm43TUNJdUJrS2pKVVEiLCJpc3MiOiJodHRwczpcL1wvcmVzb3VyY2VzLnZlZ2E2LmluZm9cLyIsIm5iZiI6MTYwMDI1NTIwMiwiZGF0YSI6eyJ1c2VyX2lkIjoiMSIsImFwcF91cmwiOiJOVWxsIn19.Y4UpB0--8kQWHFHrONhyJy_jGl3VmDZ93Y-qn7yD6tLZRmzktXeIf4YTdraNIMrYTucuVYLB6VrWVhN4TrZpaA";
    String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2MDAyNTUxOTIsImp0aSI6IkNmc0lJRmYxWm43TUNJdUJrS2pKVVEiLCJpc3MiOiJodHRwczpcL1wvcmVzb3VyY2VzLnZlZ2E2LmluZm9cLyIsIm5iZiI6MTYwMDI1NTIwMiwiZGF0YSI6eyJ1c2VyX2lkIjoiMSIsImFwcF91cmwiOiJOVWxsIn19.Y4UpB0--8kQWHFHrONhyJy_jGl3VmDZ93Y-qn7yD6tLZRmzktXeIf4YTdraNIMrYTucuVYLB6VrWVhN4TrZpaA";
    private Button share,sharephoto;
    private ImageView showImage;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    SharePhotoContent content;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);





        showImage = findViewById(R.id.image);
//        share = findViewById(R.id.sharebtn);
//        sharephoto = findViewById(R.id.sharephoto);

        callbackManager= CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);


        String notification_url = getIntent().hasExtra("notificationChannel") ? getIntent().getStringExtra("notificationChannel") : "";

        if (!TextUtils.isEmpty(notification_url)){
            sendLinkToFb(notification_url);
        }else {


            APIInterface apiService =
                    APIClient.getClient().create(APIInterface.class);
            Call<Example> call = apiService.getAPIResponse(token);
            call.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                    Toast.makeText(MainActivity.this, "Api Called", Toast.LENGTH_SHORT).show();
                    sendNotiication(response.body().getData().get(0).getUrl());
                    Picasso.get().load(response.body().getData().get(0).getUrl()).into(showImage);

                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {

                    Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            });
        }
//        PrintKeyHash();
    }

    private void sendLinkToFb(String notification_url) {

        Picasso.get().load(notification_url).error(R.drawable.happycustomer).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                showImage.setImageBitmap(bitmap);

                SharePhoto sharePhoto=new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();

                if (ShareDialog.canShow(SharePhotoContent.class)){
                    content=new SharePhotoContent.Builder()
                            .addPhoto(sharePhoto)
                            .build();
                    shareDialog.show(content);
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void PrintKeyHash() {

        try {
            PackageInfo info=getPackageManager().getPackageInfo("com.ravinder.taskproject",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    private void sendNotiication(String url) {
        String CHANNEL_ID = createNotificationChannel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notificationChannel", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.happycustomer)
                .setContentTitle("Content details")
                .setContentText("Message details")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setSound(uri);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = (int) (System.currentTimeMillis() / 4);
        notificationManager.notify(notificationId, mBuilder.build());
    }

    private String createNotificationChannel() {
        String channelName = "privaetChannel";
        String channelDesc = "channelDesc";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelName, channelName, importance);
            channel.setDescription(channelDesc);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            NotificationChannel currChannel = notificationManager.getNotificationChannel(channelName);
            if (currChannel == null)
                notificationManager.createNotificationChannel(channel);
        }
        return channelName;

    }

}