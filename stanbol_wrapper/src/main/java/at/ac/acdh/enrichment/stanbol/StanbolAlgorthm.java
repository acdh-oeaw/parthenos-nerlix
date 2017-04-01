package at.ac.acdh.enrichment.stanbol;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.gcube.contentmanagement.lexicalmatcher.utils.AnalysisLogger;
import org.gcube.dataanalysis.ecoengine.configuration.AlgorithmConfiguration;
import org.gcube.dataanalysis.ecoengine.datatypes.PrimitiveType;
import org.gcube.dataanalysis.ecoengine.datatypes.PrimitiveTypesList;
import org.gcube.dataanalysis.ecoengine.datatypes.StatisticalType;
import org.gcube.dataanalysis.ecoengine.datatypes.enumtypes.PrimitiveTypes;
import org.gcube.dataanalysis.ecoengine.interfaces.StandardLocalExternalAlgorithm;

public class StanbolAlgorthm extends StandardLocalExternalAlgorithm{
	
	private String paramName = "input";
	
	/* should read from configuration */
	private String acceptType;
	private String contentType;
	private String stanbolRestUrl;
	
	private String result;
	

	@Override
	public String getDescription() {
		return "Accepts list of strings and tries to enrich each of them using Apache Stanbol API";
	}

	@Override
	public void init() throws Exception {
		// Taken from HelloWorldAlgorithm
		AnalysisLogger.setLogger(config.getConfigPath() + AlgorithmConfiguration.defaultLoggerFile);
		AnalysisLogger.getLogger().debug("StanbolAlgorthm process initialized");
		
		Properties props = new Properties();
		try{
			props.load(StanbolAlgorthm.class.getResourceAsStream("/stanbol_algorithm.cfg.properties"));
			
			acceptType = props.getProperty("acceptType");
			contentType = props.getProperty("contentType");
			stanbolRestUrl = props.getProperty("stanbolRestUrl");
		}catch(IOException io){
			//throw new Exception("Unable to locate stanbol_algorithm.cfg.properties", io);
			
			//defaults as backup
			acceptType = "application/json";
			contentType = "text/plain";
			stanbolRestUrl = "http://enrich.acdh.oeaw.ac.at/enhancer";
		}
	}

	@Override
	protected void process() throws Exception {
		String input = getInputParameter(paramName);
		AnalysisLogger.getLogger().debug("Processing input: " + input);
		
		Client client = ClientBuilder.newClient();
		WebTarget stanbol = client.target(stanbolRestUrl);		
		
		Response response = stanbol.request(acceptType).post(Entity.entity(input, contentType));
		
//		ClientConfig config = new DefaultClientConfig();
//		Client client = Client.create(config);
//		WebResource stanbol = client.resource(stanbolRestUrl);
//		ClientResponse response = stanbol.accept(acceptType).type(contentType).post(ClientResponse.class, input);
		
		result = response.readEntity(String.class);
		
		AnalysisLogger.getLogger().debug("Result: " + result);
	}
	
	@Override
	public StatisticalType getOutput() {		
		return new PrimitiveType(String.class.getName(), result, PrimitiveTypes.STRING, "result", "enriched result");
	}

	@Override
	protected void setInputParameters() {		
		inputs.add(new PrimitiveTypesList(String.class.getName(), PrimitiveTypes.STRING, paramName, "text to be enriched", false) );
		
	}

	@Override
	public void shutdown() {
		AnalysisLogger.getLogger().debug("StanbolAlgorthm process terminated");		
	}
	

}
