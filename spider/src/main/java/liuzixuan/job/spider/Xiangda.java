package liuzixuan.job.spider;

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
 * 爬取湖南师范大学的就业网信息
 * 登陆账号：2011701026
 * 登陆密码：2011701026
 *
 */

public class Xiangda implements PageProcessor {
	//http://xtu.good-edu.cn/showmore.php?actiontype=5
	//http://xtu.good-edu.cn/showmore.php?actiontype=5&pg=4
	//http://xtu\\.good-edu\\.cn/showmore\\.php\\?actiontype=5&pg=\\d+
    public static final String URL_LIST = "http://xtu\\.good-edu\\.cn/showmore\\.php\\?actiontype=\\d&pg=\\d+";
    //http://xtu.good-edu.cn/showarticle.php?actiontype=5&id=1696&keyword_type=&search_keyword=
    //http://xtu\\.good-edu\\.cn/showarticle\\.php\\?actiontype=5&id=\\d+&\\*
    public static final String URL_DETAIL = "http://xtu\\.good-edu\\.cn/showarticle\\.php\\?actiontype=5&id=\\d+\\D+";

    private Site site = Site
    		.me()
    		.setDomain("xtu.good-edu.cn")
    		.setSleepTime(1000)
    		.setRetryTimes(1000)
    		.addCookie("PHPSESSID", "662c9745f4cb10f707334dc11a1feaa5")
    		.addCookie("hanma_cookie_indexstyle", "1")
    		.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");

    public void process(Page page) {
    	if (page.getUrl().regex(URL_LIST).match()) {
            page.addTargetRequests(page.getHtml().xpath("//*[@id='content_r_border']").links().regex(URL_DETAIL).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            //文章页
            if (page.getResultItems().get("title")==null){
                //skip this page
                page.setSkip(true);
            }     
//            page.putField("url", page.getUrl().toString());
//            page.putField("content", page.getHtml());
//            page.putField("raw", page.getRawText());
//            page.putField("request", page.getRequest());
        } 
    	else if (page.getUrl().regex(URL_DETAIL).match()){   
    		//学校名称，单位名称，薪水，单位地址，学历要求，实习生/全职,招聘数目，招聘时间，面试地址，职位要求，详情，简历地址，原链接
        	page.putField("school", "湘潭大学");
        	page.putField("title", page.getHtml().xpath("//*[@id='content_border']/div[1]/div[2]/h3/text()").toString());
            page.putField("name", "");
            page.putField("salary", "");
            page.putField("position", page.getHtml().xpath("//*[@id='content_border']/div[1]/div[3]/table[1]/tbody/tr[3]/td[2]/text()").toString());
            page.putField("education", "");
            page.putField("property", "");
            page.putField("num", "");
            page.putField("time", page.getHtml().xpath("//*[@id='content_border']/div[1]/div[3]/div[1]/span/text()").toString());
            page.putField("interview", page.getHtml().xpath("//*[@id='content_border']/div[1]/div[3]/div[1]/span/text()").toString());
            page.putField("require", page.getHtml().xpath("//*[@id='content_border']/div[1]/div[3]/tidyText()").toString());
            page.putField("resume", "");
            page.putField("url", page.getUrl().toString());
//            
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
        Spider.create(new Xiangda()).addUrl("http://xtu.good-edu.cn/showmore.php?actiontype=5&pg=2")
        .addPipeline(new MysqlPipeline())
//        .addPipeline(new FilePipeline("E:\\spider\\"))
        .thread(10).run();
    }
}