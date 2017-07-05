package peach.main.serviceCallers;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;


/**
 * This class invokes OpenEMPI APIs
 * @author Shruti Sinha
 *
 */
public class OpenEMPIInvoker {
	
	private static String sessionCode; 
	private static final OpenEMPIInvoker _instance = initialize();
	private String baseURL;
	private String username;
	private String password;
	
	/**
	 * The static method reads config.properties file and initialises the common properties for invoking OpenEMPI 
	 * like base URL, user name and password 
	 * @return
	 */
	private static OpenEMPIInvoker initialize(){
		OpenEMPIInvoker newInstance = new OpenEMPIInvoker();
		
		try {
			Properties properties = new Properties();		
			FileReader reader = new FileReader("config.properties");
			properties.load(reader);
			newInstance.baseURL = properties.getProperty("OpenEMPI-baseURL");
			newInstance.username = properties.getProperty("OpenEMPI-username");
			newInstance.password = properties.getProperty("OpenEMPI-password");
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return newInstance;
	}
	
	/**
	 * This method calls OpenEMPI API to fetch patient details with subject id and name space   
	 * @param String: subjectId
	 * @param String: namespace
	 * @return String: xml format
	 * @throws Exception
	 */
	public static String getPersonById(String subjectId, String namespace) throws Exception{
		
		if(subjectId == null && namespace ==null ){
			throw new Exception("Subject Id or namespace is null");
		}
		
		getSessionCode();
		URL url = new URL(_instance.baseURL + "/openempi-admin/openempi-ws-rest/person-query-resource/findPersonById");
		HttpURLConnection hurl =(HttpURLConnection) url.openConnection();
		hurl.setRequestMethod("POST");
		hurl.setDoOutput(true); 
		hurl.setRequestProperty("OPENEMPI_SESSION_KEY", sessionCode);
		hurl.setRequestProperty("Content-Type", "application/xml"); 
	    hurl.setRequestProperty("Accept", "application/xml");
	    
	    String payload = null;
	    if(namespace.equalsIgnoreCase("NHS") || namespace.equals("uk.nhs.nhs_number")){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>2.16.840.1.113883.4.1</namespaceIdentifier>" //TODO : To be modified accordingly
		    		+ "<universalIdentifier>2.16.840.1.113883.4.1</universalIdentifier>" //TODO : To be modified accordingly
		    		+ "<universalIdentifierTypeCode>SSN</universalIdentifierTypeCode>" //TODO : To be modified accordingly
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    }
	    	    else if(namespace.equals("SSN")){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>2.16.840.1.113883.4.1</namespaceIdentifier>"
		    		+ "<universalIdentifier>2.16.840.1.113883.4.1</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>SSN</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    }
	    else if (namespace == "VirginiaDLN"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>2.16.840.1.113883.4.3.51</namespaceIdentifier>"
		    		+ "<universalIdentifier>2.16.840.1.113883.4.3.51</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>VirginiaDLN</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    }
	    else if (namespace == "OpenMRS"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>35a02490-5c20-11de-8ae4-001d60400e9e</namespaceIdentifier>"
		    		+ "<universalIdentifier>35a02490-5c20-11de-8ae4-001d60400e9e</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>OpenMRS</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    	
	    }
	    else if (namespace == "IHENA"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>IHENA</namespaceIdentifier>"
		    		+ "<universalIdentifier>1.3.6.1.4.1.21367.2010.1.2.300</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>ISO</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    	
	    }
	    else if (namespace == "IHELOCAL"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>2.16.840.1.113883.4.1</namespaceIdentifier>"
		    		+ "<universalIdentifier>2.16.840.1.113883.4.1</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>SSN</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    }
	    else if (namespace == "XREF2005"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>XREF2005</namespaceIdentifier>"
		    		+ "<universalIdentifier>1.3.6.1.4.1.21367.2005.1.2</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>ISO</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    }
	    else if (namespace == "HIMSS2005"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>HIMSS2005</namespaceIdentifier>"
		    		+ "<universalIdentifier>1.3.6.1.4.1.21367.2005.1.1</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>ISO</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    	
	    }
	    else if (namespace == "NIST2010"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>NIST2010</namespaceIdentifier>"
		    		+ "<universalIdentifier>2.16.840.1.113883.3.72.5.9.1</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>ISO</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    }
	    else if (namespace == "NIST2010-2"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>NIST2010-2</namespaceIdentifier>"
		    		+ "<universalIdentifier>2.16.840.1.113883.3.72.5.9.2</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>NIST2010-2</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    }
	    else if (namespace == "NIST2010-3"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>2.16.840.1.113883.4.1</namespaceIdentifier>"
		    		+ "<universalIdentifier>2.16.840.1.113883.3.72.5.9.3</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>NIST2010-3</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    }
	    else if (namespace == "ECID"){
	    	payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"  
		    		+ "<personIdentifier>"
		    		+ "<identifier>" + subjectId + "</identifier>"
		    		+ "<identifierDomain>"
		    		+ "<namespaceIdentifier>ECID</namespaceIdentifier>"
		    		+ "<universalIdentifier>ECID</universalIdentifier>"
		    		+ "<universalIdentifierTypeCode>ECID</universalIdentifierTypeCode>"
		    		+ "</identifierDomain>"
		    		+ "</personIdentifier>";
	    }
	    
	    OutputStreamWriter osw = new OutputStreamWriter(hurl.getOutputStream());
        osw.write(payload);
        osw.flush();
        osw.close();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(hurl.getInputStream(), "UTF-8"));  
        String response = "";
        String line;
        while((line = in.readLine())!=null){
        	response += line;	
        }          
        return response;
	}
	
	/**
	 * This method calls OpenEMPI API to details of all the patients. For developers use
	 * @return String: xml format
	 * @throws Exception
	 */
	public static String getPerson() throws Exception{
		
		getSessionCode();
		//TODO: Change maxRecord accordingly in the next line
		URL url = new URL(_instance.baseURL + "/openempi-admin/openempi-ws-rest/person-query-resource/loadAllPersonsPaged?firstRecord=0&maxRecords=100");
		
		HttpURLConnection hurl = (HttpURLConnection) url.openConnection();
        hurl.setRequestMethod("GET");
        hurl.setDoOutput(true);
        hurl.setRequestProperty("Content-Type", "application/xml"); 
	    hurl.setRequestProperty("Accept", "application/xml");
        hurl.setRequestProperty("OPENEMPI_SESSION_KEY", sessionCode);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(hurl.getInputStream(), "UTF-8"));
        String line;
        while((line = in.readLine())!=null)
        	System.out.println(line);
        return line;
	}
	
	/**
	 * This methods call the OpenEMPI API to get the session code
	 * @throws Exception
	 */
	private static void getSessionCode() throws Exception{
		
		URL url = new URL(_instance.baseURL + "/openempi-admin/openempi-ws-rest/security-resource/authenticate");
		
		HttpURLConnection hurl = (HttpURLConnection) url.openConnection();
        hurl.setRequestMethod("PUT");
        hurl.setDoOutput(true);                   
        hurl.setRequestProperty("Content-Type", "application/xml"); //application/json
        hurl.setRequestProperty("Accept", "application/xml");
        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        		+ "<authenticationRequest><password>"+ _instance.password + "</password><username>" + _instance.username + "</username></authenticationRequest>";
        
        OutputStreamWriter osw = new OutputStreamWriter(hurl.getOutputStream());
        osw.write(payload);
        osw.flush();
        osw.close();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(hurl.getInputStream(), "UTF-8"));
        sessionCode = in.readLine();
        
	}

}
