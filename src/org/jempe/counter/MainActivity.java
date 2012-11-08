package org.jempe.counter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView mDisplayCount;
	private TapCounter mTapCounter = new TapCounter();
	public static final String PREFS_NAME = "CounterPrefs";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mDisplayCount = (TextView)findViewById(R.id.displayCount);
    }
    
    public void onResume()
    {
    	super.onResume();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		mTapCounter.changeCount(settings.getInt("latest_count", 0));
		
        String currentCount = mTapCounter.getCount();
        
        mDisplayCount.setText(currentCount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void incCount(View view)
    {
    	mTapCounter.increaseCount();
    	saveCount();
    	
        String currentCount = mTapCounter.getCount();
        
        mDisplayCount.setText(currentCount);
    }
    
    private void saveCount()
    {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("latest_count", mTapCounter.tap_count);

		editor.commit();
    }
}
