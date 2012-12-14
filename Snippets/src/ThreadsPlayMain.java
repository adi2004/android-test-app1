import java.util.Random;

public class ThreadsPlayMain implements Runnable {

	@Override
	public void run() {
		try {
			(new Object()).wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		while (true) {
//
//		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 500; i++) {
			(new Random()).nextInt();
//			(new Math()).
			(new Thread(new ThreadsPlayMain())).start();
//			ThreadsPlayMain sth = new ThreadsPlayMain();
//			sth.run();
//			().run();
		}
	}
}
