package org.platform.modules.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

public class SolrTest1 {

	private HttpSolrServer server1 = null;
	private HttpSolrServer server2 = null;
	private HttpSolrServer server3 = null;
	
	@Before
	public void init() {
		server1 = new HttpSolrServer("http://centos.master:8080/solr");
		server2 = new HttpSolrServer("http://centos.slave1:8080/solr");
		server3 = new HttpSolrServer("http://centos.slave2:8080/solr");
	}
	
	@Test
	public void testAdd() {
		try {
			SolrInputDocument inDocument = new SolrInputDocument();
			inDocument.addField("id", "1");
			inDocument.addField("title", "我是中国人");
			inDocument.addField("content", "我的祖国,中国共产党");
			inDocument.addField("url", "http://www.tianya.com");
			server1.add(inDocument);
			server1.optimize();
			server1.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test1() {
		try {
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("*:*");
			solrQuery.setStart(0);
			solrQuery.setRows(2000);
			QueryResponse resp = server1.query(solrQuery);
			SolrDocumentList docList = resp.getResults();
			System.out.println("total number: " + docList.getNumFound());
			for (SolrDocument doc : docList) {
				System.out.println("title: " + doc.getFieldValue("title"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		try {
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("id:1");
			solrQuery.setStart(0);
			solrQuery.setRows(20);
			QueryResponse resp = server2.query(solrQuery);
			SolrDocumentList docList = resp.getResults();
			System.out.println("total number: " + docList.getNumFound());
			for (SolrDocument doc : docList) {
				System.out.println("content: " + doc.getFieldValue("content"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3() {
		try {
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery("id:1");
			solrQuery.setStart(0);
			solrQuery.setRows(20);
			QueryResponse resp = server3.query(solrQuery);
			SolrDocumentList docList = resp.getResults();
			System.out.println("total number: " + docList.getNumFound());
			for (SolrDocument doc : docList) {
				System.out.println("content: " + doc.getFieldValue("content"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
}
