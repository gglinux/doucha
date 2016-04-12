package liuzixuan.job.spider;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

public class Zhongnan implements PageProcessor {

	//http://jobsky.csu.edu.cn/home/ViewArticle.aspx?ArticleID=29854
	//http://jobsky.csu.edu.cn/home/ViewArticle.aspx?ArticleID=26000
	//http://jobsky\\.csu\\.edu\\.cn/home/ViewArticle\\.aspx\\?ArticleID=\\d+;
	public static final String URL_BASE = "http://jobsky.csu.edu.cn/home/ViewArticle.aspx?ArticleID=";
    public static final String URL_DETAIL = "http://scc\\.hnu\\.edu\\.cn/articledetail\\?t\\.PostId=\\d+";

    private Site site = Site
    		.me()
    		.setDomain("jobsky.csu.edu.cn")
    		.setSleepTime(1000)
    		.setRetryTimes(1000)
    		.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    public void process(Page page) {
    	List<String> requests =new ArrayList<String>();
    	for(int i = 26000; i < 29855; i++){
    		String url = URL_BASE+String.valueOf(i);
    		requests.add(url);
    	}
    	page.addTargetRequests(requests);
		//学校名称，单位名称，薪水，单位地址，学历要求，实习生/全职,招聘数目，招聘时间，面试地址，职位要求，详情，简历地址，原链接
    	page.putField("school", "中南大学");
    	page.putField("title", page.getHtml().xpath("//*[@id='ContentPlaceHolder1_lbltitle']/text()").toString());
        page.putField("name", "");
        page.putField("salary", "");
        page.putField("position", "");
        page.putField("education", "");
        page.putField("property", "");
        page.putField("num", "");
        page.putField("time", page.getHtml().xpath("//*[@id='ContentPlaceHolder1_lbltime']/text()").toString());
        page.putField("interview", page.getHtml().xpath("//*[@id='articleContent']/p[5]/b/span/text()").toString());
        page.putField("require", page.getHtml().xpath("//*[@id='articleContent']/tidyText()").toString());
        page.putField("resume", "");
        page.putField("url", page.getUrl().toString());
        if (page.getResultItems().get("title")==null||page.getResultItems().get("title").toString()=="抱歉, 找不到相应的文章"||page.getResultItems().get("title").toString()==""){
            //skip this page
            page.setSkip(true);
        }
    }
    
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new Zhongnan()).addUrl("http://jobsky.csu.edu.cn/home/ViewArticle.aspx?ArticleID=29720")
//        .addPipeline(new FilePipeline("E:\\spider\\"))
        .addPipeline(new MysqlPipeline())
//        .setScheduler(new FileCacheQueueScheduler("E:\\spiderurl\\huda\\"))
        .thread(10).run();
    }
}