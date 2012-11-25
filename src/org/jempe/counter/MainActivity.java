package org.jempe.counter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Vibrator mVibrator = null;
	private TextView mDisplayCount;
	private TapCounter mTapCounter = new TapCounter();
	public static final String PREFS_NAME = "CounterPrefs";
	private TextView mTapMessage;
	private TextView mDecreaseMessage;
	private TextView mCounterName;
	private ImageView mCountSign;
	private boolean mTapMessageHidden;
	private Typeface mNunitoBold;
	private Typeface mNunito;
	private int mWidth; 
	private int mHeight;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mDisplayCount = (TextView)findViewById(R.id.displayCount);
        mTapMessage = (TextView)findViewById(R.id.tap_to_count_message);
        mDecreaseMessage = (TextView)findViewById(R.id.decrease_message);
        mCounterName = (TextView)findViewById(R.id.CounterName);
        mCountSign = (ImageView) findViewById(R.id.countSign);
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        
        // Load fonts from assets folder
        mNunitoBold = Typeface.createFromAsset(getAssets(), "Nunito-Bold.ttf");  
        mNunito = Typeface.createFromAsset(getAssets(), "Nunito-Regular.ttf");  
        
        // assign fonts to textViews
        mDisplayCount.setTypeface(mNunito);
        
        // get screen size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mHeight = metrics.heightPixels;
        mWidth = metrics.widthPixels;
        
        // set the size of the textviews
        mDisplayCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, mHeight / 8);
        mCounterName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mHeight / 18);
        
        int hintFontRatio = 24;
        
        if(mHeight > 1000)
        {
        	hintFontRatio = 36;
        }
        else if(mHeight > 600)
        {
        	hintFontRatio = 32;
        }
        
        mTapMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, mHeight / hintFontRatio);
        mDecreaseMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, mHeight / hintFontRatio);
        
        int sign_width = (int) mHeight / 9;
        
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(sign_width, sign_width);
        layoutparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutparams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutparams.rightMargin = 20;
        layoutparams.topMargin = (int) ((mHeight - (mHeight / 1.618)) - (sign_width * 1.5));
        mCountSign.setLayoutParams(layoutparams);

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
    	AlphaAnimation fadeOutAnimation = new AlphaAnimation(1, (float) 0);
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
    	
    	AlphaAnimation fadeInAnimation = new AlphaAnimation((float) 0, 1);
    	fadeInAnimation.setDuration(1500);
    	fadeInAnimation.setFillAfter(true);
    	
    	mTapMessage.setAnimation(fadeInAnimation);
    	mTapMessageHidden = false;
    }
}
