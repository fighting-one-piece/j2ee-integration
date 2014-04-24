package org.platform.utils.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.platform.utils.json.JSONUtils;

public class HttpTest {
	
	@Test
	public void testGet() {
		String url = "http://search.51job.com/jobsearch/search_result.php?fromJs=1&jobarea=010000%2C00&funtype=0000&industrytype=00&keyword=java%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B%E5%B8%88&keywordtype=2&lang=c&stype=1&postchannel=0000&fromType=23";
		String html = HttpClientUtils.get(url, HttpUtils.ENCODE_GB2312);
		System.out.println(Jsoup.parse(html));
	}
	
	@Test
	public void test() {
		String url = "http://www.youku.com/";
		String html = HttpClientUtils.get(url, null, HttpUtils.ENCODE_UTF8);
		Document document = Jsoup.parse(html);
		Elements elements = document.select("div a");
		for (Element element : elements) {
			String elementText = element.text();
			if (null != elementText && !"".equals(elementText)) {
				System.out.println(element.text() + ":" + element.attr("href"));
			}
		}
	}
	
	@Test
	public void test1() throws Exception {
		String url = "http://bbs.tianya.cn/";
		Document document = Jsoup.connect(url).get();
		Elements divElements = document.select("div.title");
		System.out.println("------div start------");
		List<String> titles = new ArrayList<String>();
		for (Element element : divElements) {
			Elements aElements = element.select("a");
			for (Element aElement : aElements) {
				String text = aElement.text();
				if (null != text) {
					System.out.println(text);
					titles.add(text);
				}
			}
		}
		System.out.println("titles: " + JSONUtils.object2json(titles));
		System.out.println("------div end------");
	}
	
	@Test
	public void test2() {
		String url = "http://www.youku.com/";
		try {
			iter(url, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void iter(String url, int level) throws IOException {
		System.out.println("------" + level + "------");
		Document document = Jsoup.connect(url).get();
		Elements elements = document.select("div a");
		for (Element element : elements) {
			String elementText = element.text();
			if (null != elementText && !"".equals(elementText)) {
				System.out.println(element.text() + ":" + element.attr("href"));
				iter(element.attr("href"), level + 1);
			}
		}
	}
	
	@Test
	public void fetch() {
		String url = "http://www.bjztb.gov.cn/zbjg/";
//		System.out.println(HttpCommonsUtils.get(url, null, HttpUtils.URL_PARAM_DECODECHARSET_GBK));
//		System.out.println(HttpClientUtils.get(url, null, HttpUtils.URL_PARAM_DECODECHARSET_GBK));
		String html = HttpUnitUtils.get(url);
		System.out.println("------html------");
		System.out.println(html);
		Document document = Jsoup.parse(html);
		Elements aElements = document.select("a.blue");
		System.out.println("------a------");
		for (Element element : aElements) {
			String elementText = element.text();
			if (null != elementText && !"".equals(elementText)) {
				System.out.println(element.text() + ":" + element.attr("href"));
			}
		}
		Elements pageElements = document.select("table.table-mid tr td");
		System.out.println("------page------");
		for (Element element : pageElements) {
			String elementText = element.text();
			if (null != elementText && !"".equals(elementText)) {
				System.out.println(element.text());
			}
		}
		Elements elements = document.select("table[height=25] tr td");
		System.out.println("------elements------");
		for (Element element : elements) {
			String elementText = element.text();
			if (null != elementText && !"".equals(elementText)) {
				System.out.println(element.text());
			}
		}
	}

	public static void main(String[] args) throws HttpException, IOException {
		HttpClient client = new HttpClient();   
        //设置代理服务器地址和端口     
		//client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
        //使用GET方法，如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
        HttpMethod method = new GetMethod("http://www.1ting.com"); 
        //使用POST方法
        //HttpMethod method = new PostMethod("http://java.sun.com";); 
        client.executeMethod(method);
        //打印服务器返回的状态
        System.out.println(method.getStatusLine());
        //打印返回的信息
        System.out.println(method.getResponseBodyAsString());
        //释放连接
        method.releaseConnection();
	}
}
