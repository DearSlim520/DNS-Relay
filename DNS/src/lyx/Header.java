package lyx;

public class Header {
	/**
	 * DNS Header 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7
	 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ | ID |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ |QR| opcode |AA|TC|RD|RA| Z
	 * | RCODE | +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ | QDCOUNT |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ | ANCOUNT |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ | NSCOUNT |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+ | ARCOUNT |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	 */

	private byte[] transID1 = new byte[2];

	private byte[] flags1 = new byte[2];

	private byte[] qdcount1 = new byte[2];

	private byte[] ancount1 = new byte[2];

	private byte[] nscount1 = new byte[2];

	private byte[] arcount1 = new byte[2];

	public Header() {
	}

	public Header(byte[] transID, byte[] flags, byte[] qdcount, byte[] ancount, byte[] nscount, byte[] arcount) {
		transID1 = transID;
		flags1 = flags;
		qdcount1 = qdcount;
		ancount1 = ancount;
		nscount1 = nscount;
		arcount1 = arcount;
	}

	public byte[] getTransID() {
		return transID1;
	}

	public void setTransID(byte[] transID) {
		for (int i = 0; i < 2; i++)
			this.transID1[i] = transID[i];
	}

	public byte[] getFlags() {
		return flags1;
	}

	public void setFlags(byte[] flags) {
		for (int i = 0; i < 2; i++)
			this.flags1[i] = flags[i];
	}

	public byte[] getQdcount() {
		return this.qdcount1;

	}

	public void setQdcount(byte[] qdcount) {
		for (int i = 0; i < 2; i++)
			qdcount1[i] = qdcount[i];

	}

	public byte[] getAncount() {
		return ancount1;
	}

	public void setAncount(byte[] ancount) {
		for (int i = 0; i < 2; i++)
			this.ancount1[i] = ancount[i];
	}

	public byte[] getNscount() {
		return nscount1;
	}

	public void setNscount(byte[] nscount) {
		for (int i = 0; i < 2; i++)
			this.nscount1[i] = nscount[i];
	}

	public byte[] getArcount() {
		return arcount1;
	}

	public void setArcount(byte[] arcount) {
		for (int i = 0; i < 2; i++)
			this.arcount1[i] = arcount[i];
	}
}