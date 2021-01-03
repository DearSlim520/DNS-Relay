package lyx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Delay {
	private static DatagramSocket socket;

	static DatagramSocket getSocket() {
		return socket;
	}

	public static void main(String[] arg) {

		try {
			socket = new DatagramSocket(53);
			System.out.println("Connected to port 53.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = new byte[1024];

		DatagramPacket packet = new DatagramPacket(data, 1024);
		ExecutorService servicePool = Executors.newFixedThreadPool(10); // 容纳10个线程的线程池

		while (true) {
			for (int k = 0; k < 10; k++) {
				try {
					socket.receive(packet);
					servicePool.execute(new Runpacket(packet));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
