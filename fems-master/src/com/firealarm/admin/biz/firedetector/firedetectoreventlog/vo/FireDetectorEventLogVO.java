package com.firealarm.admin.biz.firedetector.firedetectoreventlog.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;
import com.firealarm.admin.biz.firedetector.firedetectormng.vo.FireDetectorMngVO.BaseView;

import framework.annotation.ExcelColumn;
import lombok.Data;

@Data
public class FireDetectorEventLogVO {

    private Integer rn;

    /** 화재감지기신호고유번호 */
    private long fireDetectorSignalSeq;

    /** 화재감지기고유번호 */
    private long fireDetectorSeq;

    /** 관제지역고유번호 */
    @ExcelColumn(name="관제지역명", order=10)
    private long mngAreaSeq;

    /** 전통시장고유번호 */
    @ExcelColumn(name="관제시장명", order=20)
    private long marketSeq;

    /** 점포고유번호 */
    private Long storeSeq;

    /** 점포명*/
    @ExcelColumn(name="점포명", order=30)
    private String storeName;

    /** 모델번호 */
	@ExcelColumn(name="모델번호", order=35)
	@JsonView({BaseView.class})
	private String modelNo;

    /** CTN번호 */
    @ExcelColumn(name="CTN번호", order=40)
    private String ctnNo;

    /** 메시지버전 */
    private String msgVer;

    /** 신호타입 */
    @ExcelColumn(name="신호타입", order=50)
    private String signalType;

    /** 연기값 */
    @ExcelColumn(name="연기값", order=75)
    private String smokeValue;

    /** 온도값 */
    @ExcelColumn(name="온도값", order=85)
    private String temperatureValue;

    /** 불꽃1값 */
    @ExcelColumn(name="불꽃1값 ", order=100)
    private String flame1Value;

    /** 불꽃2값 */
    @ExcelColumn(name="불꽃2값  ", order=105)
    private String flame2Value;

    /** CO값 */
    @ExcelColumn(name="CO값  ", order=115)
    private String coValue;

    /** 연기이벤트여부 */
    @ExcelColumn(name="연기이벤트여부  ", order=70)
    private boolean smokeEvent;

    /** 온도이벤트여부 */
    @ExcelColumn(name="온도이벤트여부  ", order=80)
    private boolean temperatureEvent;

    /** 불꽃이벤트여부 */
    @ExcelColumn(name="불꽃이벤트여부  ", order=90)
    private boolean flameEvent;

    /** CO이벤트여부 */
    @ExcelColumn(name="CO이벤트여부  ", order=110)
    private boolean coEvent;

    /** 비화재보여부 */
    @ExcelColumn(name="비화재보", order=160)
    private boolean notFireYn;

    /** 신호발생일시 */
    @ExcelColumn(name="발생일시  ", order=170)
    private LocalDateTime demonRegDate;

    /** 등록일시 */
    private LocalDateTime regDate;

    /** 배터리2값 */
    @ExcelColumn(name="3.6V 배터리(%)", order=121)
    private long battery2Value;

    /** 배터리값 */
    @ExcelColumn(name="3V 배터리(%)", order=120)
    private long batteryValue;

}
