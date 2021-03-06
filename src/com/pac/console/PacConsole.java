package com.pac.console;

import java.util.ArrayList;

import com.pac.console.adapters.drawerItemAdapter;
import com.pac.console.adapters.drawerItemType;
import com.pac.console.ui.About_frag;
import com.pac.console.ui.Changes_frag;
import com.pac.console.ui.Contrib_frag;
import com.pac.console.ui.OTA_frag;

import android.media.audiofx.BassBoost.Settings;
import android.net.Uri;
import android.os.Bundle;
import android.pacstats.PACStats;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PacConsole extends Activity {

    private ArrayList<drawerItemType> mGameTitles;
    private ListView mDrawerList;
    
    private drawerItemType mSelectedItem;
    
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int poss = 0;
    private boolean state = false;
    Fragment mContent = null;
    @Override
    public void onSaveInstanceState(Bundle ofLove) {
      super.onSaveInstanceState(ofLove);
      // Save UI state changes to the savedInstanceState.
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
      ofLove.putInt("flag", poss);
      ofLove.putBoolean("store", true);

    }
    
    @Override
    public void onRestoreInstanceState(Bundle ofLove) {
      super.onRestoreInstanceState(ofLove);
      // Restore UI state from the savedInstanceState.
      // This bundle has also been passed to onCreate.
      poss = ofLove.getInt("flag");
      state = ofLove.getBoolean("store");

    }

    @Override
    protected void onCreate(Bundle ofLove) {
        super.onCreate(ofLove);
        
        if (ofLove!=null){
	        poss = ofLove.getInt("flag");
	        state = ofLove.getBoolean("store");
        } else {
        	Intent intent = getIntent();
	        poss = intent.getIntExtra("flag", 0);
	        state = intent.getBooleanExtra("store", false);
        }
        
        
        setContentView(R.layout.pac_console);
       
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  
                mDrawerLayout,         
                R.drawable.ic_drawer, 
                R.string.app_name, 
                R.string.app_name  
                ) {
        	
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            	//TODO Update the actionbar title
            	if(mSelectedItem != null){
            		getActionBar().setTitle(mSelectedItem.title);
            	}
            	
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            	//TODO Update the actionbar title
            	getActionBar().setTitle(PacConsole.this.getResources().getString(R.string.app_name));

            }
        };
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
                
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        //make our list for the drawer
        createDrawList();
        
        mDrawerList.setAdapter(new drawerItemAdapter(this,R.layout.drawer_list_item, mGameTitles));
        
        mDrawerList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// ATTACH req fragment to content view
				attachFrag(arg2);
				mDrawerList.setSelection(arg2);
				poss = arg2;
			}

        	
        });
        
        // setup the drawer tab
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        if (!state){
        	mDrawerLayout.openDrawer(mDrawerList);
        } else {
        	attachFrag(poss);
        }

    }
	private void attachFrag(int possition) {
		// TODO swap fragment out.
		
		/**
		 * use tag to select the frag needed.
		 */
	        Fragment fragment = null;
	        android.app.FragmentManager fragmentManager = getFragmentManager();
	        if (fragment == null){
		        if (mGameTitles.get(possition).FLAG.equalsIgnoreCase("ota")){
		        	fragment = new OTA_frag();
		        } else if (mGameTitles.get(possition).FLAG.equalsIgnoreCase("contributors")){
		        	fragment = new Contrib_frag();
		        } else if (mGameTitles.get(possition).FLAG.equalsIgnoreCase("about")){
		        	fragment = new About_frag();
		        } else if (mGameTitles.get(possition).FLAG.equalsIgnoreCase("stats")){
		        	fragment = new PACStats();
		        } else if (mGameTitles.get(possition).FLAG.equalsIgnoreCase("changes")){
		        	fragment = new Changes_frag();
		        }

	        // Insert the fragment by replacing any existing fragment
	        fragmentManager.beginTransaction()
	                       .replace(R.id.content_frame, fragment, mGameTitles.get(possition).FLAG)
	                       .commit();
	    }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(possition, true);
        
        mSelectedItem = mGameTitles.get(possition);
        
        mDrawerLayout.closeDrawer(mDrawerList);
		
	}

    private void createDrawList(){
    	
        mGameTitles = new ArrayList<drawerItemType>();

        // ok here we go!
        
        /**
         * list is as follows
         * 
         * Update ROM
         * PA Stuff		|
         * CM Stuff		|-- All grouped by type 
         * AOKP Stuff	|		eg. all display options together, all sound options together.
         * PAC Stuff	|
         * 
         * About us
         * Contributors
         * Help
         */
        // OTA Frag
        drawerItemType holder = new drawerItemType();
        holder.title = this.getResources().getString(R.string.ota_menu_lbl);
        holder.caption = this.getResources().getString(R.string.ota_menu_cap);
        holder.caption_display = true;
        holder.FLAG = "ota";
       
        mGameTitles.add(holder);
        
        //Contributers
        holder = new drawerItemType();
        holder.title = this.getResources().getString(R.string.contrib_menu_lbl);
        holder.caption = this.getResources().getString(R.string.contrib_menu_cap);
        holder.caption_display = true;
        holder.FLAG = "contributors";
       
        mGameTitles.add(holder);
        
        //Changes
        holder = new drawerItemType();
        holder.title = this.getResources().getString(R.string.change_menu_lbl);
        holder.caption = this.getResources().getString(R.string.change_menu_cap);
        holder.caption_display = true;
        holder.FLAG = "changes";
       
        mGameTitles.add(holder);

        // About PAC Frag and set as default.
        holder = new drawerItemType();
        holder.title = this.getResources().getString(R.string.stat_menu_lbl);
        holder.caption = this.getResources().getString(R.string.stat_menu_cap);
        holder.caption_display = true;
        holder.FLAG = "stats";
       
        mGameTitles.add(holder);
        
        Fragment fragment = new About_frag();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment)
                       .commit();

        mDrawerList.setItemChecked(mGameTitles.size()-1, true);       
        mSelectedItem = mGameTitles.get(mGameTitles.size()-1);
        
        // Help Frag
        holder = new drawerItemType();
        holder.title = this.getResources().getString(R.string.help_menu_lbl);
        holder.caption = this.getResources().getString(R.string.help_menu_cap);
        holder.caption_display = true;
        holder.FLAG = "about";
       
        mGameTitles.add(holder);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.pac_console, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // TODO Handle your other itmes...

        return super.onOptionsItemSelected(item);
    }

    
}
