package com.sohu.tv.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JPSViewer {
	
	public static void main(String[] args) {

		//REFER: http://docs.oracle.com/javase/1.5.0/docs/tooldocs/share/jps.html 
		//-m: (main) Output the arguments passed to the main method. The output may be null for embedded JVMs. 
		//-l: (long) Output the full package name for the application's main class or the full path name to the application's JAR file. 
		//-v: (verbose) Output the arguments passed to the JVM.
		//-V: Output the arguments passed to the JVM through the flags file (the .hotspotrc file or the file specified by the -XX:Flags=<filename> argument). 
		//OUTPUT FORMAT:  lvmid [ [ classname | JARfilename | "Unknown"] [ arg* ] [ jvmarg* ] ]
		String jpsCmd = "jps -mlvV";
		int count = 0;
		try {
			Process subprocess = Runtime.getRuntime().exec(jpsCmd);
			
			//流总是有两端：子进程的输出流，源头是子进程，目的地是父进程。
			//站在子进程的角度，它是一个OutputStream；但站在父进程的角度，它却是一个InputStream。
			//因此，要读取子进程的输出信息，应该getInputStream()，而不是getOutputStream()。
			//简单的说，子进程没有控制台，因此子进程将STDIN,STDOUT,STDERR都重定向到父进程。
			//subprocess -redirect->  STDOUT -->[boundary] --> InputStream --> parentprocess
			InputStream subStdout = subprocess.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(subStdout));

			String aline = null;
			while ((aline = reader.readLine()) != null) {
				String[] items = aline.split(" ");
				
				if (items.length >= 2) {
					//NAME: pair[1]
					if (items[1].trim().indexOf("TestRun") > -1) {

						count++;

						System.out.println("count is " + count);

					}

				}

			}

			if (count > 1) {
				System.out.println("Has run a application!");
				return;
			}

		} catch (Exception ex) {

			ex.printStackTrace();

		}

		try {

			System.in.read();

		} catch (Exception ex) {

			ex.printStackTrace();

		}
	}

}
