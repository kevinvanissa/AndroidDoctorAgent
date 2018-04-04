package doctor.client.gui;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.ControllerException;
import jade.wrapper.O2AException;
import jade.wrapper.StaleProxyException;

import doctor.client.agent.DoctorClientAgent;
import doctor.client.agent.DoctorClientInterface;

import doctor.client.agent.AHP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {
	
	 EditText task;
	 EditText task2;
	 LinearLayout ll;
	 Button button2;
	 Button button3;
	 Button button4;
	 Button button5;
	 ArrayList<EditText> task3 = new ArrayList<EditText>();
	 ArrayList<EditText> task4 = new ArrayList<EditText>();
	 ArrayList<Spinner> task4s = new ArrayList<Spinner>();
	 ArrayList<TextView> task4st = new ArrayList<TextView>();
	 ArrayList<EditText> task5 = new ArrayList<EditText>();
	 ArrayList<Spinner> task5s = new ArrayList<Spinner>();
	 ArrayList<TextView> task5st = new ArrayList<TextView>();
	 //==============CheckBoxes==================
	 ArrayList<CheckBox> task6cb = new ArrayList<CheckBox>();
	 
	 
	 String [] weights = {"Equal Importance","Moderate Importance","Strong Importance","Very Strong Importance","Extreme Importance"};
	 HashMap<String,Double> weightsValue = new HashMap<String,Double>();
	
	 String [] weights2 = {"1","2","3","4","5"};
	 HashMap<String,Integer> weightsValue2 = new HashMap<String,Integer>();
	 
	 ArrayAdapter<String> adapter;
	 ListView listView;
	 Button selectButton;
	 
	 
	 int n;
	 int NUMBER_COMPARISON;
	 double [] p;
	 boolean [] cBox;
	 double [][] a;
	 double [][] switcha;
	 double [][] b;
	 double [] c;
	 String [] criteria;
	 double cRatio=0;
	 
	 //Alternatives
	 int num_alternatives;
	 int NUM_COMP_ALTERNATIVES;
	 double [][] d;
	 
	 //FIXME: Should update with Values from Server
	 String [] alternatives =  {"Panadol","Cataflam","Voltaren","Barilgin"};
	 String [] alternatives_temp;
	 
	    double [] f;
	    //String [] alternatives = new String[num_alternatives];
	    //ArrayList to store the list of 2d arrays used for alternatives
	    ArrayList<double[][]> storeMatrices = new ArrayList<double[][]>();
	    //Arraylist to store the double arrays
	    ArrayList<int[]> myInts = new ArrayList<int[]>();
	    //Arraylist to store calculations from combining criteria and alternatives
	    ArrayList<double[]> myDoubles = new ArrayList<double[]>();
	   //Arraylist to store eigenvectors for alternatives 
	    ArrayList<double[]> myeigenvectors = new ArrayList<double[]>();
	    //Final 2x2 matrix for final calculation
	    double [][] finalMatrix;
	    //Final double for final value 
	    double[][] finalSingleMatrix = new double[num_alternatives][1];
	    String patientID;
	    String patientName;
	    String diagnosis;
	    String diagnosisName;
	    private DoctorClientInterface doctorClientInterface;
	    DoctorClientAgent doctorAgent;
	    private String nickname;
	    private String pDrug;
	    private String pDrugName;
	    private String firstWord="";
	    private String mechanismCode;
	    private String pharmacoCode;
	    private String physiologicCode;
	    private String therapeuticCode;
	    private String radioChoice;
	    private double matchPercent=0;
	    
       //================Reference Drug================
	    final int REFERENCE_RATING = 3;
	    double reference_average = 0;
	    String reference_drug = "DRUG007";
	   //=================================================
	    
	    private AlertDialog myDialog;
	    private String[] items = {"Warrior","Archer","Wizard","Warrior2","Warrior in the Land","Warrior in the Landa","Warrior in the Landb",
	    		"Warrior in the Lanva","Warrior in thedsdf","Warrior in the Landa"};
	    private ArrayList<Integer> selectedItems = new ArrayList<Integer>();
	    boolean STOP=true;

	    
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Bundle extras = getIntent().getExtras();
			if (extras != null) {
				patientID = extras.getString("PatientID");
				patientName = extras.getString("PatientName");
				diagnosis = extras.getString("DiagnosisID");
				diagnosisName = extras.getString("DiagnosisName");
				nickname  = extras.getString("nickname");
				pDrug     = extras.getString("pDrug");
				pDrugName     = extras.getString("pDrugName");
				mechanismCode = extras.getString("MechanismCode");
				pharmacoCode  = extras.getString("PharmacoCode");
				physiologicCode = extras.getString("PhysiologicCode");
				therapeuticCode = extras.getString("TherapeuticCode");
				radioChoice = extras.getString("radioChoice");
				
			}
			
			try {
							
				doctorClientInterface = MicroRuntime.getAgent(nickname).getO2AInterface(DoctorClientInterface.class);
			    radioChoice="NO";  
				doctorClientInterface.setPatientDiagnosis(patientID, diagnosis,radioChoice,mechanismCode,pharmacoCode,physiologicCode,therapeuticCode);
				
				
			} catch (StaleProxyException e) {
				showAlertDialog(getString(R.string.msg_interface_exc), true);
			} catch (ControllerException e) {
				showAlertDialog(getString(R.string.msg_controller_exc), true);
			}
			
			
			     
	        weightsValue.put("Equal Importance", 1.0);
	        weightsValue.put("Moderate Importance", 3.0);
	        weightsValue.put("Strong Importance", 5.0);
	        weightsValue.put("Very Strong Importance", 7.0);
	        weightsValue.put("Extreme Importance", 9.0);
	        
	        
	        weightsValue2.put("1", 1);
	        weightsValue2.put("2", 2);
	        weightsValue2.put("3", 3);
	        weightsValue2.put("4", 4);
	        weightsValue2.put("5", 5);
	        
	        
	        	        
	        ScrollView sv = new ScrollView(this);
	        ll =new LinearLayout(this);
	        ll.setOrientation(LinearLayout.VERTICAL);
	        //ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
	        sv.addView(ll);
	        
	        
	         
		    try{
		    	String tmpAlternatives ="";
		    	while (tmpAlternatives == ""){
		    		tmpAlternatives = doctorClientInterface.getAlternatives();
		    	}
		    	
		    	alternatives = tmpAlternatives.split("!");
		    	
		    	while (firstWord == ""){
		    		firstWord = doctorClientInterface.getFirstWord();
		    	}
		    	
			}catch(Exception e){}
		    
		    if(firstWord.equals("Case-Based-Result:")){
		    	matchPercent=89.1;
		    	
		    	TextView tv_r = new TextView(ResultActivity.this);
		    	//tv_r.setBackgroundColor(0xFFFF00FF);
		    	tv_r.setBackgroundColor(Color.CYAN);
		        tv_r.setTextColor(0xFF000000);
		        tv_r.setTypeface(null, Typeface.BOLD);
		    	//TextView tv_r = new TextView(ResultActivity.this);
			    tv_r.setText("Result of Case-Based Reasoner:\n");
			    ll.addView(tv_r);
			    
			    TextView tv_r1 = new TextView(ResultActivity.this);
			    tv_r1.setText("PatientID: "+ patientID +" ("+patientName+")");
			    ll.addView(tv_r1);
			    
			    

			    TextView tv_r2 = new TextView(ResultActivity.this);
			    tv_r2.setText("Diagnosis: "+ diagnosis+" ("+diagnosisName+")");
			    ll.addView(tv_r2);

                TextView tv_r3 = new TextView(ResultActivity.this);
			    tv_r3.setText("Drug Choosen: "+ alternatives[0]);
			    ll.addView(tv_r3);

                TextView tv_r4 = new TextView(ResultActivity.this);
			    tv_r4.setText("\n Rationale: \n Closest Match to Patient: "+matchPercent+"%");
			    ll.addView(tv_r4);
			    
		    	
//		    	 for(int i=0;i<alternatives.length;i++){
//				    	TextView tv = new TextView(ResultActivity.this);
//				        tv.setText(alternatives[i]);
//				        ll.addView(tv);
//				    }
		    	
		    }else{
		    	
		    	
		    	
		        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
			    builder.setTitle("Choose your Drugs");
			    //builder.setIcon(R.drawable.snowflake);
			    builder.setMultiChoiceItems(alternatives, null, new DialogInterface.OnMultiChoiceClickListener() {

			    @Override
			    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			    if (isChecked){
			    selectedItems.add(which);
			    } else if (selectedItems.contains(which)){
			    selectedItems.remove(Integer.valueOf(which));
			    }

			    }
			    });
			    
			    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			    	@Override
			    	public void onClick(DialogInterface dialog, int which) {
			    	alternatives_temp = new String[selectedItems.size()];
			    	for(int i=0; i<selectedItems.size(); i++){
			    	Toast toast = Toast.makeText(getApplicationContext(), "Selected: " + alternatives[(Integer) selectedItems.get(i)], Toast.LENGTH_SHORT);
			    	toast.show();
			    	alternatives_temp[i] = alternatives[(Integer) selectedItems.get(i)];
			    	}
			    	TextView tvr = new TextView(ResultActivity.this);
				    tvr.setText("Alternatives Received:\n");
				    
				    ll.addView(tvr);
				    alternatives = alternatives_temp;
				    
				    for(int j=0;j<alternatives.length;j++){
				    	TextView tv = new TextView(ResultActivity.this);
				        tv.setText(alternatives[j] + "\n=====================");
				        ll.addView(tv);
				        
				    }
				    	        
				    TextView atv = new TextView(ResultActivity.this);
			        atv.setText("\n Enter the NUMBER of Criteria below:\n");
			        ll.addView(atv);
				    
				    
				    
			        task = new EditText(ResultActivity.this);
			        task.setMaxWidth(100);
			        task.setMinHeight(100);
			        task.setHint("Number of Criteria");
			        ll.addView(task);
			        
			        Button b = new Button(ResultActivity.this);
			        b.setText("SUBMIT");
			        b.setOnClickListener(criteriaListener);
			        ll.addView(b);
			    	
			    	
			    	}
			    	});
			    
			    builder.setCancelable(false);
			    myDialog = builder.create();
			    myDialog.show();
			    
	        task2 = new EditText(this);
	        task2.setMaxWidth(100);
	        task2.setMinHeight(100);
	        task2.setHint("Enter the Number of Alternatives..");
	        
	        button3 = new Button(this);
	        button3.setText("Save Criteria");
	        button3.setOnClickListener(saveCriteriaListener);
	        
	        button4 = new Button(this);
	        button4.setText("Compare Criteria");
	        button4.setOnClickListener(compareCriteriaListener);
	        
	        button5 = new Button(this);
	        button5.setText("Start Alogrithm");
	        button5.setOnClickListener(startAHPListener);
		    }//endif
		    
		    
		
	        setContentView(sv);
	        
	        //setContentView(R.layout.activity_result);
	    }
	 
	 
	    private OnClickListener criteriaListener = new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s = task.getText().toString();	
				n = Integer.parseInt(s.trim());
			    NUMBER_COMPARISON=(n*n-n)/2;
			    a = new double[n][n];//used to hold the values of comparisons
			    ll.removeAllViewsInLayout();
			    
				num_alternatives = alternatives.length;
				ll.removeAllViewsInLayout();
			    for(int i=0;i<n;i++){
			    	EditText et = new EditText(ResultActivity.this);
			    	et.setMaxWidth(100);
				    et.setMinHeight(100);
				    et.setHint("Enter Criteria "+(i+1));
			    	task3.add(et);
			    	
			    }
			    for(int i=0;i<n;i++){
			    	ll.addView(task3.get(i));
			    }
			    ll.addView(button3);
			}
	    };
	 
	 
	 
	    private OnClickListener saveCriteriaListener = new OnClickListener(){
			public void onClick(View v) {
				
				criteria = new String[n];
				
				for(int i=0;i<n;i++){
					criteria[i] = task3.get(i).getText().toString();
				}
				ll.removeAllViewsInLayout();
				
				for(int i=0; i<n;i++)
			        {
			            for(int j=i+1; j<n;j++)
			            {
			            	// Selection of the spinner
							//Spinner spinner = (Spinner) findViewById(R.id.myspinner);
							Spinner spinner = new Spinner(ResultActivity.this);
							// Application of the Array to the Spinner
							ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ResultActivity.this,android.R.layout.simple_spinner_item, weights);
							spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
							spinner.setAdapter(spinnerArrayAdapter);
							task4s.add(spinner);
			            	
			            					    	
					    	TextView tv = new TextView(ResultActivity.this);
					    	tv.setBackgroundColor(0xFFFF00FF);
					        tv.setTextColor(0xFF000000);
					        tv.setTypeface(null, Typeface.BOLD);
					    	
					        tv.setText("In respect to choosing a drug, how do you compare "+criteria[i].toUpperCase()+" against "+criteria[j].toUpperCase()+"?");
					        task4st.add(tv);
					        
					        CheckBox cb = new CheckBox(ResultActivity.this);
							cb.setText("Switch around criteria");
							task6cb.add(cb);
					    	
			            }
			        }
				
					for(int i=0;i<NUMBER_COMPARISON;i++){
						ll.addView(task4st.get(i));
						ll.addView(task6cb.get(i));
						ll.addView(task4s.get(i));
					}
					ll.addView(button4);
				
					
			}
	    };    
	    
	    
	    
	    private OnClickListener compareCriteriaListener = new OnClickListener(){
			public void onClick(View v) {
			
				num_alternatives = alternatives.length;
				p = new double[NUMBER_COMPARISON];
				cBox = new boolean[NUMBER_COMPARISON];
				f = new double[num_alternatives];
				NUM_COMP_ALTERNATIVES=(num_alternatives*num_alternatives-num_alternatives)/2;
			    d = new double[num_alternatives][num_alternatives];//used to hold the values of comparisons
				
				for (int i=0;i<NUMBER_COMPARISON;i++){
					//p[i] = Double.parseDouble((task4.get(i).getText().toString()));
					p[i] = weightsValue.get(task4s.get(i).getSelectedItem());
					cBox[i] = task6cb.get(i).isChecked();
					
				}
				ll.removeAllViewsInLayout();
				
				for(int k=0; k<n; k++){  
			        TextView tv = new TextView(ResultActivity.this);
			        tv.setBackgroundColor(0xFFFF00FF);
			        tv.setTextColor(0xFF000000);
			        tv.setTypeface(null, Typeface.BOLD);
			        tv.setText("[On a scale of 1-5 give a rating for the DRUGS below with respect to "+criteria[k].toUpperCase()+"]");
			        ll.addView(tv);
			        for(int i=0;i<num_alternatives;i++){
			          		            	
			            	Spinner spinner = new Spinner(ResultActivity.this);
							ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ResultActivity.this,android.R.layout.simple_spinner_item, weights2);
							spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
							spinner.setAdapter(spinnerArrayAdapter);
							task5s.add(spinner);
			            	
			            	EditText et = new EditText(ResultActivity.this);
			            	et.setMaxWidth(100);
						    et.setMinHeight(100);
			            	et.setHint("Give rating for: "+alternatives[i] +" against " +pDrugName);
			               // System.out.println("Compare "+alternatives[i]+" with "+alternatives[j]+":");
			            	
			            	TextView tv2 = new TextView(ResultActivity.this);
					        tv2.setText("Give rating for:  "+alternatives[i]+" against "+pDrugName.toUpperCase());
					        task5st.add(tv2);
			            	
			            	task5.add(et);
			            	ll.addView(tv2);
			            	ll.addView(spinner);
			        }
			    }//main for loop
				System.out.println("The size of task5s is: "+task5s.size());
				ll.addView(button5);
			}
	    };
	    
	    private OnClickListener startAHPListener = new OnClickListener(){
	 			public void onClick(View v) {
	 				int [] z = new int[num_alternatives];
	 				int checkMultiple = 1;
	 				int counter = 0;
	 				
	 				for(int i=0;i<task5s.size();i++){
	 					if(checkMultiple % num_alternatives == 0){
	 						z[counter] = weightsValue2.get(task5s.get(i).getSelectedItem().toString());
	 						myInts.add(z);
	 						z = new int[num_alternatives];
	 						counter = 0;
	 					}else{
	 						z[counter] = weightsValue2.get(task5s.get(i).getSelectedItem().toString());
	 						counter++;
	 					}
	 				    checkMultiple++;
					}
	 				
	 			for(int k=0;k<n;k++){
	 				System.out.println("Checking MyInts at Array: "+k);
	 				for(int temp: myInts.get(k)){
	 					System.out.print(temp + " ");
	 				}
	 				System.out.println("Next Array");
	 			}
	 				
	 			AHP ahp = new AHP();
	 			finalMatrix = new double[num_alternatives][n];
	 			
	 			for(double d: p){
	 				System.out.println("Element in double p: " +d);
	 				
	 			}
	 			
	 			a = ahp.initialize_matrix(p,cBox,n);
	 			System.out.println("Matrix Before Switch: =>");
	 			ahp.show_matrix(a);
	 			switcha = ahp.performSwitch(a,n);
	 			System.out.println("Matrix After Switch: =>");
	 			ahp.show_matrix(switcha);
	 			
	 		    b = ahp.mult_matrix(switcha,switcha);
	 		    System.out.println("Matrix After Squared: =>");
	 		    ahp.show_matrix(b);
	 		    c = ahp.cal_eigenvector(b);
	 		    System.out.println("Eigenvector: =>");
	 		    ahp.show_array(c);
	 		    cRatio = ahp.calculateCR(switcha,c,n);
	 		    System.out.println("Consistency Ratio: "+cRatio);
	 		    
	 		    //=================Calculation for reference Drug Average=============
	 		    double rSum = 0;
	 		    for(double r_average: c){
	 		    	rSum = (r_average * REFERENCE_RATING)+rSum;
	 		    	System.out.println("rSum: "+ rSum);
	 		    }
	 		    reference_average = rSum;
	 		    //reference_average = rSum/c.length;
	 		    System.out.println("reference average: "+reference_average);
	 		   //======================================================================== 
	 		    
	 		    	for(int k=0;k<n;k++){
	 		    		int s=0;
	 		    		double[] r =  new double[num_alternatives];
	 		    		for(int rating: myInts.get(k)){
	 		    			 r[s] = c[k] * rating;
	 		    			 System.out.println("eigenvector value: "+c[k]);
	 		    			 System.out.println("rating to be multiplied: "+rating);
	 		    			 System.out.println("Result: "+r[s]);
	 		    			 s++;
	 		    		}//innermost
	 		    		myDoubles.add(r);
	 		    	}//End second for
	 	
	 		    
	 		    
	 		 double [] finalWeighting = new double[num_alternatives];   
             //calculate the weighted average
	 		 for(int k=0;k<num_alternatives; k++){
	 			 double sum = 0.0;
	 			for(double[] cal_ratings: myDoubles){
	 				  sum = cal_ratings[k] + sum;
	 			}
	 			finalWeighting[k] = sum;
	 			//finalWeighting[k] = sum/myDoubles.size();
	 		 }
	 		    
	 		
	 	   ahp.show_array(finalWeighting);
	 	    
	 	   Intent showResults = new Intent(ResultActivity.this,
					AHPActivity.class);
	 	   ArrayList<String> alternativeList = new ArrayList<String>();
	 	   
	 	   showResults.putExtra("rDrug", pDrug);
	 	   showResults.putExtra("rDrugName", pDrugName);
	 	   showResults.putExtra("rAverage", reference_average);
	 	   showResults.putExtra("patientID", patientID);
	 	   showResults.putExtra("patientName", patientName);
	 	   showResults.putExtra("diagnosis", diagnosis);
	 	   showResults.putExtra("diagnosisName", diagnosisName);
	 	   showResults.putExtra("cRatio", cRatio);
	 	  
	 	   
	 	    for(int i=0;i<alternatives.length;i++){
	 	    	showResults.putExtra(alternatives[i], finalWeighting[i]);
	 	    	alternativeList.add(alternatives[i]);
	 	    }
	 	    showResults.putStringArrayListExtra("alternativesList", alternativeList);
			ResultActivity.this.startActivityForResult(showResults, 0);
	 			}
	 	    };
	    
	 		private void showAlertDialog(String message, final boolean fatal) {
	 			AlertDialog.Builder builder = new AlertDialog.Builder(
	 					ResultActivity.this);
	 			builder.setMessage(message)
	 					.setCancelable(false)
	 					.setPositiveButton("Ok",
	 							new DialogInterface.OnClickListener() {
	 								public void onClick(
	 										DialogInterface dialog, int id) {
	 									dialog.cancel();
	 									if(fatal) finish();
	 								}
	 							});
	 			AlertDialog alert = builder.create();
	 			alert.show();		
	 		}  
}
