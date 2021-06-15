package com.rexnord.archivepurge.multithread;

import java.io.File;

import org.apache.log4j.Logger;

class TreadSpawn extends Thread {
	static Logger logger = Logger.getLogger(TreadSpawn.class.getName());  

	private static String basepath;
	
	private static int counter = 0;

	TreadSpawn(String basepath) throws InterruptedException {
		Thread.sleep(1000);
		TreadSpawn.setBasepath(basepath);
	}

	@Override
	public void run() {
		File rootDir = new File(getBasepath());

		//System.out.println(rootDir);

		for (File f : rootDir.listFiles()) {
			if (f.isDirectory()) {
				try {
					new TreadSpawn(f.toString()).start();
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}

			} else {

				for (Object k : ArchivePurge_Main.props.keySet()) {
					String key = (String) k;

					if (!key.contains("Deletion_folder")) {

						if (f.getAbsoluteFile().toString().contains(key) && f.lastModified() < System
								.currentTimeMillis()
								- (Integer.parseInt(ArchivePurge_Main.props.getProperty(key)) * 24 * 60 * 60 * 1000L)) {
							
							// f.delete();
							
							logger.info(f + " deleted successfully, file size:= " + f.length() / 1024 + " kb," + " File count:= " + counter);
							
							counter++;

						}
					}

				}
			}

		}

		System.gc();
	}
	

	public static String getBasepath() {
		return basepath;
	}

	public static void setBasepath(String basepath) {
		TreadSpawn.basepath = basepath;
	}

}