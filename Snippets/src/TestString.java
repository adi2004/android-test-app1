import java.util.GregorianCalendar;

public class TestString {
	public static enum SB {
		SBUFF, SBUILD
	};

	public static long MAX = 1000000;

	static long timeBuff = 0;
	static long timeBuild = 0;

	public static void main(String[] args) {
		//startThreads();
		testSBuild();
		testSBuff();
		testS();
	}

	private static long testS() {
		long start = new GregorianCalendar().getTimeInMillis();
		long startMemory = Runtime.getRuntime().freeMemory();
		String s = "";
		for (int i = 0; i < MAX; i++) {
			s = s + ":" + i;
		}
		long end = new GregorianCalendar().getTimeInMillis();
		long endMemory = Runtime.getRuntime().freeMemory();
		System.out.println("Time Taken for s :" + (end - start));
		System.out.println("Memory used:" + (startMemory - endMemory));
		return end - start;
	}

	private static void startThreads() {
		Runnable thbuff = new Runnable() {
			@Override
			public void run() {
				timeBuff += testSBuff();
			}
		};
		Runnable thbuild = new Runnable() {
			@Override
			public void run() {
				timeBuild += testSBuild();
			}
		};

		int i, nrThreds = 30;
		for (i = 0; i < nrThreds; i++) {
			Thread thread = new Thread(thbuff);
			thread.start();
		}
		for (i = 0; i < nrThreds; i++) {
			Thread thread = new Thread(thbuild);
			thread.start();
		}
	}

	private static long testSBuff() {
		long start = new GregorianCalendar().getTimeInMillis();
		long startMemory = Runtime.getRuntime().freeMemory();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < MAX; i++) {
			sb.append(":").append(i);
		}
		long end = new GregorianCalendar().getTimeInMillis();
		long endMemory = Runtime.getRuntime().freeMemory();
		System.out.println("Time Taken for sbuff :" + (end - start));
		System.out.println("Total time for sbuff: " + timeBuff);
		System.out.println("Memory used:" + (startMemory - endMemory));
		return end - start;
	}

	private static long testSBuild() {
		long start = new GregorianCalendar().getTimeInMillis();
		long startMemory = Runtime.getRuntime().freeMemory();
		StringBuilder sb2 = new StringBuilder();
		for (int i = 0; i < MAX; i++) {
			sb2.append(":").append(i);
		}
		long end = new GregorianCalendar().getTimeInMillis();
		long endMemory = Runtime.getRuntime().freeMemory();
		System.out.println("Time Taken for sbuild :" + (end - start));
		System.out.println("Total time for sbuild: " + timeBuild);
		System.out.println("Memory used:" + (startMemory - endMemory));
		return end - start;
	}
}