@echo off
cd /e "E:\Archive_purge_eclipse\Deletion_mechanism"
java -classpath E:\Archive_purge_eclipse\Deletion_mechanism\bin;E:\Archive_purge_eclipse\Deletion_mechanism\lib\log4j-1.2.17.jar com.rexnord.archivepurge.multithread.ArchivePurge_Main props.properties