package liuzixuan.job.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;


/**
 * 
 * @author liuzixuan
 * url是js自动生成的，无法直接通过访问到。通过伪造url得到详细页面的链接
 *
 */
public class Keda implements PageProcessor {
	//	http://stu.hnust.edu.cn/jy/jiuyeIndex.do?method=toCategoryZPH&byzd=TYPE01&pageNo=1
	//	http://stu\\.hnust\\.edu\\.cn/jy/jiuyeIndex\\.do\\?method=toCategoryZPH&byzd=TYPE01&pageNo=\\d+
    public static final String URL_LIST = "http://stu\\.hnust\\.edu\\.cn/jy/jiuyeIndex\\.do\\?method=toCategoryZPH&byzd=TYPE01&pageNo=\\d+";
    //http://stu.hnust.edu.cn/jy/jiuyeIndex.do?method=showZphInfo2&id=4bfd8412014c07df178402eb
    //http://stu\\.hnust\\.edu\\.cn/jy/jiuyeIndex\\.do\\?method=showZphInfo2&id=\\w+
    public static final String URL_DETAIL = "http://stu\\.hnust\\.edu\\.cn/jy/jiuyeIndex\\.do\\?method=showZphInfo2&id=\\w+";
    //http://stu.hnust.edu.cn/jy/javaScript:show_jqzph('4a04cbca014abd02ed9a5dc1','')
    //http://stu\.hnust\.edu\.cn/jy/javaScript:show_jqzph\S+
    public static final String URL_DETAIL_BASE = "\\w{24}";
    
    public static final String URL_LIST_BASE="http://stu.hnust.edu.cn/jy/jiuyeIndex.do?method=toCategoryZPH&byzd=TYPE01&pageNo=";

    private Site site = Site
    		.me()
    		.setDomain("stu.hnust.edu.cn")
    		.setSleepTime(1000)
    		.setRetryTimes(1000)
    		.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    public void process(Page page) {
    	
    	//列表页按照规律加入到队列中，通过找规律使用for循环伪造
    	List<String> requests =new ArrayList<String>();
    	for(int i = 0; i < 267; i++){
    		String url = URL_LIST_BASE+String.valueOf(i);
    		requests.add(url);
    	}
    	page.addTargetRequests(requests);
    	//匹配列表页的，将页面中全部href=javascript,js代码加入到队列中
    	if (page.getUrl().regex(URL_LIST).match()) {		
    		//列表页	
            List<String> list = page.getHtml().xpath("html/body/div/div[4]/div[1]/div/div/div[2]/ul/li//a").all();
            Pattern pattern = Pattern.compile("\\w{24}"); 

            //通过正则匹配相应的a标签，在处理产生url
            for(int i = 0;i<list.size();i++){
            	Matcher matcher = pattern.matcher(list.get(i));
            	if(matcher.find()) {   
            		 page.addTargetRequest("http://stu.hnust.edu.cn/jy/jiuyeIndex.do?method=showZphInfo2&id="+matcher.group(0));
            		}  
//            	System.out.println("http://stu.hnust.edu.cn/jy/jiuyeIndex.do?method=showZphInfo2&id="+matcher.group(1));
            }
        }
  
    	//得到详细页，将其分析得到结果
    	if (page.getUrl().regex(URL_DETAIL).match()){   
    		//学校名称，单位名称，薪水，单位地址，学历要求，实习生/全职,招聘数目，招聘时间，面试地址，职位要求，详情，简历地址，原链接
        	page.putField("school", "湖南科技大学");
        	page.putField("title", page.getHtml().xpath("/html/body/div/div[4]/div/div/div[2]/table[1]/tbody/tr[1]/td/text()").toString());
            page.putField("name", "");
            page.putField("salary", "");
            page.putField("position", "");
            page.putField("education", "");
            page.putField("property", "");
            page.putField("num", "");
            page.putField("time", page.getHtml().xpath("/html/body/div/div[4]/div/div/div[2]/table[1]/tbody/tr[2]/td/text(2)").toString());
            page.putField("interview",  page.getHtml().xpath("/html/body/div/div[4]/div/div/div[2]/table[2]/tbody/tr/td/table/tbody/tr/td[1]/text()").toString());
            page.putField("require", page.getHtml().xpath("/html/body/div/div[4]/div/div/div[2]/table[4]/tidyText()").toString());
            page.putField("resume", "");
            page.putField("url", page.getUrl().toString());
            
            if (page.getResultItems().get("title")==null||page.getResultItems().get("title")==""){
                //skip this page
                page.setSkip(true);
            }
        }
        if (page.getResultItems().get("title")==null||page.getResultItems().get("title")==""){
            //skip this page
            page.setSkip(true);
        }
    }
    
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new Keda()).addUrl("http://stu.hnust.edu.cn/jy/jiuyeIndex.do?method=toCategoryZPH&byzd=TYPE01&pageNo=1")
        .addPipeline(new FilePipeline("E:\\spider\\"))
//        .addPipeline(new MysqlPipeline())
//        .setScheduler(new FileCacheQueueScheduler("E:\\spiderurl\\huda\\"))
        .thread(8).run();
    }
}