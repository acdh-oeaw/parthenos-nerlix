package org.gcube.data.analysis.algorithms.stanbolwrapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import org.gcube.contentmanagement.lexicalmatcher.utils.AnalysisLogger;
import org.gcube.dataanalysis.ecoengine.configuration.AlgorithmConfiguration;
import org.gcube.dataanalysis.ecoengine.datatypes.PrimitiveType;
import org.gcube.dataanalysis.ecoengine.datatypes.StatisticalType;
import org.gcube.dataanalysis.ecoengine.datatypes.enumtypes.PrimitiveTypes;
import org.gcube.dataanalysis.ecoengine.interfaces.StandardLocalExternalAlgorithm;


public class StanbolWrapper extends StandardLocalExternalAlgorithm {

  /** FIELDS **/	
	
	/* Input parameters */

	private String inputName = "input";
	private String acceptType;
	private String contentType;
	private String stanbolRestUrl;
	
	/* output parameters */
	
	private String outputName = "output";
	private File outputJsonFile;

  /** METHODS **/		
	
	@Override
	public void init() throws Exception {
		AnalysisLogger.setLogger(config.getConfigPath() + AlgorithmConfiguration.defaultLoggerFile);
		AnalysisLogger.getLogger().debug("Process initialized");
	}

	@Override
	public String getDescription() {
		return "This process sends an input text file to the Apache Stanbol REST API and returns a semantically enriched output text file in json-ld format";
	}

	@Override
	protected void setInputParameters() {		
		inputs.add(new PrimitiveType(File.class.getName(), null, PrimitiveTypes.FILE, inputName, "File containing the text to be enriched", "input_text") );
	}

	@Override
	protected void process() throws Exception {
		
		// This properties acquisition part has to be changed once a service endpoint has been created
		// (the three properties will be read from specific fields inside the resource definition)
		acceptType = "application/json";
		contentType = "text/plain";
		stanbolRestUrl = "http://enrich.acdh.oeaw.ac.at/enhancer";
		
		// This is the part in which the stanbol service is called and the response is saved into a file
		// (the file will be available at the end of the computation inside a specific folder in the workspace)
	    HttpURLConnection connection = null;
		URL url = new URL(stanbolRestUrl);
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Accept", acceptType);
	    connection.setRequestProperty("Content-Type", contentType);
	    connection.setUseCaches(false);
	    connection.setDoOutput(true);
	    File inputText = new File(getInputParameter(inputName));
		BufferedReader reader = new BufferedReader(new FileReader(inputText));
		String text = "";
	    String line;
		while ((line = reader.readLine()) != null)  
			text += line;
	    reader.close();
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.writeBytes(text);
	    outputStream.flush();
	    outputStream.close();
	    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuffer response = new StringBuffer();
	    while ((line = reader.readLine()) != null) {
			response.append(line);
			response.append("\n");
	    }
	    reader.close();
	    connection.disconnect();
	    String result = response.toString();
	    outputJsonFile = new File (outputName + ".json");
		FileUtils.writeStringToFile(outputJsonFile, result);
	    
	}
	
	@Override
	public StatisticalType getOutput() {		
		return new PrimitiveType(File.class.getName(), outputJsonFile, PrimitiveTypes.FILE, outputName, "Enriched strings");
	}

	@Override
	public void shutdown() {
		AnalysisLogger.getLogger().debug("Process terminated");		
	}
   
}