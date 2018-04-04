package doctor.client.agent;

import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.Location;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.*;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;
import jade.util.leap.ArrayList;
import jade.util.leap.HashMap;
import jade.util.leap.Iterator;
import jade.util.leap.Map;
import jade.util.leap.Set;
import jade.util.leap.SortedSetImpl;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.content.ContentElement;
import java.util.Random;

import jade.domain.mobility.MobileAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;

public class DoctorClientAgent extends Agent implements DoctorClientInterface {
	private static final long serialVersionUID = 1594371294421614291L;
	 private Map locations = new HashMap();
	 private ArrayList al = new ArrayList();
	 private AID [] doctorManagerAgent;
	 private String patientID;
	 private String diagnosisCode;
	 private String drugAlternatives="";
	 private String firstWord="";
	 private String mechanismActionCode="empty";
	 private String pharmacoKineticsCode="empty";
	 private String physiologicCode="empty";
	 private String therapeuticCode="empty";
	 private String radioChoice="empty";
	
	protected void setup() {
		System.out.println("Doctor Agent Started...");
		//Find doctor agent and update agent variable in this class
		DFAgentDescription template=new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("doctorAgent-manager");
		template.addServices(sd);
		
		try{
			DFAgentDescription [] result= DFService.search(this,template);
			System.out.println("Found the following agents ");
			doctorManagerAgent = new AID[result.length];
			
			for (int i=0;i < result.length;i++){
				doctorManagerAgent[i]=result[i].getName();
				System.out.println(doctorManagerAgent[i].getName());
			}//end for

		//Behaviour to send request to Doctor agent on Server
	    //addBehaviour(new RequestPreProcess());

		}catch(FIPAException fe){

			fe.printStackTrace();
		}
		
		
		 // Register language and ontology
		  getContentManager().registerLanguage(new SLCodec());
		  getContentManager().registerOntology(MobilityOntology.getInstance());
		
		try{
		// Get available locations with AMS
	     sendRequest(new Action(getAMS(), new QueryPlatformLocationsAction()));
	     
	     //Receive response from AMS
         MessageTemplate mt = MessageTemplate.and(
			                  MessageTemplate.MatchSender(getAMS()),
			                  MessageTemplate.MatchPerformative(ACLMessage.INFORM));
         ACLMessage resp = blockingReceive(mt);
         ContentElement ce = getContentManager().extractContent(resp);
         Result result = (Result) ce;
         jade.util.leap.Iterator it = result.getItems().iterator();
         while (it.hasNext()) {
            Location loc = (Location)it.next();
            if(loc.getName().toLowerCase().contains("Ward".toLowerCase())){
            locations.put(loc.getName(), loc);
            al.add(loc);
            }
		 }
         AID aid = this.getAID();

         //array of locations
	    Object [] o = al.toArray();
	    int i = o.length;
	    Random r = new Random();
	    int randomValue = r.nextInt(i);
	    Location dest = (Location)o[randomValue];
	    MobileAgentDescription mad = new MobileAgentDescription();
	    mad.setName(aid);
	    mad.setDestination(dest);
	    MoveAction ma = new MoveAction();
	    ma.setMobileAgentDescription(mad);
	    sendRequest(new Action(aid, ma));
	     
		}catch(Exception e){e.printStackTrace();}
		
    // Activate the GUI - THIS IS VERY IMPORTANT
	registerO2AInterface(DoctorClientInterface.class, this);
	     
   }
	 
	public void setPatientDiagnosis(String patientID, String diagnosisCode, String radioChoice, String mechanismActionCode, String pharmacoKineticsCode,
			String physiologicCode, String therapeuticCode){
		this.patientID = patientID;
		this.diagnosisCode = diagnosisCode;
		this.mechanismActionCode = mechanismActionCode;
		this.pharmacoKineticsCode = pharmacoKineticsCode;
		this.physiologicCode  = physiologicCode;
		this.therapeuticCode  = therapeuticCode;
		this.radioChoice = radioChoice;
		//Behaviour to send request to Doctor agent on Server
	    addBehaviour(new RequestPreProcess());
	    System.out.println("Adding RequestPreProcess Behaviour to Agent");

	}
	
	@Override
	public String getAlternatives() {
		// TODO Auto-generated method stub
		return drugAlternatives;
	}
	
	@Override
	public String getFirstWord(){
		return firstWord;
	}
	
	
	
	//Inner Class to get the alternatives
	private class RequestPreProcess extends Behaviour{
		private static final long serialVersionUID = 1594371294421614291L;
		private int step = 0;
		private MessageTemplate mt;
		private int repliesCnt=0;
		private String doctorAgentName;
		
		
		public void action(){
			switch(step){
				case 0:
					
					//Send the request to the doctor agent
					ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
					for (int i=0;i<doctorManagerAgent.length;i++){
						request.addReceiver(doctorManagerAgent[i]);
					}//end for
					request.setContent(patientID+" "+diagnosisCode+" "+radioChoice+" "+mechanismActionCode+" "+pharmacoKineticsCode+" "+physiologicCode+" "+therapeuticCode);
					request.setConversationId("alternatives-data");
					request.setReplyWith("request" + System.currentTimeMillis() );
					myAgent.send(request);
					//prepare template to get message
					mt= MessageTemplate.and(MessageTemplate.MatchConversationId("alternatives-data"),
							MessageTemplate.MatchInReplyTo(request.getReplyWith()));
					step=1;
					break;
				case 1:
					//Receive all alternatives from doctor agent
					ACLMessage reply = myAgent.receive(mt);
					if(reply != null){
						if(reply.getPerformative() == ACLMessage.INFORM){
							String myalternatives = reply.getContent();
							System.out.println("Alternatives Received "+ myalternatives);
							String arr[] = myalternatives.split("!",2);
							firstWord = arr[0];
							drugAlternatives = arr[1];
							doctorAgentName = reply.getSender().getLocalName();
                            System.out.println("From "+ reply.getSender().getName() );
						}//End if
						repliesCnt++;	
						if(repliesCnt >= doctorManagerAgent.length){
							//we received all replies
							step=2;
						}

					}else{
						block();
					}
					break;
				default: 
					System.out.println("How the hell did I reach here?!");
					break;	
			}//End Switch
		}//End Action Method
		
		public boolean done(){
			return step == 2;
		}
		
	}
	
	
	protected void takeDown() {
	}
	
	   void sendRequest(Action action) {
		// ---------------------------------

		      ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		      request.setLanguage(new SLCodec().getName());
		      request.setOntology(MobilityOntology.getInstance().getName());
		      //getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		      //getContentManager().registerOntology(MobilityOntology.getInstance());
		      try {
			     getContentManager().fillContent(request, action);
			     request.addReceiver(action.getActor());
			     send(request);
			  }
			  catch (Exception ex) { ex.printStackTrace(); }
		   }
}
