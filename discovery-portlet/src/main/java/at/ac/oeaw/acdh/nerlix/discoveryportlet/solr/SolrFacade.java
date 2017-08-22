package at.ac.oeaw.acdh.nerlix.discoveryportlet.solr;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;

/**
 * The facade object to the solr database. In the non JEE context the singleton object is created while calling getInstance() for 
 * the first time. In the JEE context the class should be subtyped in a managed bean with application scope to have the opportunity 
 * to access the application context for parameters
 * 
 * @author Wolfgang Walter SAUER (wowasa)
 *
 */
public class SolrFacade{
	private static SolrFacade _solraccess;
	
	private SolrClient solr; 
	
	protected SolrFacade(String urlString){
		this.solr = new HttpSolrClient.Builder(urlString).build();
	}
	
	public static SolrFacade getInstance(String urlString){
		if(_solraccess == null)
			_solraccess = new SolrFacade(urlString);		
		
		return _solraccess;
	}
	
	public static SolrFacade getNewInstance(String urlString){
		_solraccess = new SolrFacade(urlString);		
		
		return _solraccess;
	}	
	public static SolrFacade getInstance(){

		
		return _solraccess;
	}
	
	public SolrClient getClient(){
		return this.solr;
	}
	
	public List<FacetField> getFacets(String... fields) throws SolrServerException, IOException{
		SolrQuery query = new SolrQuery().setQuery("*:*").setFacet(true).addFacetField(fields);
		return this.solr.query(query).getFacetFields();
	}

}
