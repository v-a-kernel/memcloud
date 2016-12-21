<%@page import="io.downgoon.jresty.rest.model.UnifiedResponseCode"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="io.downgoon.jresty.rest.model.UnifiedResponse,io.downgoon.jresty.rest.model.UnifiedResponseCode" %>

<%
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	if(up == null) {
		up = new UnifiedResponse();
		up.setStatus(UnifiedResponseCode.RC_ERROR);
		up.setMessage("系统运行异常");
		up.setDebug("jsp页面中提取model响应数据失败");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>系统运行情况</title>
<script type="text/javascript" src="http://js.sohu.com/library/jquery1.4.2.js"></script>
<script type="text/javascript" src="/resources/js/index.js"></script>
<script type="text/javascript" src="/resources/js/swfobject.js"></script>
<script type="text/javascript" src="/resources/js/indexFlash.js"></script>
</head>

<body>

<!--曲线flash容器-->
		<div class="flashs" id=playSohuVideo>
			<script type="text/javascript">
				//曲线图
				showIndexFlash(600,300,"/resources/flash/sohuVideo.swf","sohuVideo","NOSHOW=SI|VI&TAB=1周,WW,1|1月,MM,1|1季度,MM,3|全部,all,0","playSohuVideo","http://index.tv.sohu.com/index/tvs-play/1008564.json|D*U|http://index.tv.sohu.com/index/tvs-play/1009768.json");
			</script>
		</div>


	</body>
</html>