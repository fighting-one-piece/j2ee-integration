package org.platform.modules.crawl.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.platform.modules.crawl.entity.CrawlJob;
import org.platform.modules.lucene.IIndex;
import org.platform.modules.lucene.IndexUtils;
import org.platform.modules.lucene.biz.IIndexBusiness;
import org.platform.modules.lucene.biz.impl.FSIndexBusinessImpl;
import org.platform.utils.http.HttpClientUtils;
import org.platform.utils.http.HttpUtils;
import org.platform.utils.json.JSONUtils;

public class Fetcher {
	
	private static final String url = "http://search.51job.com/jobsearch/search_result.php?fromJs=1&jobarea=010000%2C00&funtype=0000&industrytype=00&keyword=java%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B%E5%B8%88&keywordtype=2&lang=c&stype=1&postchannel=0000&fromType=23";
	
	public String obtainText(Element element) {
		String elementText = element.text();
		if (null != elementText) {
			elementText = elementText.trim();
		}
		return elementText;
	}
	
	@Test
	public void test() {
		String url = "http://search.51job.com/job/49669969,c.html";
		String html = HttpClientUtils.get(url, HttpUtils.ENCODE_GB2312);
		Document document = Jsoup.parse(html);
		String career = document.select("table.jobs_1 tr td.sr_bt").get(0).text();
		System.out.println(career);
		String company = document.select("table.jobs_1 tr table a").get(0).text();
		System.out.println(company);
		Elements tds = document.select("div.grayline table.jobs_1 tr td");
		for (Element td : tds) {
			if (!td.hasAttr("class"))
				continue;
			String tdClass = td.attr("class");
			if ("txt_1".equals(tdClass) || "txt_2".equals(tdClass) ||
					"job_detail".equals(tdClass) || "txt_4 wordBreakNormal job_detail ".equals(tdClass)) {
				System.out.println(td.text());
			}
		}
	}
	
	@Test
	public void test1() {
		String html = HttpClientUtils.get(url, HttpUtils.ENCODE_GB2312);
		Document document = Jsoup.parse(html);
		Map<String, String> map = new HashMap<String, String>();
		Elements trs = document.select("tr");
		for (Element tr : trs) {
			if(!tr.hasAttr("class")) {
				continue;
			}
			CrawlJob job = new CrawlJob();
			String trClass = tr.attr("class");
			if ("tr0".equals(trClass)) {
				Elements tds = tr.select("td");
				for (Element td : tds) {
					if(!td.hasAttr("class")) {
						continue;
					}
					String tdClass = td.attr("class");
					if ("td1".equals(tdClass)) {
						Elements as = td.select("a");
						for (Element a : as) {
							job.setCareer(a.text());
							break;
						}
					} else if ("td2".equals(tdClass)) {
						Elements as = td.select("a");
						for (Element a : as) {
							job.setCompany(a.text());
							break;
						}
					} else if ("td3".equals(tdClass)) {
						Elements spans = td.select("span");
						for (Element span : spans) {
							job.setLocation(span.text());
							break;
						}
					} else if ("td4".equals(tdClass)) {
						Elements spans = td.select("span");
						for (Element span : spans) {
							job.setUpdateTime(span.text());
							break;
						}
					}
					
				}
			} else if ("tr1".equals(trClass)) {
				Elements tds = tr.select("td.td1234");
				for (Element td : tds) {
					job.setRequire(td.text());
					break;
				}
			} else if ("tr2".equals(trClass)) {
				Elements tds = tr.select("td.td1234");
				for (Element td : tds) {
					job.setSummary(td.text());
					break;
				}
			}
			map.put(job.getCareer() + "-" + job.getCompany(), JSONUtils.object2json(job));
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
		}
	}
	
	@Test
	public void test2() {
		String html = HttpClientUtils.get(url, HttpUtils.ENCODE_GB2312);
		Document document = Jsoup.parse(html);
		Elements hrefs = document.select("tr.tr0 td.td1 a");
		for (Element href : hrefs) {
			System.out.println(href.text() + "-" + href.attr("href"));
		}
	}
	
	@Test
	public void test3() {
		String url = "http://www.newsmth.net/nForum/#!board/Career_Upgrade?p=1";
		String html = HttpClientUtils.get(url, HttpUtils.ENCODE_GB2312);
		Document document = Jsoup.parse(html);
		Map<String, String> map = new HashMap<String, String>();
		Elements trs = document.select("tr");
		for (Element tr : trs) {
			CrawlJob job = new CrawlJob();
			Elements tds = tr.select("td");
			for (Element td : tds) {
				if(!td.hasAttr("class")) {
					continue;
				}
				String tdClass = td.attr("class");
				if ("title_9".equals(tdClass)) {
					Elements as = td.select("a");
					for (Element a : as) {
						job.setCareer(a.text());
						break;
					}
				} else if ("title_10".equals(tdClass)) {
					job.setUpdateTime(td.text());
				} 
			}
		map.put(job.getCareer() + "-" + job.getCompany(), JSONUtils.object2json(job));
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
		}
	}
	
