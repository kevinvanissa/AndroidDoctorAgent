package doctor.client.gui;

import doctor.client.agent.DoctorClientAgent;
import java.util.logging.Level;

import jade.android.AndroidHelper;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.AgentContainer;
import jade.core.MicroRuntime;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.Profile;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import doctor.client.agent.DoctorClientInterface; 
import doctor.client.agent.DoctorClientAgent;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity{
	private Logger logger = Logger.getJADELogger(this.getClass().getName());
	private MicroRuntimeServiceBinder microRuntimeServiceBinder;
	private ServiceConnection serviceConnection;
	private String nickname;
	private DoctorClientInterface doctorClientInterface;
	AgentController alphaAgent=null;
	
    private ArrayAdapter<String> adapter,adapter1,adapter2,adapter3,adapter4,adapter5,adapter6,adapter7;
    
    AutoCompleteTextView patientField=null,diagnosisField=null,pDrugField=null,mechanismField=null,
    		pharmacoField=null,physiologicField=null,therapeuticField=null;
	
    //TEST DATA FOR SAMPLE PATIENTS	
	String[] PATIENTS ={"Dundee Miller--P001", "John Brown--P002", "Stacy Walters--P003","Shereen Johnson--P004",
			"Judge Burke--P005","Fenton Ferguson--P006","Stacy Scarlett--P007","Annett Wilks--P008","Mark Foot--P009",
			"Sam Stewart--P010"};
	
	String[] DRUGS = {"DOXYCYCLINE--N0000146109", "Antidiarrheal--N0000178374", "Atenolol--N0000146784","Aspirin--N0000145918",
			"Tetracycline--N0000146005","WARFARIN--N0000148057","Penicillin--N0000147965"};
	
	String[] DIAGNOSIS = {"Diarrhea--N0000000963", "Syphilis--N0000002881", "Diabetes Mellitus--N0000000950","Hypertension--N0000001616",
			"Pregnancy--N0000010195","Pain--N0000002278","Asthma--N0000000498","Pneumonia--N0000002432","Pneumonia, Bacterial--N0000003812",
			"Pneumonia, Pneumococcal--N0000002435","Inflammation--N0000001687"};

	String [] MECHANISMACTION = {
			"P-Glycoprotein Inhibitors--N0000185503",
			"Cytochrome P450 2C9 Inhibitors--N0000185504",
			"Cytochrome P450 Inducers--N0000185505",
			"Cytochrome P450 3A4 Inducers--N0000185506",
			"Cytochrome P450 2C9 Inducers--N0000185507",
			"Cytochrome P450 2C19 Inducers--N0000185607",
			"Microsomal Triglyceride Transfer Protein Inhibitors--N0000186778",
			"alpha-Particle Emitting Activity--N0000187052",
			"RANK Ligand Blocking Activity--N0000187054",
			"Sodium-Glucose Transporter Inhibitors--N0000187056",
			"Sodium-Glucose Transporter 1 Inhibitors--N0000187057",
			"Sodium-Glucose Transporter 2 Inhibitors--N0000187058",
			"Organic Cation Transporter Interactions--N0000187060",
			"Organic Cation Transporter 2 Inhibitors--N0000187061",
			"Cytochrome P450 2C8 Inhibitors--N0000187062",
			"Cytochrome P450 2C8 Inducers--N0000187063",
			"Cytochrome P450 2B6 Inducers--N0000187064",
			"Membrane Transporter Interactions--N0000188367",
			"Fatty Acid Transporter Interactions--N0000188368",
			"Microsomal Triglyceride Transfer Protein Interactions--N0000188369",
			"Ion Transporter Interactions--N0000188370",
			"Anion Transporter Interactions--N0000188371",
			"Cation Transporter Interactions--N0000188372",
			"Proton Pump Interactions--N0000188373",
			"Antiporter Interactions--N0000188374",
			"Symporter Interactions--N0000188375",
			"Sodium Chloride Symporter Interactions--N0000188376",
			"Sodium Potassium Chloride Symporter Interactions--N0000188377",
			"Sodium-Glucose Transporter Interactions--N0000188378",
			"Monosaccharide Transporter Interactions--N0000188379",
			"Ligand-Gated Ion Channel Interactions--N0000188380",
			"Ionotropic Glutamate Receptor Interactions--N0000188381",
			"Adrenergic beta-Antagonists--N0000000161",
			"Adrenergic beta-Agonists--N0000000245",
			"Adrenergic beta1-Agonists--N0000009921",
			"Adrenergic beta2-Agonists--N0000009922",
			"Adrenergic beta1-Antagonists--N0000009923",
			"Adrenergic beta2-Antagonists--N0000009924",
			"Adrenergic beta3-Agonists--N0000185007"
	};
	
	String [] PHARMACOKINETICS = {
			"Clinical Kinetics--N0000000003",
			"Absorption--N0000000008",
			"Affected by food/other nutrients--N0000000009",
			"Decreased by food--N0000000010",
			"Increased by food--N0000000011",
			"Affected by gastric pH--N0000000012",
			"Enhanced by acidity--N0000000013",
			"Decreased by acidity--N0000000014",
			"Affected by intestinal surface area--N0000000015",
			"Affected by concomitant drug administration--N0000000016",
			"Absorption Half-Life--N0000000017",
			"Distribution--N0000000018",
			"Volume of distribution--N0000000019",
			"Site of concentration--N0000000020",
			"Site of relative exclusion--N0000000021",
			"Elimination--N0000000022",
			"Metabolism--N0000000023",
			"Site of Metabolism--N0000000024",
			"Intestinal Metabolism--N0000000025",
			"Hepatic Metabolism--N0000000026",
			"Pulmonary Metabolism--N0000000027",
			"Renal Metabolism--N0000000028",
			"Plasma Metabolism--N0000000029",
			"Enzymatic Pathway of Metabolism--N0000000030",
			"Cytochromes--N0000000031",
			"Esterases--N0000000032",
			"Epoxide hydrolases--N0000000033",
			"Glucuronosyl transferases--N0000000034",
			"Glutathione s-transferases--N0000000035",
			"Sulfotransferases--N0000000036",
			"N-acetyl transferases--N0000000037",
			"Thiopurine methyltransferase--N0000000038",
			"Monoamine oxidase--N0000000039",
			"Co-methyltransferase--N0000000040",
			"Route of Excretion--N0000000041",
			"Renal Excretion--N0000000042",
			"Hepatic Excretion--N0000000043",
			"Pulmonary Excretion--N0000000044",
			"Fecal Excretion--N0000000045",
			"Breast Milk Excretion--N0000000046",
			"Clinically Unimportant Route of Excretion--N0000000047",
			"Saliva Excretion--N0000000048",
			"Tear Excretion--N0000000049",
			"Sweat Excretion--N0000000050",
			"Skin Excretion--N0000000051",
			"Hair Excretion--N0000000052",
			"Nail Excretion--N0000000053",
			"Umbilical Lint Excretion--N0000000054",
			"Kinetics--N0000000055",
			"Order--N0000000056",
			"First Order--N0000000057",
			"Zero Order--N0000000058",
			"Compartments--N0000000059",
			"1-Compartment--N0000000060",
			"2-Compartment--N0000000061",
			"3-Compartment--N0000000062",
			"Clearance--N0000000063",
			"Half-Life--N0000000064",
			"Aromatic hydroxylation--N0000008289"
	};
	
	String [] PHYSIOLOGICEFFECT = {
			"Lymphocyte Activity Modulation--N0000182152",
			"T Lymphocyte Activity Modulation--N0000182153",
			"T Lymphocyte Costimulation Activity Modulation--N0000182154",
			"Decreased T Lymphocyte Activation--N0000182155",
			"Decreased B Lymphocyte Activation--N0000182156",
			"Increased T Lymphocyte Activation--N0000182157",
			"Increased B Lymphocyte Activation--N0000182158",
			"T Lymphocyte Costimulation Activity Blockade--N0000182829",
			"Acquired Immunity--N0000183363",
			"Actively Acquired Immunity--N0000183364",
			"Passively Acquired Immunity--N0000183365",
			"Monocyte Function Alteration--N0000183882",
			"Increased Monocyte Activation--N0000183883",
			"Increased Macrophage Proliferation--N0000183884",
			"Increased Coagulation Factor IX Activity--N0000184153",
			"Increased Coagulation Factor X Activity--N0000184154",
			"Increased Fibrin Polymerization Activity--N0000184155",
			"Increased Coagulation Factor VII Concentration--N0000184156",
			"Increased Coagulation Factor IX Concentration--N0000184157",
			"Increased Coagulation Factor X Concentration--N0000184158",
			"Increased Fibrinogen Concentration--N0000184159",
			"Increased Fibrin Concentration--N0000184160",
			"Increased Prothrombin Concentration--N0000184161",
			"Cell-mediated Immunity--N0000184306",
			"Increased IgG Production--N0000185001",
			"Humoral Immunity--N0000185002",
			"Decreased Coagulation Activity--N0000175978",
			"Increased Hemagglutinin Inhibition Antibody Production--N0000186107"
	};
	
	String [] THERAPEUTICCATEGORY = {
			"Therapeutic Categories--N0000178293",
			"Antidote--N0000178294",
			"Anti-infective Agent--N0000178295",
			"Antibacterial Agent--N0000178296",
			"Antifungal Agent--N0000178297",
			"Antihelminthic Agent--N0000178298",
			"Antimalarial Agent--N0000178299",
			"Antimicrobial Agent--N0000178300",
			"Antimycobacterial Agent--N0000178301",
			"Antiparasitic Agent--N0000178302",
			"Antiprotozoal Agent--N0000178303",
			"Antiviral Agent--N0000178304",
			"Antiseptic Agent--N0000178305",
			"Anti-inflammatory Agent--N0000178306",
			"Antineoplastic Agent--N0000178307",
			"Antirheumatic Agent--N0000178308",
			"Cardiovascular Agent--N0000178309",
			"Anti-anginal Agent--N0000178310",
			"Anti-arrhythmic Agent--N0000178311",
			"Central Nervous System Agent--N0000178312",
			"Anti-epileptic Agent--N0000178313",
			"Antidepressive Agent--N0000178314",
			"Antimanic Agent--N0000178315",
			"Antipsychotic Agent--N0000178316",
			"Diagnostic Agent--N0000178317",
			"Respiratory System Agent--N0000178318",
			"Antitussive Agent--N0000178319",
			"Gastrointestinal Agent--N0000178370",
			"Anti-emetic Agent--N0000178371",
			"Antidiarrheal Agent--N0000178373",

	};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
        	nickname="Doctor002";
			SharedPreferences settings = getSharedPreferences(
					"jadeChatPrefsFile", 0);
			String host = settings.getString("defaultHost", "");
			String port = settings.getString("defaultPort", "");
			startDoctor(nickname, host, port, agentStartupCallback);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Unexpected exception creating chat agent!");
		}
        
        patientField = (AutoCompleteTextView) findViewById(R.id.editPatient);
		adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, PATIENTS);
		patientField.setAdapter(adapter1);
        patientField.setThreshold(1);
		
        
       diagnosisField = (AutoCompleteTextView) findViewById(R.id.editDiagnosis);
       adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, DIAGNOSIS);
	   diagnosisField.setAdapter(adapter2);
       diagnosisField.setThreshold(1);
       
       
       pDrugField = (AutoCompleteTextView) findViewById(R.id.editPDrug);
       adapter3 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, DRUGS);
		pDrugField.setAdapter(adapter3);
       pDrugField.setThreshold(1);
       
       
       mechanismField = (AutoCompleteTextView) findViewById(R.id.editMechanism);
       adapter4 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, MECHANISMACTION);
       mechanismField.setAdapter(adapter4);
       mechanismField.setThreshold(1);
		
       pharmacoField = (AutoCompleteTextView) findViewById(R.id.editPharmaco);
       adapter5 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, PHARMACOKINETICS);
       pharmacoField.setAdapter(adapter5);
       pharmacoField.setThreshold(1);
       
       
       physiologicField = (AutoCompleteTextView) findViewById(R.id.editPhysiologic);
       adapter6 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, PHYSIOLOGICEFFECT);
       physiologicField.setAdapter(adapter6);
       physiologicField.setThreshold(1);
       
       
       therapeuticField = (AutoCompleteTextView) findViewById(R.id.editTherapeutic);
       adapter7 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, THERAPEUTICCATEGORY);
       therapeuticField.setAdapter(adapter7);
       therapeuticField.setThreshold(1);
       
              
        
        Button button = (Button) findViewById(R.id.btnSubmit);
		button.setOnClickListener(medSearchListener);
		
    }
    
    private OnClickListener medSearchListener = new OnClickListener(){
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String patientID = patientField.getText().toString();
			String patientName = patientID.split("--")[0];
			patientID = patientID.split("--")[1];
			
			String diagnosisID = diagnosisField.getText().toString();
			String diagnosisName = diagnosisID.split("--")[0];
			diagnosisID = diagnosisID.split("--")[1];
			
			String pDrug = pDrugField.getText().toString();
			String pDrugName = pDrug.split("--")[0];
			pDrug =  pDrug.split("--")[1];
			
					
			String mechanismCode = mechanismField.getText().toString();
			if(TextUtils.isEmpty(mechanismCode.trim())){
				mechanismCode="empty";
			}else{
				String mechanismName = mechanismCode.split("--")[0];
				mechanismCode = mechanismCode.split("--")[1];
			}
			
			String pharmacoCode = pharmacoField.getText().toString();
			if(TextUtils.isEmpty(pharmacoCode.trim())){
				pharmacoCode="empty";
			}else{
				String pharmacoName = pharmacoCode.split("--")[0];
				pharmacoCode = pharmacoCode.split("--")[1];
			}
			
			
			String physiologicCode = physiologicField.getText().toString();
			if(TextUtils.isEmpty(physiologicCode.trim())){
				physiologicCode="empty";
			}else{
				String physiologicName = physiologicCode.split("--")[0];
				physiologicCode =physiologicCode.split("--")[1];
			}
			
			
			String therapeuticCode = therapeuticField.getText().toString();
			if(TextUtils.isEmpty(therapeuticCode.trim())){
				therapeuticCode="empty";
			}else{
				String therapeuticName = therapeuticCode.split("--")[0];
				therapeuticCode = therapeuticCode.split("--")[1];
			}
			
			
			//===================END OPTIONS======================
			
			TextView myTV = (TextView) findViewById(R.id.textView3);
			myTV.setText("Please wait to receive alternatives...");
			
			Intent showResults = new Intent(MainActivity.this,
					ResultActivity.class);
			showResults.putExtra("PatientID", patientID);
			showResults.putExtra("PatientName", patientName);
			showResults.putExtra("DiagnosisID",diagnosisID);
			showResults.putExtra("DiagnosisName",diagnosisName);
			showResults.putExtra("nickname", nickname);
			showResults.putExtra("pDrug", pDrug);
			showResults.putExtra("pDrugName", pDrugName);
			//================CHOICE==========================
			//showResults.putExtra("radioChoice", radioChoice);
			//===============OPTIONS===========================
			showResults.putExtra("MechanismCode", mechanismCode);
			showResults.putExtra("PharmacoCode", pharmacoCode);
			showResults.putExtra("PhysiologicCode", physiologicCode);
			showResults.putExtra("TherapeuticCode", therapeuticCode);
			
			MainActivity.this.startActivityForResult(showResults, 0);
			
		
			
			
		}
    	
    	
    };
    
    
	private RuntimeCallback<AgentController> agentStartupCallback = new RuntimeCallback<AgentController>() {
		@Override
		public void onSuccess(AgentController agent) {
		}

		@Override
		public void onFailure(Throwable throwable) {
			logger.log(Level.INFO, "Nickname already in use!");
			
		}
	};
    
    
	
	
	public void startDoctor(final String nickname, final String host,
			final String port,
			final RuntimeCallback<AgentController> agentStartupCallback) {

		final Properties profile = new Properties();
		//profile.setProperty(Profile.MAIN_HOST, host);
		profile.setProperty(Profile.MAIN_HOST, "10.0.2.2");
		//profile.setProperty(Profile.MAIN_PORT, port);
		profile.setProperty(Profile.MAIN_PORT, "1099");
		profile.setProperty(Profile.MAIN, Boolean.FALSE.toString());
		profile.setProperty(Profile.JVM, Profile.ANDROID);

		if (AndroidHelper.isEmulator()) {
			// Emulator: this is needed to work with emulated devices
			profile.setProperty(Profile.LOCAL_HOST, AndroidHelper.LOOPBACK);
		} else {
			profile.setProperty(Profile.LOCAL_HOST,
					AndroidHelper.getLocalIPAddress());
		}
		// Emulator: this is not really needed on a real device
		profile.setProperty(Profile.LOCAL_PORT, "2000");

		if (microRuntimeServiceBinder == null) {
			serviceConnection = new ServiceConnection() {
				public void onServiceConnected(ComponentName className,
						IBinder service) {
					microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
					logger.log(Level.INFO, "Gateway successfully bound to MicroRuntimeService");
					startContainer(nickname, profile,agentStartupCallback);
				};

				public void onServiceDisconnected(ComponentName className) {
					microRuntimeServiceBinder = null;
					logger.log(Level.INFO, "Gateway unbound from MicroRuntimeService");
				}
			};
			logger.log(Level.INFO, "Binding Gateway to MicroRuntimeService...");
			bindService(new Intent(getApplicationContext(),
					MicroRuntimeService.class), serviceConnection,
					Context.BIND_AUTO_CREATE);
		} else {
			logger.log(Level.INFO, "RumtimeGateway already binded to service");
			startContainer(nickname, profile,agentStartupCallback);
		}
	}
	
	
	
	private void startContainerw(String nickname, Properties profile) {
	       try{
	            //Get a hold of Jade-Runtime
	            Runtime rt = Runtime.instance();
				//Exit JVM when there are no more containers around
	            rt.setCloseVM(true);
	            //Create a default profile
	            Profile p = new ProfileImpl(false);
	            p.setParameter(Profile.MAIN_HOST,"10.0.2.2");
	            p.setParameter(Profile.MAIN_PORT, "1099");
	            p.setParameter(Profile.MAIN, Boolean.FALSE.toString());
	            p.setParameter(Profile.JVM, Profile.ANDROID);
	            
	            
	            
	            
	            //Create a new non-main container
	            jade.wrapper.AgentContainer ac = rt.createAgentContainer(p);
	            //Create a new agent
	            alphaAgent = ac.createNewAgent(nickname,DoctorClientAgent.class.getName(),null);
	            //start the agent
	            alphaAgent.start();
	        }catch(Exception ex){
	            ex.printStackTrace();
	        }
	
	}
	
	
	
	private void startContainer(final String nickname, Properties profile,
			final RuntimeCallback<AgentController> agentStartupCallback) {
		if (!MicroRuntime.isRunning()) {
			microRuntimeServiceBinder.startAgentContainer(profile,
					new RuntimeCallback<Void>() {
						@Override
						public void onSuccess(Void thisIsNull) {
							logger.log(Level.INFO, "Successfully start of the container...");
							startAgent(nickname, agentStartupCallback);
						}

						@Override
						public void onFailure(Throwable throwable) {
							logger.log(Level.SEVERE, "Failed to start the container...");
						}
					});
		} else {
			startAgent(nickname, agentStartupCallback);
		}
	}	
	
	
	
	private void startAgent(final String nickname,
			final RuntimeCallback<AgentController> agentStartupCallback) {
		microRuntimeServiceBinder.startAgent(nickname,
				DoctorClientAgent.class.getName(),
				new Object[] { getApplicationContext() },
				new RuntimeCallback<Void>() {
					@Override
					public void onSuccess(Void thisIsNull) {
						logger.log(Level.INFO, "Successfully start of the "
								+ DoctorClientAgent.class.getName() + "...");
						try {
							agentStartupCallback.onSuccess(MicroRuntime
									.getAgent(nickname));
						} catch (ControllerException e) {
							// Should never happen
							agentStartupCallback.onFailure(e);
						}
					}

					@Override
					public void onFailure(Throwable throwable) {
						logger.log(Level.SEVERE, "Failed to start the "
								+ DoctorClientAgent.class.getName() + "...");
						agentStartupCallback.onFailure(throwable);
					}
				});
	}
	
	
	private void killAlphaAgent(){
        if(alphaAgent != null){
            try{
                alphaAgent.kill();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }//Endif
      }//End killAlphaAgent
	
	
	
    
    
    
    
    
}
