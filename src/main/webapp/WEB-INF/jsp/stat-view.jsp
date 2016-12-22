<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="io.downgoon.jresty.rest.model.UnifiedResponse,io.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="io.memcloud.utils.MD5,io.memcloud.stats.MemStatSummary" %>
<%@page import="io.memcloud.utils.StatDateType,io.memcloud.utils.MyUtils" %>
<%
UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
Map<String,Object> attachment = (Map<String,Object>)up.getAttachment();
MemStatSummary summary = (MemStatSummary)attachment.get("summary");

String curDateTxt = (String)attachment.get("curDate"); 
String preDateTxt = (String)attachment.get("preDate");
String ip = (String)attachment.get("ip");
Integer port = (Integer)attachment.get("port");
String ipport = ip+":"+port;
String ipportUTF8 = MD5.urlEncode(ipport);

String[] commands = new String[] {"get","set","hit","mis"};
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%=ipport%></title>

<script type="text/javascript" src="/resources/amcharts/flash/swfobject.js"></script>
<script type="text/javascript" src="/resources/amcharts/js/amcharts.js"></script>
<script type="text/javascript" src="/resources/amcharts/js/amfallback.js"></script>
<script type="text/javascript" src="/resources/amcharts/js/raphael.js"></script>

<style>
#table_info {width:600px; text-align:center;border:1px solid #ddd; border-width:0 0 1px 1px;}
#table_info th {background:#ececec;}
#table_info th,#table_info td {height:22px; border:1px solid #ddd; border-width:1px 1px 0 0;}
</style>

</head>

<body style="background-color:#EEEEEE">
	<div id="main-content">
	
	<% if ( summary!= null) { %>
	<div id="summary">
		<div class="base" style="float:left;">
			<table id="table_info" border="0" cellspacing="0" cellpadding="0">
				<tbody>
    				<tr><th>Memcached Server Info</th><th><%=ipport%></th></tr>
    				<tr><td>Start Time</td><td><%=MemStatSummary.prettySecond(summary.getStartTimeSecond(), "yyyy/MM/dd HH:mm:ss")%></td></tr>
    				<tr><td>Uptime</td><td><%=MemStatSummary.humanSecond(summary.getUptimeSecond())%></td></tr>
    				<tr><td>Memcached Version</td><td><%=summary.getVersion() %></td></tr>
    				<tr><td>Current Connections</td><td><%=summary.getCurrConns() %></td></tr>
    				<tr><td>Total Connections Since Started</td><td><%=summary.getTotalConns() %></td></tr>
    				<tr><td>Bytes Read</td><td><%=MemStatSummary.humanBytes(summary.getReadBytes()) %></td></tr>
    				<tr><td>Bytes Written</td><td><%=MemStatSummary.humanBytes(summary.getWriteBytes()) %></td></tr>
    				<tr><td>I/O (Read+Written) Rate</td><td><%=MemStatSummary.humanBytes(summary.getIORate()) %> per sec</td></tr>
				</tbody>
			</table>		
		</div>
		<div class="hits" style="float:left;">
			<table id="table_info" border="0" cellspacing="0" cellpadding="0">
				<tbody>
    				<tr><th>Cache Info & Percentage</th><th>Used: <%=MyUtils.percentFormat(summary.getUsedPercentage()) %>    Hits: <%=MyUtils.percentFormat(summary.getHitPercentage()) %></th></tr>
    				<tr><td>Current Items(total)</td><td><%=MyUtils.numberFormat(summary.getCurrItems())%> (<%=MyUtils.numberFormat(summary.getTotalItems())%>)</td></tr>
    				<tr><td>Used Cache Size</td><td><%=MemStatSummary.humanBytes(summary.getUsedBytes()) %></td></tr>
    				<tr><td>Free Cache Size</td><td><%=MemStatSummary.humanBytes(summary.getFreeBytes()) %></td></tr>
    				<tr><td>Total Cache Size</td><td><%=MemStatSummary.humanBytes(summary.getTotalBytes()) %></td></tr>
    				<tr><td>Hits (Rate)</td><td><%=MyUtils.numberFormat(summary.getHitCount())%> (<%=MyUtils.numberFormat(summary.getHitRate())%> req/sec)</td></tr>
    				<tr><td>Misses (Rate)</td><td><%=MyUtils.numberFormat(summary.getMisCount()) %> (<%=MyUtils.numberFormat(summary.getMisRate())%> req/sec)</td></tr>
    				<tr><td>Get[Hits+Misses] (Rate)</td><td><%=MyUtils.numberFormat(summary.getGetCount()) %> (<%=MyUtils.numberFormat(summary.getGetRate())%> req/sec)</td></tr>
    				<tr><td>Set (Rate)</td><td><%=MyUtils.numberFormat(summary.getSetCount()) %> (<%=MyUtils.numberFormat(summary.getSetRate()) %> req/sec)</td></tr>
				</tbody>
			</table>		
		</div>
	</div>
	
	<p /><p /><br /><br />
		<%for(String cmd : commands) { %>
		<div style="float:left;">
			<h2><a id="chartTitle_<%=cmd %>" target="_black"></a></h2>
			<div id="chartFlash_<%=cmd %>" style="width:600px; height:400px; background-color:#FFFFFF"></div>
		</div>		
		<% } %>
		
		
		<script type="text/javascript">
			var params = {
		        bgcolor:"#FFFFFF"
		    };	
			
			<%for(String cmd : commands) { %>
			document.getElementById("chartTitle_<%=cmd %>").setAttribute("href","/memcloud/stat-<%=cmd %>.html?triple=<%=ip %>_<%=port %>_<%=curDateTxt %>");
			document.getElementById("chartTitle_<%=cmd %>").innerHTML = "<%=cmd %>_<%=ip %>_<%=port %>_<%=curDateTxt %>.csv";
			
			var flashVars_<%=cmd %> = {
			     path: "/resources/amcharts/flash/",
				 settings_file: "/resources/settings_stat_mem.xml",
				 data_file: "/memcloud/stat-<%=cmd %>.html?triple=<%=ip %>_<%=port %>_<%=curDateTxt %>"
			};
			

			// change 8 to 80 to test javascript version
		    if (swfobject.hasFlashPlayerVersion("8")){
			    swfobject.embedSWF("/resources/amcharts/flash/amline.swf", "chartFlash_<%=cmd %>", "1200", "400", "8.0.0", "/resources/amcharts/flash/expressInstall.swf", flashVars_<%=cmd %>, params);
			}
			else{//js not supported by IE
				var amFallback_1 = new AmCharts.AmFallback();
				amFallback_1.settingsFile = flashVars_<%=cmd %>.settings_file;			
			    amFallback_1.dataFile = flashVars_<%=cmd %>.data_file;
				amFallback_1.pathToImages = "/resources/amcharts/images/";
				amFallback_1.type = "line";
				amFallback_1.write("chartFlash_<%=cmd %>");
			}
		    <% } %>
			
		</script>
		
		<% }  else { %>
		<div class="base" style="float:left;">
			<table id="table_info" border="0" cellspacing="0" cellpadding="0">
				<tbody>
    				<tr><th>Memcached Server Info</th><th><%=ipport%></th></tr>
    				<tr><td>错误提示：</td><td>监控信息尚未采集</td></tr>
    				
				</tbody>
			</table>		
		</div>
		<% } %>
</div><!-- End of main-content  -->
	</body>
</html>
