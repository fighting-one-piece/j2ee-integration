package org.platform.modules.crawl;

import java.util.Iterator;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.platform.utils.regex.RegexUtils;

public class SeleniumUtils {
	
	public static int PAGENUM = 50;
	public static String WEBSITE = "http://www.51job.com/";
	public int pageIndex = 0;
	public int rank = 0;
	public String searchWords = "";
	
	public static void main(String[] args) {
		SeleniumUtils SeleniumUtils = new SeleniumUtils();
		SeleniumUtils.getPageHtml(WEBSITE,"数据挖掘");
	}
	
	public String getPageHtml(String url,String keyWords){
		searchWords = keyWords;
		String html = "";
		
		// 如果你的 FireFox 没有安装在默认目录，那么必须在程序中设置
//    	System.setProperty("webdriver.firefox.bin", "C:\\Program Files\\Mozilla Firefox\\firefox.exe");
    	System.setProperty("webdriver.ie.driver", "D:\\develop\\java\\selenium\\IEDriverServer.exe");
    	// 创建一个 FireFox 的浏览器实例
//        WebDriver driver = new FirefoxDriver();
        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        WebDriver driver = new InternetExplorerDriver(ieCapabilities);

        // 让浏览器访问 Baidu
        driver.get(url);

        // 选择地区
        WebElement jobArea = driver.findElement(By.name("btnJobarea"));
        jobArea.click();
        WebElement area = driver.findElement(By.xpath("/html/body/div[9]/table/tbody/tr[3]/td/table/tbody/tr[2]/td[2]"));
        area.click();
        
        // 输入关键字
        WebElement jobName = driver.findElement(By.name("keyword"));
        jobName.clear();
        jobName.sendKeys(keyWords);
        
        // 搜索
        driver.findElement(By.xpath("//*[@id='frmKeywordSearch']/div[3]/div[3]/input")).click();

        String currentWindow = driver.getWindowHandle();//获取当前窗口句柄
        Set<String> handles = driver.getWindowHandles();//获取所有窗口句柄
        Iterator<String> it = handles.iterator();
        while (it.hasNext()) {
	        if (currentWindow == it.next()){
	        	continue;
	        }
	        WebDriver window = driver.switchTo().window(it.next());//切换到新窗口
	        String windowHandle = window.getWindowHandle();
	        for(int i=1; i<=PAGENUM; i++){
	        	pageIndex = i;
	        	html = window.getPageSource();
		        getUrls(html,1,driver.switchTo().window(currentWindow));
		        driver.switchTo().window(windowHandle);
		        WebElement nextPage = window.findElement(By.xpath("/html/body/div[2]/div[5]/div[3]/div[3]/table/tbody/tr/td[2]/table/tbody/tr/td[8]/a"));
		        nextPage.click();
	        }
        }
        
		return html;
	}
	
	private void getUrls(String html ,int pageIndex,WebDriver driver){
		Element doc = Jsoup.parse(html);
		Elements hrefs = doc.select("#resultList a.jobname");
		rank = 0;
		for(int i=0; i<hrefs.size(); i++){
			rank ++;
			Element a = hrefs.get(i);
			System.out.println(a.attr("abs:href"));
			getConents(a.attr("abs:href"),driver);
		}
	}
	
	private void getConents(String url,WebDriver driver){
		driver.get(url);
		String jobName = getValue("/html/body/div[2]/div/div[2]/table[1]/tbody/tr[1]/td",driver);
		String company = getValue("/html/body/div[2]/div/div[2]/table[1]/tbody/tr[2]/td/table/tbody/tr/td[1]/a[1]",driver);
		
		String date = getValue("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/table/tbody/tr[1]/td[2]",driver);
		String place = getValue("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/table/tbody/tr[1]/td[4]",driver);
		
		String experience = getValue("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/table/tbody/tr[2]/td[2]",driver);
		String salary = getValue("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/table/tbody/tr[2]/td[6]",driver);
		
		String zhineng = getValue("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/table/tbody/tr[4]",driver);
		String describe = getValue("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/table/tbody/tr[5]",driver);
		describe += getValue("/html/body/div[2]/div/div[2]/div[1]/div[2]/div/table/tbody/tr[6]",driver);
		
		String companyAbstact = getValue("/html/body/div[2]/div/div[2]/div[3]/div[2]/div",driver);
		String email = getEmail(companyAbstact);
		email = email + "|" + getEmail(describe);
		String website = getWebsite(companyAbstact);
		
		System.out.println("职位名称：\t" + jobName);
		System.out.println("公司名称：\t" + company);
		System.out.println("日期：\t" + date);
		System.out.println("地点：\t" + place);
		System.out.println("经验：\t" + experience);
		System.out.println("薪水：\t" + salary);
		System.out.println("职称：\t" + zhineng);
		System.out.println("职能描述：\t" + describe);
		System.out.println("邮箱：\t" + email);
		System.out.println("网站：\t" + website);
		
//		Job job =  new Job();
//		job.setUrl(url);
//		job.setPage(pageIndex);
//		job.setRank(rank);
//		job.setKeyWord(searchWords);
//		job.setCompany(company);
//		job.setJobName(jobName);
//		job.setDate(date);
//		job.setWorkPlace(place);
//		job.setExperience(experience);
//		job.setSalary(salary);
//		job.setZhineng(zhineng);
//		job.setDescribe(describe);
//		job.setEmail(email);
//		job.setWebsite(website);
//		job.save();
//		System.out.println(job.getUrl() + "已保存完毕.....");
		System.out.println("----------------------------------------------------------------------");
	}
	
	private String getEmail(String str){
		String email = "";
		email = RegexUtils.getString(str, "[\\w[\\.-]]+@[\\w]+\\.com");
		return email;
	}
	
	private String getWebsite(String str){
		String website = "";
		website = RegexUtils.getString(str, "http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
		return website;
	}
	private String getValue(String rule,WebDriver driver){
		String value = "";
		try{
			value = driver.findElement(By.xpath(rule)).getText();
		}catch(Exception e){
			
		}
		return value;
	}
}