	@Test
	public void test4() throws Exception {
		String url = "http://www.newsmth.net/nForum/#!board/Career_Upgrade?p=1";
//		String url = "http://www.newsmth.net/nForum/#!board/Career_Upgrade";
		System.out.println(HttpClientUtils.get(url, HttpUtils.ENCODE_GB2312));
		Map<String, String> params = new HashMap<String, String>();
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		System.out.println(HttpClientUtils.post(url, params, HttpUtils.ENCODE_GB2312));
//		System.out.println(HttpCommonsUtils.get(url, HttpUtils.ENCODE_GB2312));
//		System.out.println(Jsoup.connect(url).get().html());
	}
	
	public static void analyzer() {
		String word = "职位标签:  java 互联网支付 软件工程师职位职能:  高级软件工程师  职位描述: 本公司为大中型第三方支付公司提供基于自有产品的支付核心及相关系统的客户化开发服务，岗位要求如下： 1、本科以上学历，计算机相关专业。 2、两年以上JAVA开发经验。 3、熟悉UNIX操作系统，熟悉ORACLE或DB2数据库。 4、有银行支付系统、第三方支付系统开发经验者优先。 5、具有良好的沟通、团队协作、计划和创新的能力。 6、具备架构设计能力及初级项目管理能力的开发组长优先。 岗位职责： 高级软件工程师： 1、高级专业技术人员职位，独立负责工作小组，给下级成员提供引导或支持并监督他们的日常活动； 2、参与软件产品项目规划工作，制定具体项目实施方案； 3、整合并优化项目开发所需各种资源； 4、负责软件开发技术和规范及标准流程的改进； 5、参与软件系统的设计和分析； 6、根据开发进度和任务分配，完成相应模块软件的设计、开发、编程任务。 软件工程师： 1、技术人员职位，在上级的领导和监督下定期完成量化的工作要求； 2、能独立处理和解决所负责的任务； 3、根据开发进度和任务分配，完成相应模块软件的设计、开发、编程任务； 4、进行程序单元、功能的测试，查出软件存在的缺陷并保证其质量； 5、进行编制项目文档和质量记录的工作； 6、维护软件使之保持可用性和稳定性。";
		Analyzer analyzer = IndexUtils.obtainAnalyzer(IIndex.ANALYZER_MMSEG4J_MAXWORD);
		try {
			TokenStream tokenStream = analyzer.tokenStream("field", new StringReader(word));
			CharTermAttribute charTermAttr = tokenStream.addAttribute(CharTermAttribute.class);
			PositionLengthAttribute positionLengthAttr = tokenStream.addAttribute(PositionLengthAttribute.class);
			PositionIncrementAttribute positionIncAttr = tokenStream.addAttribute(PositionIncrementAttribute.class);
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				System.out.print(charTermAttr.toString());
				System.out.print(positionLengthAttr.getPositionLength() + ":");
				System.out.println(positionIncAttr.getPositionIncrement());
			}
			tokenStream.end();
			tokenStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertIndex() {
		String word = "职位标签:  java 互联网支付 软件工程师职位职能:  高级软件工程师  职位描述: 本公司为大中型第三方支付公司提供基于自有产品的支付核心及相关系统的客户化开发服务，岗位要求如下： 1、本科以上学历，计算机相关专业。 2、两年以上JAVA开发经验。 3、熟悉UNIX操作系统，熟悉ORACLE或DB2数据库。 4、有银行支付系统、第三方支付系统开发经验者优先。 5、具有良好的沟通、团队协作、计划和创新的能力。 6、具备架构设计能力及初级项目管理能力的开发组长优先。 岗位职责： 高级软件工程师： 1、高级专业技术人员职位，独立负责工作小组，给下级成员提供引导或支持并监督他们的日常活动； 2、参与软件产品项目规划工作，制定具体项目实施方案； 3、整合并优化项目开发所需各种资源； 4、负责软件开发技术和规范及标准流程的改进； 5、参与软件系统的设计和分析； 6、根据开发进度和任务分配，完成相应模块软件的设计、开发、编程任务。 软件工程师： 1、技术人员职位，在上级的领导和监督下定期完成量化的工作要求； 2、能独立处理和解决所负责的任务； 3、根据开发进度和任务分配，完成相应模块软件的设计、开发、编程任务； 4、进行程序单元、功能的测试，查出软件存在的缺陷并保证其质量； 5、进行编制项目文档和质量记录的工作； 6、维护软件使之保持可用性和稳定性。";
		IIndexBusiness manager = new FSIndexBusinessImpl();
		manager.insert(new Object[]{new Content(word)});
		manager.commit();
	}
	
	public static void main(String[] args) {
		String url = "http://search.51job.com/jobsearch/search_result.php?fromJs=1&jobarea=010000%2C00&funtype=0000&industrytype=00&keyword=java%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B%E5%B8%88&keywordtype=2&curr_page=#1&lang=c&stype=1&postchannel=0000&fromType=23";
		System.out.println(HttpClientUtils.get(url, HttpUtils.ENCODE_GB2312));
	}
	
	
	private static class Content {
		private String word = null;
		
		public Content(String word) {
			this.word = word;
		}
		
		@SuppressWarnings("unused")
		public String getWord() {
			return word;
		}
		
		@SuppressWarnings("unused")
		public void setWord(String word) {
			this.word = word;
		}
		
	}
}
