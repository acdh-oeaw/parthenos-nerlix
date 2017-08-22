/**
 * 
 */
package at.ac.oeaw.acdh.nerlix.discoveryportlet.solr;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 * @author WolfgangWalter SAUER (wowasa) <wolfgang.sauer@oeaw.ac.at>
 * @param <T>
 *
 */
public class SolrLazyDataModel extends LazyDataModel<SolrDocument> {

	/**
	 * 
	 */
	private SolrFacade solr; 
	private SolrQuery query;

	
	private static final long serialVersionUID = 1L;
	
	public SolrLazyDataModel(SolrFacade solr, Map<String, Object> filter){
		this.solr = solr;
		this.query = new SolrQuery().setQuery("*:*");
		
		
		if(filter != null){

			for(String key : filter.keySet()){
				
				if(filter.get(key) != null){
					if(key.equals("globalFilter") && filter.get(key) != null){
						this.query.addFilterQuery((String)filter.get(key));
					}
					else if(filter.get(key) instanceof String[] && ((String[]) filter.get(key)).length > 0){
						String part = null;
						
						
						
						for(String criterium : (String []) filter.get(key)){
							
							if(part == null)
								part = (key + ":(\"" + criterium);
							else
								part += ("\" OR \"" + criterium);
						}
						this.query.addFilterQuery(part + "\")");
					}
				}
			}
			
		}
	}

	@Override
	public List<SolrDocument> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
		this.query = this.query.setStart(first).setRows(pageSize);
		
		SolrDocumentList list = null; 
		
		try {
			list = this.solr.getClient().query(query).getResults();
			this.setRowCount(list != null?(int) list.getNumFound():0);


		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return list;
	}

	@Override
	public List<SolrDocument> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		return load(first, pageSize, null, filters);
	}

}
