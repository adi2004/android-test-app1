package thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StopThread {
	private Lock lock = new ReentrantLock();

	public static void main(String... args) {
		System.out.println("Starting threads...");
		(new StopThread()).startThreads(20);
	}

	private void startThreads(int nrOfThreads) {
		for (int i = 0; i < nrOfThreads; i++) {
			startThread(i, (long) (Math.random() * 10000000000l));
		}
	}

	private void startThread(final int number, final long load) {
		Thread workerThread = new Thread("Number " + number) {
			@Override
			public void run() {
				try {
					lock.lock();
					doAlgorithmWork(load);
				} finally {
					System.out.println("Thread " + number + " finished...");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						System.out.println("Intrerupted");
						return;
					}
					System.out.println("Unlocking from thread...");
					lock.unlock();
				}
			}
		};
		System.out.println("Started thread number " + number);
		workerThread.start();
		try {
			workerThread.join(100);
		} catch (InterruptedException e) {
		} catch (IllegalMonitorStateException e) {
		}

		//block until either the thread is done, or 100ms passed
		if (workerThread.isAlive()) {
			workerThread.interrupt();
			workerThread.stop(); //if thread is still alive, stop it
			System.out.println("Unlocking from main...");
			lock.unlock();
			System.out.println("Thread " + number + " is now dead!");
		}
	}

	protected void doAlgorithmWork(long load) {
		while (load-- > 0) {
		}
	}
}
