package liuzixuan.job.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

public class Huda implements PageProcessor {
	//http://scc.hnu.edu.cn/newsjob!getMore.action?p.currentPage=1&Lb=1
	//爬取规则，正则表达式，列表标题页
    public static final String URL_LIST = "http://scc\\.hnu\\.edu\\.cn/newsjob!getMore\\.action\\?p\\.currentPage=\\d+&Lb=\\d+";
    //http://scc.hnu.edu.cn/articledetail?t.PostId=4504
    //爬取规则，正则，详细的信息页
    public static final String URL_DETAIL = "http://scc\\.hnu\\.edu\\.cn/articledetail\\?t\\.PostId=\\d+";

    private Site site = Site
    		.me()
    		.setDomain("scc.hnu.edu.cn")
    		.setSleepTime(10000)
    		.setRetryTimes(10000)
    		.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    public void process(Page page) {
    	if (page.getUrl().regex(URL_LIST).match()) {
            page.addTargetRequests(page.getHtml().xpath("//*[@id='right']").links().regex(URL_DETAIL).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            //文章页
            if (page.getResultItems().get("title")==null){
                //skip this page
                page.setSkip(true);
            }           
        } 
    	else if (page.getUrl().regex(URL_DETAIL).match()){   
    		//学校名称，单位名称，薪水，单位地址，学历要求，实习生/全职,招聘数目，招聘时间，面试地址，职位要求，详情，简历地址，原链接
        	page.putField("school", "湖南大学");
        	page.putField("title", page.getHtml().xpath("//*[@id='content']/div[2]/div[1]/text()").toString());
            page.putField("name", page.getHtml().xpath("//*[@id='content']/div[2]/div[3]/div/table/tbody/tr[1]/td[2]/font/text()").toString());
            page.putField("salary", "");
            page.putField("position", page.getHtml().xpath("//*[@id='content']/div[2]/div[3]/div/table/tbody/tr[3]/td[2]/font/text()").toString());
            page.putField("education", page.getHtml().xpath("//*[@id='content']/div[2]/div[5]/table[1]/tbody/tr[2]/td[2]/text()").toString());
            page.putField("property", "");
            page.putField("num", page.getHtml().xpath("//*[@id='content']/div[2]/div[5]/table[4]/tbody/tr[4]/td[2]/text()").toString());
            page.putField("time", page.getHtml().xpath("//*[@id='content']/div[2]/div[2]/span[1]/em/text()").toString());
            page.putField("interview", "");
            page.putField("require", page.getHtml().xpath("//*[@id='content']/div[2]/div[5]/tidyText()").toString());
            page.putField("resume", "");
            page.putField("url", page.getUrl().toString());
            
            if (page.getResultItems().get("title")==null){
                //skip this page
                page.setSkip(true);
            }
        }
    }
    
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new Huda()).addUrl("http://scc.hnu.edu.cn/articledetail?t.PostId=8277")
//        .addPipeline(new JsonFilePipeline("E:\\spider\\"))
//        .addPipeline(new MysqlPipeline())
        .setScheduler(new FileCacheQueueScheduler("E:\\spiderurl\\huda\\"))
        .thread(2).run();
    }
}