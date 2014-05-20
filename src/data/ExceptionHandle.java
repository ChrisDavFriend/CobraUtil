package data;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class ExceptionHandle {
	
	private static PrintStream exceptionLog;
	private static File logFile;
	
	private static boolean logReady;
	
	public static void readyExceptionHandle(File logFile) throws IOException{
		ExceptionHandle.logFile = logFile;
		logFile.createNewFile();
		exceptionLog = new PrintStream(logFile);
		logReady = true;
	}
	
	public static void setLogFile(File logFile){
		ExceptionHandle.logFile = logFile;
	}
	
	public static File getLogFile(){
		return logFile;
	}
	
	public static String printToErrorStream(String error){
		System.err.println(error);
		if(logReady)exceptionLog.println(error);
		else System.err.println("The exception handler was either never readied, or the log file was invalid! No errors will be logged.");
		return error;
	}
	
	public static void clean(){
		exceptionLog.close();
	}
	
	
	public static void noSuchDimension(char dimension){
		throw new IllegalStateException(printToErrorStream("No such dimension '" + dimension + "' exists!"));
	}
	
	public static void sheetAlreadyBound(){
		printToErrorStream("Spritesheet has already been bound!");
	}
	
	public static void noSpritesheetsLoaded(){
		printToErrorStream("No spritesheets have been loaded!");
	}

}
