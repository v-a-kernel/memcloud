package io.memcloud.utils;

import java.util.Arrays;
import java.util.StringTokenizer;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class CLIDemo {

	public static void main(String[] args) throws Exception {
		
//		String[] arguments = args4CLI("memcached -m 1024 -c 256");
		String[] arguments = args4CLI("memcached -c 256");
		System.out.println(Arrays.asList(arguments));


      
		//REFER: http://zhenghaoju700.blog.163.com/blog/static/13585951820120162145548/ 
		//S1： 定义参数阶段
		Options opts = new Options();
		opts.addOption("m", true, "memory size required");
		opts.addOption("c", true, "conn limit required");
		
		opts.addOption("h", "help", false, "Usage: XXX");
		
		
		//S2：解析器
		BasicParser parser = new BasicParser();
		CommandLine cli = parser.parse(opts, arguments);
		if ( ! cli.hasOption("m")) {
			System.out.println("参数m是必选的");
			return ;
		}
		String m = cli.getOptionValue("m");
		System.out.println(m);
	}
	
	private static String[] args4CLI(String cmd) {
		StringTokenizer st = new StringTokenizer(cmd);
		String[] args = new String[st.countTokens()];
	    int c = 0;
		while(st.hasMoreElements() ){
			args[c] = st.nextToken();
			c++;
	    }
		return args;
	}
	
	static void demoTokenizer() {
//		String s = new String("The Java platform is the ideal   platform for network computing");
		String s = "-d -m 1024 -c 256";
       StringTokenizer st = new StringTokenizer(s);
       System.out.println( "Token Total: " + st.countTokens() );
       while( st.hasMoreElements() ){
         System.out.println( st.nextToken() );
      }
	}

}
