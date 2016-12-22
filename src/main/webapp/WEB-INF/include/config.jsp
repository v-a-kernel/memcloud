<%
	boolean devMode = true; 
	
	String jsRootVersion = "201101131828"; 
	String jsRoot = "http://tv.sohu.com/comm/plus/" + jsRootVersion + "/";
	
	String cssRootVersion = "201101131832";
	String cssRoot = "http://tv.sohu.com/comm/plus/" + cssRootVersion + "/";
	
	String imageRoot = "http://tv.sohu.com/comm/plus/" + cssRootVersion + "/image/";	
	
	
	if(devMode) {
		jsRoot = "/resources/js/" ;
		cssRoot = "/resources/css/";
		imageRoot = "/resources/image/";		
	}
%>
