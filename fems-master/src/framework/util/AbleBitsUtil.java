package framework.util;

import org.apache.commons.lang3.StringUtils;

import framework.security.util.BitArray;

/**
 * Bit 연산 관련 유틸
 *
 * @author ByeongDon
 */
public class AbleBitsUtil {
	private AbleBitsUtil() {}

	/** 문자열의 왼쪽 Zero Padding처리 */
	public static String leftZeroPad(String str, int size) {
		return StringUtils.leftPad(str, size, "0");
	}

	/** 이진숫자문자열을 BitArray로 변환 */
	public static BitArray binStringToBits(String register) {
		BitArray bits = new BitArray(register.length());
		char[] charArray = register.toCharArray();
		for (int i = 0; i<charArray.length; i++) {
			bits.set(i, charArray[i] == '1');
		}
		return bits;
	}

	/** BitArray를 이진숫자문자열로 변환 */
	public static String bitsToBinString(BitArray bits) {
		StringBuilder sb = new StringBuilder(bits.length());
		boolean[] boolArray = bits.toBooleanArray();
		for (boolean b : boolArray) {
			sb.append(b?"1":"0");
		}
		return sb.toString();
	}

	/** 길이가 같은 BitArray로 XOR연산결과 획득 */
	public static BitArray xorBitArray(BitArray a1, BitArray a2) {
		boolean[] b1 = a1.toBooleanArray();
		boolean[] b2 = a2.toBooleanArray();
		boolean[] bResult = new boolean[b1.length];
		for (int i = 0; i < bResult.length; i++) {
			bResult[i] = b1[i] ^ b2[i];
		}
		return new BitArray(bResult);
	}

	/** 문자열 앞부분과 뒷부분을 바꾸기. shuffleLen이 음수이면 역으로 바꾸기(홀수길이에서 영향) */
	public static String shuffleString(String str, int shuffleLen) {
		if(shuffleLen > 0) {
			return StringUtils.right(str, str.length() - shuffleLen) + StringUtils.left(str, shuffleLen);
		} else {
			int shuffleLenReversed = shuffleLen * -1;
			return StringUtils.right(str, shuffleLenReversed) + StringUtils.left(str, str.length() - shuffleLenReversed);
		}
	}


}
