package org.jempe.counter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Vibrator mVibrator = null;
	private TextView mDisplayCount;
	private TapCounter mTapCounter = new TapCounter();
	public static final String PREFS_NAME = "CounterPrefs";
	private TextView mTapMessage;
	private TextView mDecreaseMessage;
	private boolean mTapMessageHidden;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mDisplayCount = (TextView)findViewById(R.id.displayCount);
        mTapMessage = (TextView)findViewById(R.id.tap_to_count_message);
        mDecreaseMessage = (TextView)findViewById(R.id.decrease_message);
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }
    
    public void onResume()
    {
    	super.onResume();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		mTapCounter.changeCount(settings.getInt("latest_count", 0));
		
        String currentCount = mTapCounter.getCount();
        
        mDisplayCount.setText(currentCount);
        showTapMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
     
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId()) {
        	case R.id.view_source_menu:
        		viewSource();
        		return true;
            case R.id.decrease_count_menu:
                decreaseCount();
                return true;
            case R.id.reset_count_menu:
            	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            	
            	builder.setMessage(R.string.reset_message)
                .setTitle(R.string.reset_title);
            	
            	// Add the buttons
            	builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            	            	resetCount();
            	           }
            	       });
            	builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            	           }
            	       });

            	// Create the AlertDialog
            	AlertDialog dialog = builder.create();
            	dialog.show();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void incCount(View view)
    {
    	mTapCounter.increaseCount();
    	saveCount();
    	
        String currentCount = mTapCounter.getCount();
        
        mDisplayCount.setText(currentCount);
    	mVibrator.vibrate(50);
    	if(mTapMessageHidden == false)
    	{
    		hideTapMessage();
    	}
    }
    
    public void decreaseCount()
    {
    	mTapCounter.decreaseCount();
    	saveCount();
    	
        String currentCount = mTapCounter.getCount();
        
        mDisplayCount.setText(currentCount);
    }
    
    public void resetCount()
    {
   		mTapCounter.resetCount();
   		saveCount();
	
   		String currentCount = mTapCounter.getCount();
    
   		mDisplayCount.setText(currentCount);
    }
    
    public void viewSource()
    {
    	Uri webpage = Uri.parse("https://github.com/jempe/-tapcounter-android");
    	Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
    	
    	startActivity(webIntent);
    }
    
    private void saveCount()
    {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("latest_count", mTapCounter.tap_count);

		editor.commit();
    }
    
    @Override
    public void onBackPressed()
    {
        decreaseCount();
    }
    
    private void hideTapMessage()
    {
    	AlphaAnimation fadeOutAnimation = new AlphaAnimation(1, 0);
    	fadeOutAnimation.setDuration(1500);
    	fadeOutAnimation.setFillAfter(true);
    	
    	mTapMessage.setAnimation(fadeOutAnimation);
    	mTapMessageHidden = true;
    	
    	AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1);
    	fadeInAnimation.setDuration(1500);
    	fadeInAnimation.setFillAfter(true);
    	
    	mDecreaseMessage.setAnimation(fadeInAnimation);
    }
    
    private void showTapMessage()
    {
    	AlphaAnimation fadeOutAnimation = new AlphaAnimation(1, 0);
    	fadeOutAnimation.setDuration(0);
    	fadeOutAnimation.setFillAfter(true);
    	
    	mDecreaseMessage.setAnimation(fadeOutAnimation);
    	
    	AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1);
    	fadeInAnimation.setDuration(1500);
    	fadeInAnimation.setFillAfter(true);
    	
    	mTapMessage.setAnimation(fadeInAnimation);
    	mTapMessageHidden = false;
    }
}
