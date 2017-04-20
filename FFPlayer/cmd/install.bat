@echo off
cd D:\Project\GitHub\FFPlayer
D:
javapackager.exe -createjar -classpath lib/fst-1.58.jar;lib/commons-logging-1.1.3.jar;lib/httpclient-4.3.6.jar;lib/httpcore-4.3.3.jar;lib/jflyfox_base-2.0.jar -appclass com.flyfox.ffplayer.MainFFPlayer -srcdir bin -outdir target/jar -outfile ffplayer.jar
rem javapackager.exe -createjar -appclass com.flyfox.ffplayer.MainFFPlayer -native resources -srcdir bin -outdir target/jar -outfile ffplayer.jar

mkdir  target\jar\lib
copy lib target\jar\lib
mkdir  target\jar\resources
mkdir  target\jar\resources\lyric
copy resources target\jar\resources
copy resources\lyric target\jar\resources\lyric

javapackager.exe -deploy -appclass com.flyfox.ffplayer.MainFFPlayer -native image -srcdir target/jar -outdir target/out -outfile ffplayer -name FFPlayer
