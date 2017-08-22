/**
 * 
 */
package at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;


import org.apache.solr.common.SolrDocument;
import org.primefaces.model.LazyDataModel;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;


import at.ac.oeaw.acdh.nerlix.discoveryportlet.solr.*;

/**
 * @author WolfgangWalter SAUER (wowasa) <wolfgang.sauer@oeaw.ac.at>
 *
 */
@ManagedBean
@ApplicationScoped
public class SolrDataSource extends AbstractDataSource<FacetField.Count,SolrDocument> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SolrFacade solr;
	
	
	private List<org.apache.solr.client.solrj.response.FacetField> ffList;
	
	public SolrDataSource(){
		this.solr = SolrFacade.getInstance("https://beta-parthenos.d4science.org/solr/CIDOC_index_public_shard1_replica1");
		
		try {
			ffList = solr.getFacets("language", "datasourcenameforbrowsing", "type");
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getCollections()
	 */
	@Override
	public List<FacetField.Count> getCollections() {
		return this.ffList != null? this.ffList.get(1).getValues():null;
	}

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getLanguages()
	 */
	@Override
	public List<FacetField.Count> getLanguages() {
		return this.ffList != null? this.ffList.get(0).getValues():null;
	}

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getRessourceTypes()
	 */
	@Override
	public List<FacetField.Count> getRessourceTypes() {
		return this.ffList != null? this.ffList.get(2).getValues():null;
	}
	

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getNationalProjects()
	 */
	@Override
	public List<FacetField.Count> getNationalProjects() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see at.ac.oeaw.acdh.nerlix.discoveryportlet.datasource.AbstractDataSource#getResults(java.lang.Object)
	 */
	@Override
	public  LazyDataModel<SolrDocument> getResults(Map<String, Object> filter) {
		return new SolrLazyDataModel (this.solr, filter);
	}
}
