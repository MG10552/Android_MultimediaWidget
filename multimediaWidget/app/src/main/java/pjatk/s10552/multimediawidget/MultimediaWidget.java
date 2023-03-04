package pjatk.s10552.multimediawidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import static android.content.ContentValues.TAG;


public class MultimediaWidget extends AppWidgetProvider {

    private static SoundService service;
    private static int imageID = 0;
    private final int[] imgID = { R.drawable.alexandria_street, R.drawable.library_of_alexandria_sketch, R.drawable.alex_insides };

    protected static PendingIntent getPendingSelfIntent(Context context, String action){
        Intent intent = new Intent();
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent,0);
    }

    void changePic(RemoteViews views){
        imageID ++;
        if(imageID == imgID.length) imageID = 0;
            views.setImageViewResource(R.id.contentImage,
                    imgID[imageID]);
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        service = new SoundService(context);

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.multimedia_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);


        views.setOnClickPendingIntent(R.id.button , getPendingSelfIntent(context, "findOutMore"));
        views.setOnClickPendingIntent(R.id.button3 , getPendingSelfIntent(context, "soundsPlay"));
        views.setOnClickPendingIntent(R.id.contentImage , getPendingSelfIntent(context, "changeImage"));
        views.setOnClickPendingIntent(R.id.button2 , getPendingSelfIntent(context, "soundsPrev"));
        views.setOnClickPendingIntent(R.id.button4 , getPendingSelfIntent(context, "soundsNext"));

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.multimedia_widget);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        Log.i("-------------------", "I've got: " + intent.getAction());

        if (intent.getAction().equals("findOutMore")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.britannica.com/topic/Library-of-Alexandria"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
            Log.d(TAG, "onReceive: LoA - website should be loaded by now.");
        }
        else if (intent.getAction().equals("soundsPlay")) {
            service.playSounds();
            Log.d(TAG, "onReceive:  Sounds are playing.");
        }
        else if (intent.getAction().equals("changeImage")) {
            changePic(views);
            Log.d(TAG, "onReceive: Images Change.");
        }
        else if (intent.getAction().equals("soundsPrev")) {
            service.prevTrack();
            Log.d(TAG, "onReceive: Previous track.");
        }
        else if (intent.getAction().equals("soundsNext")) {
            service.nextTrack();
            Log.d(TAG, "onReceive: Next track.");
        }

        if(service.isPlaying()){
            views.setTextViewText(R.id.button3, "Stop");
        } else {
            views.setTextViewText(R.id.button3, "Play");
        }

        manager.updateAppWidget(new ComponentName(context,
                MultimediaWidget.class), views);
    }

}

