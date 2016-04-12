<?php
//发送文本消息格式

// <xml>
//  <ToUserName><![CDATA[toUser]]></ToUserName>
//  <FromUserName><![CDATA[fromUser]]></FromUserName> 
//  <CreateTime>1348831860</CreateTime>
//  <MsgType><![CDATA[text]]></MsgType>
//  <Content><![CDATA[this is a test]]></Content>
//  <MsgId>1234567890123456</MsgId>
//  </xml>

class News{

	private $ToUser; 
	private $FromUser;
	private $CreateTime;
	private $MsgType;
	private $Content;
	private $MsgId;
	
	/**
	 * 初始化参数
	 */
	public function getParam(){
		$str = $_POST[];
	}
	
	/**
	 * 回复hello
	 */

	public function replyHello(){

	}


	public function replyJob(){

	}
}

?>