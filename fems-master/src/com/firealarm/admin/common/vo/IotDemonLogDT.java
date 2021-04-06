package com.firealarm.admin.common.vo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.LocalDateTime;

import com.firealarm.admin.appconfig.CodeMap.DETECTOR_SIGNAL_TYPE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IotDemonLogDT implements Comparable<IotDemonLogDT>, Serializable {
	private static final long serialVersionUID = -2566089417202227110L;

	/** 화재감지기원본신호고유번호 */
	private long fireDetectorDemonSignalSeq;

	/** 메시지버전 */
	private String msgVer;

	/** IP주소 */
	private String ip;

	/** Port번호 */
	private String port;

	/** CTN번호 */
	private String ctnNo;

	/** 배터리값 */
	private String batteryValue;

	/** 연기값 */
	private String smokeValue;

	/** 온도값 */
	private String temperatureValue;

	/** 불꽃1값 */
	private String flame1Value;

	/** 불꽃2값 */
	private String flame2Value;

	/** CO값 */
	private String coValue;

	/** 신호타입값 */
	private String signalTypeValue;

	/** 화재이벤트타입 */
	private String fireEventValue;

	/** 신호발생일시 */
	private LocalDateTime demonRegDate;

	/** 확인여부 */
	private boolean confirmYn;

	/** 확인일시 */
	private LocalDateTime confirmDate;

	/** 등록일시 */
	private LocalDateTime regDate;

	public DETECTOR_SIGNAL_TYPE getSignalType() {
		return DETECTOR_SIGNAL_TYPE.getSignalTypeFromDemonValue(signalTypeValue);
	}

	private boolean isValidFireEventValue() {
		return StringUtils.isNotEmpty(fireEventValue) && (fireEventValue.length() == 4);
	}

	public boolean isFireEvent() {
		return isValidFireEventValue() && StringUtils.contains(fireEventValue, '1');
	}

	public boolean isFireEventBySmoke() {
		return isValidFireEventValue() && ('1' == fireEventValue.charAt(3));
	}

	public boolean isFireEventByOtherThanSmoke() {
		return isValidFireEventValue() && StringUtils.contains(StringUtils.substring(fireEventValue, 0, 3), '1');
	}

	public boolean isFireEventByTemperature() {
		return isValidFireEventValue() && ('1' == fireEventValue.charAt(2));
	}

	public boolean isFireEventByFlame() {
		return isValidFireEventValue() && ('1' == fireEventValue.charAt(1));
	}

	public boolean isFireEventByCo() {
		return isValidFireEventValue() && ('1' == fireEventValue.charAt(0));
	}

	public int getBatteryValueAsInteger() {
		return NumberUtils.toInt(StringUtils.substring(batteryValue, 2, 4));
	}

	public int getBattery2ValueAsInteger() {
		return NumberUtils.toInt(StringUtils.substring(batteryValue, 4, 6));
	}

	@Override
	public int compareTo(IotDemonLogDT compare) {
		Long l1 = Long.valueOf(this.fireDetectorDemonSignalSeq);
		Long l2 = Long.valueOf(compare.fireDetectorDemonSignalSeq);
		return l1.compareTo(l2);
	}

	@Override
	public String toString() {
		return "IotDemonLogDT [fireDetectorDemonSignalSeq=" + fireDetectorDemonSignalSeq + ", msgVer=" + msgVer
				+ ", ip=" + ip + ", port=" + port + ", ctnNo=" + ctnNo + ", batteryValue=" + batteryValue
				+ ", smokeValue=" + smokeValue + ", temperatureValue=" + temperatureValue + ", flame1Value="
				+ flame1Value + ", flame2Value=" + flame2Value + ", coValue=" + coValue + ", signalTypeValue="
				+ signalTypeValue + ", fireEventValue=" + fireEventValue + ", demonRegDate=" + demonRegDate
				+ ", regDate=" + regDate + "]";
	}
}
