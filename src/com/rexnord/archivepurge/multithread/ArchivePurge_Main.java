package com.rexnord.archivepurge.multithread;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ArchivePurge_Main {

	static Logger logger = Logger.getLogger(ArchivePurge_Main.class.getName());  
	public static Properties props;
	public static String Deletion_Folder;

	public static void main(String[] args) throws IOException, InterruptedException {

		BasicConfigurator.configure();
		
		logger.info("Archive Purge Mechanism - job started");
		
		if (args.length < 1) {
			logger.error("Properties file not found");
			System.exit(1);
		}
		else {
			logger.info("Properties file loaded successfully");
		}
		String propertiesFileName = args[0];

		getproperties(propertiesFileName);

		new TreadSpawn(Deletion_Folder).start();

	}

	public static void getproperties(String propertiesFileName) {
		props = new Properties();

		try {

			props.load(new FileInputStream("properties//" + propertiesFileName));

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

		Deletion_Folder = props.getProperty("Deletion_folder").trim();

	}
}