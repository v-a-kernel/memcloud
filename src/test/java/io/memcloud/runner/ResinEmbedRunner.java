package io.memcloud.runner;

import com.caucho.resin.HttpEmbed;
import com.caucho.resin.ResinEmbed;
import com.caucho.resin.WebAppEmbed;

public class ResinEmbedRunner {

	
	public static void main(String[] args) throws Exception {
		// http://swiftlet.net/archives/723
		
		//configuration settings80
		int webAppPort = Integer.parseInt(System.getProperty("http.runner.port", "8080"));
		
		//相对地址：WebRoot
		String webAppRootPath = System.getProperty("http.runner.root", "src/main/webapp");
		

		//ResinEmbed web server for development phase
		final ResinEmbed resin = new ResinEmbed();
		HttpEmbed http = new HttpEmbed(webAppPort);
		resin.addPort(http);
		WebAppEmbed webApp = new WebAppEmbed("/", webAppRootPath);
		resin.addWebApp(webApp);

		resin.start();
		resin.join();
	}

}
