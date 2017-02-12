package org.platform.utils.http;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HttpUnitUtils extends HttpUtils {
	
	public static String get(String url) {
		String result = "";
		WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setTimeout(35000);
		webClient.getOptions().setDoNotTrackEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        try {
			HtmlPage page = webClient.getPage(url);
			result = page.asXml();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String get(String url, String dest) {
		String result = "";
		WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setTimeout(35000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        try {
			HtmlPage page = webClient.getPage(url);
			result = page.asXml();
			page.save(new File(dest, "utf-8"));
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		String html = get("http://www.goubanjia.com/");
		Document document = Jsoup.parse(html);
		Elements ipElements = document.select("td.ip");
		for (int i = 0, len = ipElements.size(); i < len; i++) {
			Element ipElement = ipElements.get(i);
			System.out.println(ipElement);
		}
		Elements portElements = document.select("span.port");
		for (int i = 0, len = portElements.size(); i < len; i++) {
			Element portElement = portElements.get(i);
			System.out.println(portElement);
		}
	}

}
