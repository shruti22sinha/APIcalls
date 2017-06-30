package peach.main.serviceCallers;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import com.google.gson.JsonObject;

import peach.main.utils.JSONParser;

/**
 * This class invokes EtherCIS APIs
 * @author Shruti Sinha
 *
 */
public class EtherCISInvoker {

	private static String sessionCode;
	private static final EtherCISInvoker _instance = initialize();
	private String baseURL;
	private String username;
	private String password;


	/**
	 * The static method reads config.properties file and initialises the common properties for invoking EtherCIS like base URL, user name and password 
	 * @return 
	 */
	private static EtherCISInvoker initialize(){
		
		EtherCISInvoker newInstance = new EtherCISInvoker();
		try {
			Properties properties = new Properties();	
			FileReader reader = new FileReader("config.properties");
			properties.load(reader);
			newInstance.baseURL =  properties.getProperty("EtherCIS-baseURL");
			newInstance.username = properties.getProperty("EtherCIS-username");
			newInstance.password = properties.getProperty("EtherCIS-password");
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return newInstance;
	}

	/**
	 * This method clears the session 
	 */
	public static void clearSession(){
		sessionCode = null;
	}

	/**
	 * This method sets the session code for EtherCIS call. If the session code is null, it create a new session code, else returns the existing session code. 
	 * @throws Exception
	 */
	private static void setSessionCode() throws Exception{

		if(sessionCode != null)
			return;

		URL url = new URL(_instance.baseURL + "/rest/v1/session?username="+_instance.username+"&password="+_instance.password);

		//URL url = new URL("http://192.168.99.100:8888/rest/v1/session?username=guest&password=guest");
		HttpURLConnection hurl = (HttpURLConnection) url.openConnection();
		hurl.setRequestMethod("POST");
		hurl.setDoOutput(true);                   

		BufferedReader in = new BufferedReader(new InputStreamReader(hurl.getInputStream(), "UTF-8"));
		String line;
		String data = "";
		while((line = in.readLine())!=null)
			data += line;

		JsonObject jsonObject = JSONParser.parse(data);
		sessionCode = jsonObject.getAsJsonPrimitive("sessionId").getAsString();

	}

	/**
	 * This method calls EtherCIS API to fetch EHR status with subject id and name space   
	 * @param String: subjectID
	 * @param String: namespace
	 * @return String : json String (EHR status from EtherCIS) 
	 * @throws Exception
	 */
	public static String getEhrStatusFromSubjectId(String subjectID, String namespace) throws Exception{

		if(subjectID == null || namespace == null){
			throw new Exception("Subject Id  is null");
		}

		setSessionCode();
		URL url = new URL(_instance.baseURL + "/rest/v1/ehr/?subjectId="+ subjectID +"&subjectNamespace=uk.nhs.nhs_number");
		HttpURLConnection hurl = (HttpURLConnection) url.openConnection();
		hurl.setRequestMethod("GET");
		hurl.setDoOutput(true);
		hurl.setRequestProperty("Ehr-Session", sessionCode);
		hurl.setRequestProperty("Content-Type", "application/xml");
		BufferedReader in = new BufferedReader(new InputStreamReader(hurl.getInputStream(), "UTF-8"));
		String data = "";
		String line;
		while((line = in.readLine())!=null)
			data += line;
		return data;
	}

	/**
	 * This method calls EtherCIS API to fetch subject Id of the patient linked to EHR id 
	 * @param String: ehrID
	 * @return String : json String ( with subject Id and namespace from EtherCIS) 
	 * @throws Exception
	 */
	public static String getSubjectIDFromEHRId(String ehrID) throws Exception{
		
		if(ehrID == null ){
			throw new Exception("Ehr ID is null");
		}
		
		setSessionCode();
		URL url = new URL(_instance.baseURL + "/rest/v1/query?sql=SELECT%20p.*%20FROM%20ehr.party_identified%20AS%20p%20INNER%20JOIN%20ehr.status%20AS%20s%20%20ON%20p.id%20%3D%20s.party%20WHERE%20s.ehr_id%20%3D%20'"
				+ ehrID + "'");
		
		HttpURLConnection hurl = (HttpURLConnection) url.openConnection();
		hurl.setRequestMethod("GET");
		hurl.setRequestProperty("Ehr-Session", sessionCode);
		hurl.setRequestProperty("Content-Type", "application/json");      

		BufferedReader in = new BufferedReader(new InputStreamReader(hurl.getInputStream(), "UTF-8"));
		String data = "";
		String line;
		while((line = in.readLine())!=null)
			data += line;
		return data;
	}

}
