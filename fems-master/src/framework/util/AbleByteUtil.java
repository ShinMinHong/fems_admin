package framework.util;

import java.nio.ByteBuffer;

public class AbleByteUtil {

	private AbleByteUtil() { /**/ }

	/**
	 * 두 바이트 배열을 병합
	 */
	public static byte[] mergeBytes(byte[] a, byte[] b) {
		ByteBuffer packet = ByteBuffer.allocate(a.length + b.length);
		packet.put(a);
		packet.put(b);
		return packet.array();
	}

	/**
	 * 주어진 바이트 배열에서 왼쪽 일부를 제거한 바이트 배열을 획득
	 */
	public static byte[] skipBytes(byte[] a, int skiplength) {
		ByteBuffer packet = ByteBuffer.allocate(a.length - skiplength);
		packet.put(a, skiplength, a.length - skiplength);
		return packet.array();
	}
}
