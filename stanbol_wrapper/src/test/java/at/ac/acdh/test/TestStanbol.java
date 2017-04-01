package at.ac.acdh.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.gcube.dataanalysis.ecoengine.configuration.AlgorithmConfiguration;
import org.gcube.dataanalysis.ecoengine.datatypes.PrimitiveType;
import org.gcube.dataanalysis.ecoengine.interfaces.ComputationalAgent;
import org.gcube.dataanalysis.ecoengine.processing.factories.TransducerersFactory;
import org.gcube.dataanalysis.ecoengine.test.regression.Regressor;
import org.junit.Test;

import at.ac.acdh.enrichment.stanbol.StanbolAlgorthm;

public class TestStanbol {
	
	@Test
	public void deploymentTest() throws Exception{
		System.out.println("Deployment Test Begin");
		List<ComputationalAgent> kStatistics = TransducerersFactory.getTransducerers(deploymentTestConfiguration());
		kStatistics.get(0).init();
		Regressor.process(kStatistics.get(0));
		kStatistics.get(0).shutdown();
		System.out.println("Deployment Test End");
		
		StanbolAlgorthm sa = (StanbolAlgorthm) kStatistics.get(0);
		PrimitiveType result = (PrimitiveType) sa.getOutput();
		
		System.out.println("Result: \n");
		System.out.println(result.getContent().toString());
	}
	
	
	private static AlgorithmConfiguration deploymentTestConfiguration () {
		AlgorithmConfiguration testConfiguration = Regressor.getConfig();
		testConfiguration.setAgent("STANBOL_ALGORITHM");
		testConfiguration.setParam("input", "The Stanbol enhancer can detect famous cities such as Paris and people such as Bob Marley");
		return testConfiguration;	
	}
	
	
	private static AlgorithmConfiguration readInputFromFile() {
		AlgorithmConfiguration testConfiguration = Regressor.getConfig();
		testConfiguration.setAgent("STANBOL_ALGORITHM");
		
		InputStream is = TestStanbol.class.getResourceAsStream("/input_text.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while((line = br.readLine()) != null){
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
			}
		}		
		
		testConfiguration.setParam("input", sb.toString());
		return testConfiguration;
	}

}
