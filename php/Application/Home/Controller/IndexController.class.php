<?php
namespace Home\Controller;
use Think\Controller;
class IndexController extends Controller {
    public function index(){
    	// var_dump($_GET);
    	$select = array(
    		'school'   =>  "",
    		'property' =>  "",
    		'stamp'	   =>  "",
    		'position' =>  "",
    		'education'=>  ""
    		);
    	if (isset($_GET['school'])) {
    		switch (I('get.school')) {
    			case 'keda':		$select['school'] = " and school = '湖南科技大学' ";break;
    			case 'huda':		$select['school'] = " and school = '湖南大学' ";break;
    			case 'xiangda':		$select['school'] = " and school = '湘潭大学' ";break;
    			case 'zhongnan':	$select['school'] = " and school = '中南大学' ";break;
    			case 'hushi':		$select["school"] = " and school = '湖南师范大学' ";break;
    			default:break;
    		}
    	}
    	if (isset($_GET['property'])) {
    		switch (I('get.property')) {
    			case 'shixi': 		$select['property'] = " and title like '%实习%' ";break;
                                        case '?shixi':       $select['property'] = " and title like '%实习%' ";break;
    			default:break;
    		}
    	}
    	if (isset($_GET['time'])) {
    		switch (I('get.time')) {
    			case 'two':		$select['stamp'] = "and stamp > ".(time()-2*24*60*60);break;
    			case 'five':	$select['stamp'] = "and stamp > ".(time()-5*24*60*60);break;
    			case 'seven': 	$select['stamp'] = "and stamp > ".(time()-7*24*60*60);break;
    			default:break;
    		}
    	}
    	if (isset($_GET['position'])) {
    		switch (I('get.position')) {
    			case 'changsha':		$select['school'] ="and school = ' 湖南大学' or school = '中南大学' or school = '湖南师范大学' ";break;
    			case 'xiangtan':		$select['school'] = "and school = '湘潭大学' or school = '湖南科技大学' ";break;
    			default:break;
    		}
    	}

    	if (isset($_GET['education'])) {
    		switch (I('get.education')) {
    			case 'shuoshi':	$select['education']  = "";break;
    			default:break;
    		}
    	}
    	$job = M('job');
    	// $sql = "select id,title,time from job where salary = ''".$select['school'].$select['property'].$select['time'].$select['position'].$select['education'].'order by stamp';
    	$where = "salary = ' ' ".$select['school'].$select['property'].$select['time'].$select['position'].$select['education'];
            // echo $where;
    	$count = $job->where($where)->count();
		$Page       = new \Think\Page($count,25);// 实例化分页类 传入总记录数和每页显示的记录数(25)
		$show       = $Page->show();// 分页显示输出

		// 进行分页数据查询 注意limit方法的参数要使用Page类的属性
		$data = $job->where($where)->order('stamp desc')->limit($Page->firstRow.','.$Page->listRows)->getField('id,title,time');
        $this->assign('data',$data);// 赋值数据集
		$this->assign('page',$show);// 赋值分页输出
		$this->display(); // 输出模板
    }
    
    public function detail(){
        $id = I('get.id');
        $data = M('job')->where('id='.$id)->find();
        // var_dump($data);
        $this->assign('data',$data);
        $this->display();
    }
}