package parser;

public class RunProgram {
	public static void main(String[] args) {
		Log.i("[main] Starting program...");
		Config cfg = new Config("res\\config.xml");
		Log.i("[main] printing cfg... \n" + cfg.toString() + "\n[main] ... end of print;");
		
		AHKLogParser log = new AHKLogParser(cfg);
		Log.i("[main] " + log.toString());
	}
}
