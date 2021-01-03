package lyx;

public class Function {

	public int bytetoInt(byte[] data) {
		return (int) (((data[0] & 0xff) << 8) | (data[1] & 0xff));
	}

	public byte[] Inttobyte(int data) {
		byte[] result = new byte[2];

		result[0] = (byte) ((data >> 8) & 0xFF);
		result[1] = (byte) (data & 0xFF);
		return result;
	}

	public byte[] Inttobyte2(int data) {
		byte[] result = new byte[4];
		result[0] = (byte) ((data >> 24) & 0xFF);
		result[1] = (byte) ((data >> 16) & 0xFF);
		result[2] = (byte) ((data >> 8) & 0xFF);
		result[3] = (byte) (data & 0xFF);
		return result;
	}

	public byte[] stringtobyte(String IP) {
		byte[] result = new byte[4];
		String[] IPpart = IP.split("\\.");
		if (IPpart.length != 4) {
			return null;
		}
		for (int i = 0; i < IPpart.length; i++) {
			int num = Integer.parseInt(IPpart[i]);
			byte tmp = 0;
			if (num > 127) {
				tmp = (byte) (num - 256);
			} else {
				tmp = (byte) num;
			}
			result[i] = tmp;
		}
		return result;
	}
}
