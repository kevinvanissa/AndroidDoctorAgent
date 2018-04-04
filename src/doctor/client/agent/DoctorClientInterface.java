package doctor.client.agent;

public interface DoctorClientInterface {
	public void setPatientDiagnosis(String patientID, String diagnosisCode,String radioChoice, String mechanismActionCode, String pharmacoKineticsCode,
			String physiologicCode, String therapeuticCode);
	public String getAlternatives();
	public String getFirstWord();
}
