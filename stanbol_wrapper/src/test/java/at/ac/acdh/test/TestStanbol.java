package org.gcube.data.analysis.algorithms.stanbolwrapper;

import java.util.List;

import org.gcube.dataanalysis.ecoengine.configuration.AlgorithmConfiguration;
import org.gcube.dataanalysis.ecoengine.interfaces.ComputationalAgent;
import org.gcube.dataanalysis.ecoengine.processing.factories.TransducerersFactory;
import org.gcube.dataanalysis.ecoengine.test.regression.Regressor;
import org.junit.Test;

public class StanbolWrapperTest {
	
	@Test
	public void deploymentTest() throws Exception{
		System.out.println("Deployment Test Begin");
		List<ComputationalAgent> kStatistics = TransducerersFactory.getTransducerers(deploymentTestConfiguration());
		kStatistics.get(0).init();
		Regressor.process(kStatistics.get(0));
		kStatistics.get(0).shutdown();
		System.out.println("Deployment Test End");
	}
	
	private static AlgorithmConfiguration deploymentTestConfiguration () {
		AlgorithmConfiguration testConfiguration = Regressor.getConfig();
		testConfiguration.setAgent("STANBOL_WRAPPER");		
		testConfiguration.setParam("input", "data/input_text.txt");
		return testConfiguration;	
	}
	
}