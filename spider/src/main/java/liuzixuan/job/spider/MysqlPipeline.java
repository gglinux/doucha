package liuzixuan.job.spider;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import liuzixuan.job.sql.Save;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 
 * @author liuzixuan
 *爬取信息转存到MySQL数据库
 *
 */

public class MysqlPipeline implements Pipeline {
	
	//数据存储及整理
	public void process(ResultItems resultItems, Task task) {
		// TODO Auto-generated method stub
		HashMap<String, String> detail = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
        	String value = (String) entry.getValue();
        	if(value=="null"){
        		 detail.put(entry.getKey(), "");
        	}else{
        		  detail.put(entry.getKey(), value);
        	}
           // detail.put(entry.getKey(), (String) entry.getValue());
           // System.out.println("!!!!!"+entry.getKey() + ":\t" + entry.getValue());
        }
        analyze(detail);
        Save save = new Save();
        try {
			save.connect();
			save.insert(detail);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//对爬取得到的数据进行处理，主要是处理“招聘信息发布时间”
	public void analyze(HashMap<String,String> detail){
		String school = detail.get("school");
		if(school.compareTo("湘潭大学")==0&&detail.get("title")!=""){
			String time = detail.get("time");
			time = time.substring(5, 15);
			String stamp = stringToData(time,"yyyy-MM-dd");
			detail.put("stamp", stamp);
			detail.put("time", time);			
			String interview = detail.get("interview");
			int ViewBegin = interview.indexOf("举办地点") + 5;
			int ViewEnd = interview.length();
			interview = interview.substring(ViewBegin, ViewEnd);
			detail.put("interview", interview);
		}
		if(school.compareTo("湖南大学")==0&&detail.get("title")!=""){
			String time = detail.get("time");
			String stamp = stringToData(time,"yyyy-MM-dd");
			detail.put("stamp", stamp);
		}
		if(school.compareTo("湖南师范大学")==0&&detail.get("title")!=""){
			String time = detail.get("time");
			time = time.substring(7, 17);
			String stamp = stringToData(time,"yyyy-MM-dd");
			detail.put("stamp", stamp);
			detail.put("time", time);			
			String interview = detail.get("interview");
			interview = interview.substring(7, interview.length());
			detail.put("interview", interview);
		}
		if(school.compareTo("湖南科技大学")==0&&detail.get("title")!=""){
			String time = detail.get("time");
			time = time.substring(0, 10);
			String stamp = stringToData(time,"yyyy-MM-dd");
			detail.put("stamp", stamp);
			detail.put("time", time);			
		}
		if(school.compareTo("中南大学")==0&&detail.get("title")!=""){
			String time = detail.get("time");
			String stamp = stringToData(time,"yyyy/MM/dd");
			detail.put("stamp", stamp);
			detail.put("time", time);
			
			String interview = detail.get("interview");
			if(interview!=null&&interview.length()>5){
				interview = interview.substring(5, interview.length());
				detail.put("interview", interview);
			}
		}
		
	}
	//将字符串的时间日期转换成 专有的时间日期类
	public String stringToData(String data,String stamp){
		    SimpleDateFormat sdf = new SimpleDateFormat(stamp);  
		    Date date;
		    String re_time = "";
			try {
				date = sdf.parse(data);
				 long l = date.getTime();  
				 String str = String.valueOf(l);  
				 re_time = str.substring(0, 10);  
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return re_time;
	}

}
