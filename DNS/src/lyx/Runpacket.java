package lyx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Runpacket implements Runnable {
	DatagramPacket packet;
	private byte[] data;
	private static DatagramSocket socket;
	private InetAddress clientAddress;
	private int clientPort;
	public static String[][] database = new String[1024][2];

	public Runpacket() {
	}

	public Runpacket(DatagramPacket packet) {
		clientAddress = packet.getAddress();
		clientPort = packet.getPort();
		this.packet = packet;
		data = new byte[packet.getData().length];
		System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());
//		System.out.println(1);
	}

	public void run() {
		String[] temp = new String[200];
		String delimeter = " ";
		int total_row = 0;
		try {
			String path = "src/lyx/dnsrelay.txt";

			File filename = new File(path);
			FileReader fileReader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;
			while ((line = reader.readLine()) != null) {
				temp = line.split(delimeter);
				for (int j = 0; j < 2; j++) {
					database[total_row][j] = temp[j];
				}
				total_row++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		int off = 0;
		byte[] buff = new byte[2];
		Header dnsheader = new Header();
		Function function = new Function();
		for (int i = 0; i < 2; i++) {
			buff[i] = data[i + off];
		}
		off += 2;
		dnsheader.setTransID(buff);

		for (int i = 0; i < 2; i++) {
			buff[i] = data[i + off];
		}
		off += 2;
		dnsheader.setFlags(buff);

		for (int i = 0; i < 2; i++) {
			buff[i] = data[i + off];
		}
		off += 2;
		dnsheader.setQdcount(buff);

		for (int i = 0; i < 2; i++) {
			buff[i] = data[i + off];
		}
		off += 2;
		dnsheader.setAncount(buff);

		for (int i = 0; i < 2; i++) {
			buff[i] = data[i + off];
		}
		off += 2;
		dnsheader.setNscount(buff);

		for (int i = 0; i < 2; i++) {
			buff[i] = data[i + off];
		}
		off += 2;
		dnsheader.setArcount(buff);
////////////
		String domainName = null;
		int match = 0;
		int row = 0;
		String IP = "";
		if (function.bytetoInt(dnsheader.getQdcount()) > 0) {
			StringBuffer stringBuffer = new StringBuffer();
			while ((data[off] & 0xFF) != 0) {
				int cashe = data[off] & 0xFF;
				byte[] B = new byte[cashe];
				off++;
				System.arraycopy(data, off, B, 0, cashe);
				String s = null;
				try {
					s = new String(B, "GB2312");
				} catch (IOException e) {
					e.printStackTrace();
				}
				stringBuffer.append(s);
				off += cashe;
				if (((data[off]) & 0xFF) != 0)
					stringBuffer.append(".");
			}
			domainName = stringBuffer.toString();
		} // 读取网站名字

		off += 1;
		byte[] type = new byte[2];
		for (int i = 0; i < 2; i++) {
			type[i] = data[i + off];
		}
		off += 2;
		byte[] class_1 = new byte[2];
		for (int i = 0; i < 2; i++) {
			class_1[i] = data[i + off];
		}
		off += 2;// 读取type 和class
	///////	
		for (row = 0; row < total_row; row++) {
			if (domainName.equals(database[row][1])) {
				match = 1;
				IP = database[row][0];
				break;
			}
		} // 和数据库进行匹配

		System.out.println("Query: " + domainName);
		if (match == 1 && function.bytetoInt(type) == 1) {
			int flag;
			if (!IP.equals("0.0.0.0")) { // rcode为0（没有差错）
				flag = 0x8580;
			} else { // rcode为3（名字差错），只从一个授权名字服务器上返回，它表示在查询中指定的域名不存在
				flag = 0x8583;
			}
			byte[] response = new byte[1024];

			int offset = 0;
			for (offset = 0; offset < 2; offset++)
				response[offset] = data[offset];
			System.arraycopy(function.Inttobyte(flag), 0, response, offset, 2);
			offset += 2;
			for (int i = 0; i < 2; i++, offset++)
				response[offset] = data[offset];
			System.arraycopy(function.Inttobyte(1), 0, response, offset, 2);
			offset += 2;
			System.arraycopy(function.Inttobyte(1), 0, response, offset, 2);
			offset += 2;
			System.arraycopy(function.Inttobyte(1), 0, response, offset, 2);
			offset += 2; // header

			for (int i = 0; i < domainName.length() + 2; i++, offset++)
				response[offset] = data[offset];
			for (int i = 0; i < 2; i++, offset++)
				response[offset] = type[i];
			for (int i = 0; i < 2; i++, offset++)
				response[offset] = class_1[i];// query

			if (!IP.equals("0.0.0.0")) {
				System.arraycopy(function.Inttobyte(0xc00c), 0, response, offset, 2);
				offset += 2;
				for (int i = 0; i < 2; i++, offset++)
					response[offset] = type[i];
				for (int i = 0; i < 2; i++, offset++)
					response[offset] = class_1[i];
				System.arraycopy(function.Inttobyte2(3600 * 24), 0, response, offset, 4);
				offset += 4;
				System.arraycopy(function.Inttobyte(4), 0, response, offset, 2);
				offset += 2;
				System.arraycopy(function.stringtobyte(IP), 0, response, offset, function.stringtobyte(IP).length);
				offset += function.stringtobyte(IP).length;
			}

			System.arraycopy(function.Inttobyte(0xc00c), 0, response, offset, 2);
			offset += 2;
			System.arraycopy(function.Inttobyte(6), 0, response, offset, 2);
			offset += 2;
			for (int i = 0; i < 2; i++, offset++)
				response[offset] = class_1[i];
			System.arraycopy(function.Inttobyte2(3600 * 24), 0, response, offset, 4);
			offset += 4;
			System.arraycopy(function.Inttobyte(0), 0, response, offset, 2);
			offset += 2;
//////////////////9.11
			
			byte[] final_response = new byte[offset];
			for (int i = 0; i < offset; i++)
				final_response[i] = response[i];
			DatagramPacket ResponsePacket = new DatagramPacket(final_response, final_response.length, clientAddress,
					clientPort);

			synchronized (this) {
				try {
					Delay.getSocket().send(ResponsePacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		else {
			try {
				InetAddress dnsServerAddress = InetAddress.getByName("202.106.0.20");
				DatagramPacket internetSendPacket = new DatagramPacket(data, data.length, dnsServerAddress, 53);
				DatagramSocket internetSocket = new DatagramSocket();
				internetSocket.send(internetSendPacket);
				byte[] receivedData = new byte[1024];
				DatagramPacket internetReceivedPacket = new DatagramPacket(receivedData, receivedData.length);
				internetSocket.receive(internetReceivedPacket);

				DatagramPacket responsePacket = new DatagramPacket(receivedData, internetReceivedPacket.getLength(),
						clientAddress, clientPort);
				internetSocket.close();
				synchronized (this) {
					try {
						Delay.getSocket().send(responsePacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
