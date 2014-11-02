package org.lrazo.modernart;

import java.util.ArrayList;
import java.util.Random;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	private LinearLayout row1;
	private LinearLayout row2;
	private LinearLayout row3;
	private SeekBar seekBar;
	
	private int screenWidth;
	private int screenHeight;

	final Context mContext = this;
	
	private ArrayList<Pair<ImageView, float[]>> dynamicColorRectangles;
	private ArrayList<ImageView> staticColorRectangles;
	private static final int rectanglesPerRow = 2;

	private static final float sat = 0.5f;
	private static final float lum = 0.5f;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);        
        this.seekBar = (SeekBar) findViewById(R.id.seekBar);
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        
        this.refreshRectangles();
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				changeColors(progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		});

	}

    /**
     * Shift all the colored rectangle hues
     * @param value The amount to shift by
     */
    private void changeColors(int value) {
    	for (Pair<ImageView,float[]> pair : this.dynamicColorRectangles) {
    		ImageView image = pair.first;
    		ShapeDrawable rectangle = (ShapeDrawable) image.getDrawable();
    		
    		float[] hsvValues = pair.second;
    		
    		float hue = (hsvValues[0] + value) % 180;
    		
    		rectangle.getPaint().setColor(Color.HSVToColor(new float[]{hue, hsvValues[1], hsvValues[2]}));
    		image.invalidate();
    	}
    }

	private void refreshRectangles() {
        this.dynamicColorRectangles = new ArrayList<Pair<ImageView, float[]>>();
        this.staticColorRectangles = new ArrayList<ImageView>();
        
        this.row1 = (LinearLayout) findViewById(R.id.row1);
        this.row1.removeAllViews();
        this.addRectanglesToRow(row1,1);
        
        this.row2 = (LinearLayout) findViewById(R.id.row2);
        this.row2.removeAllViews();
        this.addRectanglesToRow(row2,2);
        
        this.row3 = (LinearLayout) findViewById(R.id.row3);
        this.row3.removeAllViews();
        this.addRectanglesToRow(row3,3);		
	}

	private void addRectanglesToRow(LinearLayout row, int nrow) {
    	int totalWidth = this.screenWidth;
    	
    	for (int i = 1; i <= rectanglesPerRow; i++) {
        	ImageView image = new ImageView(this);
        	ShapeDrawable rectangle = new ShapeDrawable(new RectShape());
        	rectangle.setIntrinsicHeight(this.screenHeight/3);
        	rectangle.setPadding(0, 0, 0, 0);
        	
        	if ((i & 1) == 0 && nrow == 2) {
	    		rectangle.getPaint().setColor(Color.LTGRAY);
	    		this.staticColorRectangles.add(image);
    		}
    		else {
	    		float[] hsvColor = this.generateRandomColor();
	    		rectangle.getPaint().setColor(Color.HSVToColor(hsvColor));
	    		this.dynamicColorRectangles.add(new Pair<ImageView, float[]>(image, hsvColor));
    		}
        	int width;
        	if (i < rectanglesPerRow) {
	        	width = (int) Math.round(Math.random() * (totalWidth / 1.5));
	        	totalWidth -= width;
        	}
        	else
        	{
        		width = totalWidth;
        	}
        	rectangle.setIntrinsicWidth(width);
        	image.setImageDrawable(rectangle);
        	row.addView(image);
        }
		
	}

    /**
     * Return a random color in HSV format
     * @return HSV as a float array
     */
    private float[] generateRandomColor() {
    	Random rand = new Random();
    	float h = rand.nextFloat()*180;
    	return new float[]{h, sat, lum};
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.more_info) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        	TextView myMsg = new TextView(this);
        	myMsg.setText(R.string.dialog_text);
        	myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        	builder.setView(myMsg)
        	//builder.setMessage(R.string.dialog_text)
		        	.setPositiveButton(R.string.visit_moma, new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       Intent startWeb = new Intent(MainActivity.this, WebActivity.class);
		                       startActivity(startWeb);
		                   }
		               })
		               .setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       dialog.cancel();
		                   }
		               });        	
        	builder.create().show();
            return true;
        }
        else if (id == R.id.refresh) {
        	this.refreshRectangles();
        	this.row1.invalidate();
        	this.row2.invalidate();
        	this.row3.invalidate();
        }
        return super.onOptionsItemSelected(item);
	}
}
