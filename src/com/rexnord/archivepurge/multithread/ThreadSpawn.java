package com.rexnord.archivepurge.multithread;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

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

		logger.info("Scanning files in directory:= "+rootDir);

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
							
							 f.delete();
							
							logger.info(f + " deleted successfully, file size:= " + f.length() / 1024 + " kb," + " File count:= " + counter);
							
							counter++;

						}
					}

				}
			}

		}
		
		logger.info("Threadname:" + Thread.currentThread().getName() + ", Thread CPU TIME: "
				+TimeUnit.NANOSECONDS.
                toSeconds( ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()) 
				+ ", Memory used: Heap : ="
				+ ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / (1024.0) + " kb"
				+ ", Memory used: Non Heap := "
				+ ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed() / (1024.0) + " kb");

		logger.info(
				"###############################################THREAD JOB COMPLETED#####################################################################################");

		System.gc();
	}
	

	public static String getBasepath() {
		return basepath;
	}

	public static void setBasepath(String basepath) {
		TreadSpawn.basepath = basepath;
	}

}