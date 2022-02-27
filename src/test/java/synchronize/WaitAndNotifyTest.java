package synchronize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WaitAndNotifyTest {
	private static final int PRODUCER_THREAD_SIZE = 4;
	private static final int CONSUMER_THREAD_SIZE = 3;
	private static final int BUFFER_SIZE = 3;
	private static CountDownLatch producerLatch = new CountDownLatch(PRODUCER_THREAD_SIZE);
	private static CountDownLatch consumerLatch = new CountDownLatch(CONSUMER_THREAD_SIZE);

	/**
	 * 실행해서 로그를 확인해보자.
	 * bound buffer가 꽉 차있으면 producer는 wait 상태에 들어간다. (그리고 대기 집합에 들어간다)
	 * 만약 consumer에서 buffer를 비우고 notify()를 호출하면, 대기 집합에 있던 스레드를 다시 락을 획득할 수 있도록 진입 집합에 할당된다. 그리고
	 * 멈추었던 코드부터 다시 실행한다.
	 * 이하는 실행 로그이다.
	 * --------
	 * start :: PRO 3
	 * start :: PRO 2
	 * start :: PRO 1
	 * start :: PRO 0
	 * inserted :: PRO 3
	 * notify :: PRO 3
	 * inserted :: PRO 0
	 * notify :: PRO 0
	 * inserted :: PRO 1
	 * notify :: PRO 1
	 * [wait] :: PRO 2
	 * start :: CON 0
	 * start :: CON 1
	 * start :: CON 2
	 * removed :: CON 0
	 * notify :: CON 0
	 * [resume] :: PRO 2
	 * inserted :: PRO 2
	 * notify :: PRO 2
	 * removed :: CON 2
	 * notify :: CON 2
	 * removed :: CON 1
	 * notify :: CON 1
	 * --------
	 * PRO 2가 봉쇄되어 대기하다가, consumer에 의해 remove가 되어 사용할 수 있게 되자 다시 코드를 실행한 것을 볼 수 있다.
	 */
	@DisplayName("wait와 notify 기능 테스트")
	@Test
	void waitAndNotifyTest() throws InterruptedException {
		BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>(BUFFER_SIZE);

		for (int i = 0; i < PRODUCER_THREAD_SIZE; i++) {
			Thread thread = new Thread(new ProducerThread<>(boundedBuffer, 1, i));
			thread.start();
		}

		producerLatch.await(1000, TimeUnit.MILLISECONDS);
		for (int i = 0; i < CONSUMER_THREAD_SIZE; i++) {
			Thread thread = new Thread(new ConsumerThread<>(boundedBuffer, i));
			thread.start();
		}

		consumerLatch.await();
	}

	public static class ProducerThread<T> implements Runnable {
		private BoundedBuffer<T> boundedBuffer;
		private T item;
		private String name;

		public ProducerThread(BoundedBuffer<T> boundedBuffer, T item, int name) {
			this.boundedBuffer = boundedBuffer;
			this.item = item;
			this.name = "PRO " + name;
		}

		@Override
		public void run() {
			System.out.println("start :: " + this.name);
			boundedBuffer.insert(item, this.name);

			producerLatch.countDown();
		}
	}

	public static class ConsumerThread<T> implements Runnable {
		private BoundedBuffer<T> boundedBuffer;
		private String name;

		public ConsumerThread(BoundedBuffer<T> boundedBuffer, int name) {
			this.name = "CON " + name;
			this.boundedBuffer = boundedBuffer;
		}

		@Override
		public void run() {
			System.out.println("start :: " + this.name);
			T removed = boundedBuffer.remove(this.name);

			consumerLatch.countDown();
		}
	}

	public static class BoundedBuffer<T> {
		private int count, in, out;
		private T[] buffer;

		public BoundedBuffer(int bufferSize) {
			count = 0;
			in = 0;
			out = 0;
			this.buffer = (T[]) new Object[bufferSize];
		}

		public synchronized void insert(T item, String name) {
			while (count == BUFFER_SIZE) {
				try {
					System.out.println("[wait] :: " + name);
					wait();
					System.out.println("[resume] :: " + name);
				} catch (InterruptedException e) {
					System.out.println("insert 인터럽트 익셉션 : " + name);
				}
			}
				buffer[in] = item;
				in = (in + 1) % BUFFER_SIZE;
				count++;
				System.out.println("inserted :: " + name);

				System.out.println("notify :: " + name);
				notify();
		}

		public synchronized T remove(String name) {
			T item;
			while (count == 0){
				try {
					System.out.println("[wait] ::" + name);
					wait();
					System.out.println("[resume] :: " + name);
				} catch (InterruptedException e) {
					System.out.println("remove 인터럽트 익셉션 : " + name);
				}
			}

			item = buffer[out];
			out = (out + 1) % BUFFER_SIZE;
			count--;
			System.out.println("removed :: " + name);

			System.out.println("notify :: " + name);
			notify();

			return item;
		}
	}
}
