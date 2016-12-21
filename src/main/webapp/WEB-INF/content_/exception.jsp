<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="io.downgoon.jresty.rest.model.UnifiedResponse,io.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@ include file="/WEB-INF/include/config.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	if(up == null) {
		up = new UnifiedResponse();
		up.setStatus(UnifiedResponseCode.RC_ERROR);
		up.setMessage("系统运行异常");
		up.setDebug("jsp页面中提取model响应数据失败");
	} else {
		up.setMessage("JSP异常："+ up.getMessage());
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="description" content="搜狐高清电影频道" />
<meta name="keywords" content="高清电影频道" />
<meta name="robots" content="all" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>搜狐视频-搜狐</title>
<link type="text/css" rel="stylesheet" href="http://tv.sohu.com/upload/tv110421/main_boke.css" />
<link type="text/css" rel="stylesheet" href="http://tv.sohu.com/upload/tv110421/play2.css" />
</head>
<body>
<!--BEGIN:指数-关键词终端-->
<div id="contentA" class="area">
	<div class="left">
		<div class="gray651">
			<div class="gray-hd">
				<div class="tit">&nbsp;</div>
			</div>
			<div class="gray-bd">	
				<div class="error-tips">
					<p><span><%=up.getMessage()%></span></p>
					<p></p>
					<p></p>
					<a href="http://index.tv.sohu.com" title="" class="btn_6">返回指数首页</a>
				</div>
			</div>
			<div class="gray-ft"></div>
		</div>
	</div>
	<div class="right" style="display: none;">	
		<div class="graybox">
			<div class="gray-hd cf"><a href="/" title="" class="lk">指数查询</a><a href="/" title="" class="lkbt on">对比查询</a></div>
			<div class="gray-bd">
				<div class="querybox">
					<ul class="query-hd cf">
						<li class="on"><a href="/" title="">对比影视剧</a></li>
						<li><a href="/" title="">对比搜索词</a></li>
						<li><a href="/" title="">对比明星</a></li>
					</ul>
					<div class="query-bd">
						<div class="l1">
							<input type="text" value="请输入文字..."><a class="btn_s" title="" href="/">添加</a>	
						</div>
						<div class="l2 cf">
							<p><a href="/" title="" class="nms">非常勿扰</a><a href="/" title="" class="cls">关闭</a><a href="/" title="" class="nms">魔法魔法魔法师</a><a href="/" title="" class="cls">关闭</a><a href="/" title="" class="nms">魔法师魔法师</a><a href="/" title="" class="cls">关闭</a></p>
							<a href="/" title="" class="widegray">开始对比</a>
							<div class="y-tips" style="display:block;">您最多只能同时对比三项</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="graybox notop" style="display: none;">
			<div class="gray-hd cf"><h4>按人群查询</h4></div>
			<div class="gray-bd">
				<div class="seles cf">
					<form id="" name="" method="post" action="">
						<label>
							<select name="select" id="select"><option>性别</option></select>
						</label>
						<label>
							<select name="select" id="select"><option>年龄</option></select>
						</label>
						<label>
							<select name="select" id="select"><option>学历</option></select>
						</label>
						<label>
							<select name="select" id="select"><option>职业</option></select>
						</label>
						<label>
							<select name="select" id="select"><option>收入</option><option>1000-3000</option><option>3000-5000</option><option>5000-8000</option></select>
						</label>
					</form>
					<div class="sublink"><a href="/" title="" class="widegray">开始查询</a></div>
				</div>				
			</div>
		</div>
	</div>
</div>
<!--END:指数-关键词终端-->
<div id="footArea">页尾</div>

<!--Attachment for debug: 异常栈信息-->
<br /><br /><br />
<div id="debug_exception_stack" style="display: none;">
	<s:debug></s:debug>
</div>

</body>
</html>