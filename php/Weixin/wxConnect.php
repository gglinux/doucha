<?php
//define your token
define("TOKEN", "gglinux163com");
$wechatObj = new wechatCallbackapiTest();

if (isset($_GET['echostr'])) {
    $wechatObj->valid();   
}
else{
    $wechatObj->responseMsg();
}
class wechatCallbackapiTest
{
	public function valid()
    {
        $echoStr = $_GET["echostr"];

        //valid signature , option
        if($this->checkSignature()){
        	echo $echoStr;
        	exit;
        }
    }

    public function responseMsg()
    {
		//get post data, May be due to the different environments
		$postStr = $GLOBALS["HTTP_RAW_POST_DATA"];

      	//extract post data
		if (!empty($postStr)){
                /* libxml_disable_entity_loader is to prevent XML eXternal Entity Injection,
                   the best way is to check the validity of xml by yourself */
                libxml_disable_entity_loader(true);
              	$postObj = simplexml_load_string($postStr, 'SimpleXMLElement', LIBXML_NOCDATA);
                $msgType = $postObj->MsgType;
                switch ($msgType) {
                    case 'event':
                        $this->replyEvent($postObj);
                        break;
                    case 'text':
                        $this->replyText($postObj);
                        break;
                    default:
                        $this->replyOther($postObj);
                        break;
                }

        }else {
        	echo "";
        	exit;
        }
    }
		
	private function checkSignature()
	{
        // you must define TOKEN by yourself
        if (!defined("TOKEN")) {
            throw new Exception('TOKEN is not defined!');
        }
        
        $signature = $_GET["signature"];
        $timestamp = $_GET["timestamp"];
        $nonce = $_GET["nonce"];
        		
		$token = TOKEN;
		$tmpArr = array($token, $timestamp, $nonce);
        // use SORT_STRING rule
		sort($tmpArr, SORT_STRING);
		$tmpStr = implode( $tmpArr );
		$tmpStr = sha1( $tmpStr );
		
		if( $tmpStr == $signature ){
			return true;
		}else{
			return false;
		}
	}

    private function replyEvent($postObj){

        if ($postObj->Event == "subscribe") {
            $contentStr = "关注逗叉网，查询校招信息.迎娶白富美，走向人生巅峰.回复'job',获取逗叉网链接.";
        }
        else{
            $contentStr = "";
        }
        $this->sendMsg($postObj, $contentStr);
   
    }
    
    private function replyText($postObj){
        
        $keyword = trim($postObj->Content);
        if($keyword =="job"){
            $contentStr = "http://www.gglinux.com/";
        }
        else{
            $contentStr = "请前往【http://www.gglinux.com/】,查询校招信息！";
        }
        $this->sendMsg($postObj, $contentStr);
    }

    private function replyOther($postObj){
        $contentStr = "请前往【http://www.gglinux.com/】,查询校招信息！";
        $this->sendMsg($postObj,$contentStr);
    }

    private function sendMsg($postObj, $contentStr, $msgType="text"){
        $textTpl = "<xml>
                    <ToUserName><![CDATA[%s]]></ToUserName>
                    <FromUserName><![CDATA[%s]]></FromUserName>
                    <CreateTime>%s</CreateTime>
                    <MsgType><![CDATA[%s]]></MsgType>
                    <Content><![CDATA[%s]]></Content>
                    <FuncFlag>0</FuncFlag>
                    </xml>";
        $resultStr = sprintf($textTpl, $postObj->FromUserName, $postObj->ToUserName, time(), $msgType, $contentStr);
        echo $resultStr;           
    }
}

?>