package com.example.hellomap;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Driving_tips extends Activity {
	private TextView batteryInfo,tv_name,num1,num2;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.driving_info);
		//textview setting for supravas battery status
		batteryInfo=(TextView)findViewById(R.id.Battery_Status);
		tv_name = (TextView) findViewById(R.id.from_profile_user_name);
		num1 = (TextView) findViewById(R.id.Number1);
		num2 = (TextView) findViewById(R.id.Number2);
		
		
		pref = getSharedPreferences("Registration", 0);
		String name = pref.getString("Name", null);
		tv_name.setText(name);
		
		pref = getSharedPreferences("Registration", 0);
		String n1 = pref.getString("Num1", null);
		num1.setText(n1);
		
		pref = getSharedPreferences("Registration", 0);
		String n2 = pref.getString("Num2", null);
		num2.setText(n2);
		
		 this.registerReceiver(this.batteryInfoReceiver,	
		 new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		ImageView welcome = (ImageView) findViewById(R.id.welcome);
        // Display the GIF (from raw resource) into the ImageView
        loadGifIntoImageView(welcome, R.raw.welcome_chosen);
		
		Button DashBoard= (Button)findViewById(R.id.Dash_board);
	    DashBoard.setOnClickListener(new OnClickListener() 
	    {   public void onClick(View v) 
	        {   
	    	    displayPopupWindow(v);
	            Intent intent = new Intent(Driving_tips.this,MainActivity.class);
	            String number1 = num1.getText().toString();
	            String number2 = num2.getText().toString();
	            if(TextUtils.isEmpty(number1))
	            { 
	             new AlertDialog.Builder(Driving_tips.this)
	            .setMessage("Please Fill the Contact Information Before you go to DashBoard")
	            .setCancelable(false)
	            .setPositiveButton("Profile", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	 Intent intent1 = new Intent(Driving_tips.this, Profile.class);
                    	 Driving_tips.this.startActivity(intent1);
                    	 overridePendingTransition(R.anim.right_in, R.anim.left_out); 
                   }
               })
                 .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                            dialog.cancel();
	                            
	                       }
	                   })
	           
	             .show();
	             
	             
	            // response is either null or empty
	            }else{
	            intent.putExtra("number1",number1);
	            intent.putExtra("number2",number2);
	            startActivity(intent);     
	            overridePendingTransition(R.anim.right_in, R.anim.left_out);
	                finish();
	        }
	        }
	    
		private void displayPopupWindow(View v) {
			
			// TODO Auto-generated method stub
			PopupWindow popup = new PopupWindow(Driving_tips.this);
		    View layout = getLayoutInflater().inflate(R.layout.pop_up_content, null);
		    popup.setContentView(layout);
		    // Set content width and height
		    popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		    popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		    // Closes the popup window when touch outside of it - when looses focus
		    popup.setOutsideTouchable(true);
		    popup.setFocusable(true);
		    // Show anchored to button
		    popup.setBackgroundDrawable(new BitmapDrawable());
		    popup.showAsDropDown(v);
			
		}
	});
	    

	    Button quit_supra = (Button) findViewById(R.id.Quit_supravas);
	    quit_supra.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	showDialog(DIALOG_XYZ);
	            
	        }
	    });
}
	
	private final static int DIALOG_XYZ = 0;

	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	        case DIALOG_XYZ:
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setMessage("Are you sure you want to quit 'Supravas' ")
	                   .setCancelable(false)
	                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                            Driving_tips.this.finish();	                            
	                            System.exit(0);
	                       }
	                   })
	                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                            dialog.cancel();
	                       }
	                   });
	            Dialog alert = builder.create();
	            return alert;
	    }
	    return null;
	}
	 // Gif images code
	
	protected void loadGifIntoImageView(ImageView ivImage, int rawId) {
        try {
            GifAnimationDrawable anim = new GifAnimationDrawable(getResources().openRawResource(rawId));
            ivImage.setImageDrawable(anim);
            ((GifAnimationDrawable) ivImage.getDrawable()).setVisible(true, true);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		 @Override
		 public void onReceive(Context context, Intent intent) {
			 int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
			 batteryInfo.setText(" Battery status:                  " +level+"%"+"\n");
		 }
	};
	
	public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.second_screen_menu, menu);
        return true;
    }
	public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.about:
            LayoutInflater inflater= LayoutInflater.from(this);
        	View view=inflater.inflate(R.layout.about_supravas, null);
        	TextView textview_about=(TextView)view.findViewById(R.id.textmsg);
        	textview_about.setText("About");
        	AlertDialog.Builder alertDialog_about = new AlertDialog.Builder(this);  
        	alertDialog_about.setTitle("About Supravas");  
        	alertDialog_about.setMessage("'Supravas – An Accident Detector' is an android application. This application is designed to reduce  the mortality rate due to road accidents. When a person has met with an accident this application detects it and gives an alert to the user. If accident has really occurred or if the person is injured he can press the “yes” button in the alert dialog so that an automatic text message is sent to the concerned person. The application also shows the current battery status and also provides the exact gps location to the user.This application is developed as part of our engineering final year academic project with the objective of saving a life.");  
        	alertDialog_about.setPositiveButton("OK", null);  
        	AlertDialog alert = alertDialog_about.create();
        	alert.show();
            return true;
        case R.id.tips:
        	LayoutInflater inflater1= LayoutInflater.from(this);
        	View view1=inflater1.inflate(R.layout.driving_tips, null);
        	TextView textview_tips=(TextView)view1.findViewById(R.id.textview_tips);
        	textview_tips.setText("fill the tips its scrollable space");
        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);  
        	alertDialog.setTitle("Few major driving tips ");  
        	alertDialog.setMessage("1.Safety should always be of top priority when purchasing a vehicle."+ "\n"+"2.Keep vehicle in good working condition."+"\n"+"3.Keep your eyes on road and hands on handle bars."+"\n"+"4.Stick to the left lane and leave the right lane to faster traffic."+
                	"\n"+"5.Don't beat traffic lights and also traffic signs."+"\n"+"6.Do not overtake turning vehicles and Never over take on the left."+"\n"+"7.Be extra careful in unexpected weather."+"\n"+"8.Maintain distance between vehicles."+"\n"+"9.Remember Speed ThrillS but Kills."+
                	"\n"+"10.Don't drink and DrIvE."+"\n"+"11.Wear helmet and save head."+"\n"+"12.Make sure you have a clear head before operating your vehicle"+"\n"+"13.Limit driving alone when tired."+"\n"+"14.Don't be in a hurry plan ahead."+"\n"+"15. Avoid aggressive driving by relaxing and having patience."+
                	"\n"+"16.2 stroke engines are big polluters because of carbon monoxide emission. Keep your engine tuned."+"\n"+"17. Do not use horn unnecessarily."+"\n"+"18. Remember you are vulnerable: in any collision you are more in danger than car or bus."+"\n"+"19.Avoid usage of mobile phones while driving"+
                	"\n"+"20.Be responsible – advice people who are not aware of the safety tips/measures."+"\n\n"+"Last but not least, HaPpY aNd SaFe DrIvInG :-)");  
            alertDialog.setPositiveButton("OK", null);
        	AlertDialog alert_tips = alertDialog.create();
        	alert_tips.show();
            return true;
        case R.id.Find_us:
        	  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.supravas.blogspot.in"));
        	  this.startActivity(browserIntent);
        	  return true;
        case R.id.Profile:
        	 Intent intent = new Intent(this, Profile.class);
             this.startActivity(intent);     
             overridePendingTransition(R.anim.right_in, R.anim.left_out); 
             return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	           .setMessage("Are you sure you want to exit Supravas?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    Driving_tips.this.finish();
	                    System.exit(0);
	               }
	           })
	           .setNegativeButton("No", null)
	           .show();
	}



}
