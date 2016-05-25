package org.platform.modules.elasticsearch;

import java.net.InetSocketAddress;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

public class ESClient {
	
	public void clent1() {
		Client client = TransportClient.builder().build().addTransportAddress(
				new InetSocketTransportAddress(new InetSocketAddress(
						"hostname", 9200)));
		GetResponse response = client.prepareGet("index", "type", "id")
				.execute().actionGet();
		System.out.println(response.getSourceAsString());
		client.close();
	}
	
	public void clent2() throws Exception {
		Settings.Builder settings = Settings.builder().put("client.transport.sniff", true)
				.put("cluster.name", "application-1");
		TransportClient.Builder tClient = TransportClient.builder().settings(settings);
		Client client = tClient.build().addTransportAddress(
				new InetSocketTransportAddress(new InetSocketAddress(
						"centos.master", 9200)));
		IndexResponse response = client.prepareIndex("test", "user", "1")
				.setSource(XContentFactory.jsonBuilder().startObject()
						.field("name", "zhangsan").field("age", 18).endObject())
							.setTTL(8000).execute().actionGet();
		System.out.println(response.getId());
		client.close();
	}
	
	public void clent3() {
		Settings.Builder settings = Settings.builder().put("client.transport.sniff", true)
				.put("cluster.name", "");
		TransportClient.Builder tClient = TransportClient.builder().settings(settings);
		Client client = tClient.build().addTransportAddress(
				new InetSocketTransportAddress(new InetSocketAddress(
						"hostname", 9200)));
		DeleteResponse response = client.prepareDelete("index", "type", "id")
				.execute().actionGet();
		System.out.println(response.getId());
		client.close();
	}
	
	public void clent4() {
		Settings.Builder settings = Settings.builder().put("client.transport.sniff", true)
				.put("cluster.name", "");
		TransportClient.Builder tClient = TransportClient.builder().settings(settings);
		Client client = tClient.build().addTransportAddress(
				new InetSocketTransportAddress(new InetSocketAddress(
						"hostname", 9200)));
		SearchRequestBuilder builder = client.prepareSearch("indices")
				.setTypes("types").setSearchType(SearchType.DEFAULT)
					.setFrom(0).setSize(100);
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(
				new QueryStringQueryBuilder("qs").field("field")).should(
						new QueryStringQueryBuilder("qs").field("field"));
		SearchResponse response = builder.setQuery(queryBuilder).execute().actionGet();
		System.out.println(response.getHits().getTotalHits());
		client.close();
	}

	public static void main(String[] args) {
		
	}
}
