package doctor.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.widget.LinearLayout;

import android.widget.TextView;

public class AHPActivity extends Activity {
	private LinearLayout ll;
	HashMap<String, Double> hmap = new HashMap<String, Double>();
	
	
	   public static Drawable createLine(int color, int height)
	    {
	        ShapeDrawable sd = new ShapeDrawable(new RectShape());

	        sd.setIntrinsicHeight(height);
	        Paint fgPaintSel = sd.getPaint();
	        fgPaintSel.setColor(color);
	        fgPaintSel.setStyle(Style.FILL);
	        fgPaintSel.setStrokeWidth(height);
	        fgPaintSel.setPathEffect(new DashPathEffect(new float[] { 5, 10 }, 0));
	        return sd;
	    }
	    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ahp);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        
        
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			
			TextView ptv = new TextView(AHPActivity.this);
			ptv.setBackgroundColor(0xFFFF00FF);
	        ptv.setTextColor(0xFF000000);
	        ptv.setTypeface(null, Typeface.BOLD);
			ptv.setText("Patient: (" + extras.getString("patientID") + ") "+extras.getString("patientName").toUpperCase());
			ll.addView(ptv);
			
			TextView ptv1 = new TextView(AHPActivity.this);
			ptv1.setText("Diagnosis: (" + extras.getString("diagnosis")+") "+extras.getString("diagnosisName"));
			ll.addView(ptv1);
			
			
			TextView rtv2 = new TextView(AHPActivity.this);
			rtv2.setText("Criteria Consistency Ratio: "+roundDecimal(extras.getDouble("cRatio")));
			ll.addView(rtv2);
			
			
			View v = new View(AHPActivity.this);
	        v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5));
	        v.setBackgroundDrawable(createLine(Color.RED, 5));
	        ll.addView(v);
	        
	        TextView wtv = new TextView(AHPActivity.this);
			wtv.setText("\n\n");
			ll.addView(wtv);        	
			
			//==============Reference Drug Info =====================
			TextView rtv1 = new TextView(AHPActivity.this);
			rtv1.setBackgroundColor(Color.CYAN);
	        rtv1.setTextColor(0xFF000000);
	        rtv1.setTypeface(null, Typeface.BOLD);
			rtv1.setText("Reference Drug and Rating:");
			ll.addView(rtv1);
			TextView rtv = new TextView(AHPActivity.this);
			rtv.setText(extras.getString("rDrugName").toUpperCase() + " =  " + roundDecimal(extras.getDouble("rAverage")));
			ll.addView(rtv);
			//========================================================
			
			 TextView wtv4 = new TextView(AHPActivity.this);
			 wtv4.setText("\n\n");
			 ll.addView(wtv4); 
			
			TextView rtv3 = new TextView(AHPActivity.this);
			rtv3.setBackgroundColor(Color.CYAN);
	        rtv3.setTextColor(0xFF000000);
	        rtv3.setTypeface(null, Typeface.BOLD);
			rtv3.setText("Alternative Drugs and Ratings:");
			ll.addView(rtv3);
			
			ArrayList<String> alternativesList = extras.getStringArrayList("alternativesList");
			
			for(int i=0;i< alternativesList.size();i++){
				//TextView tv = new TextView(AHPActivity.this);
				hmap.put(alternativesList.get(i).toString(), roundDecimal(extras.getDouble(alternativesList.get(i))));
				//tv.setText(alternativesList.get(i).toString() + " =  " + roundDecimal(extras.getDouble(alternativesList.get(i).toString())) );
				//ll.addView(tv);
			}
			
			 Map<String, Double> map = sortByValues(hmap); 
			   System.out.println("After Sorting:");
			      Set set2 = map.entrySet();
			      Iterator iterator2 = set2.iterator();
			      while(iterator2.hasNext()) {
			           Map.Entry me2 = (Map.Entry)iterator2.next();
			           System.out.print(me2.getKey() + ": ");
			           System.out.println(me2.getValue());
			           TextView tv = new TextView(AHPActivity.this);
			           tv.setText(me2.getKey() + " =  " + me2.getValue().toString());
			           ll.addView(tv);
			      }
			
		}
		setContentView(ll);
        
	}
	
	//method to convert to 2 decimal places
	private  double roundDecimal(double d){
	    double db=0.0;
	    DecimalFormat df = new DecimalFormat("#,##0.00");
	    try{
	            db = Double.valueOf(df.format(d));
	    }catch(NumberFormatException e){
	        e.printStackTrace();
	    }
	    return db;
	}
	
	
	  private HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o2, Object o1) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });
	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
}
