package com.firealarm.admin.biz.statistics.montheventstats.vo;

import framework.annotation.ExcelColumn;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MonthEventStatsVO {

	/* 달(Month) */
	@ExcelColumn(name="월", order=1)
	private String mm;

	/* 연기 */
	@ExcelColumn(name="연기 화재", order=11)
	private int smokeCount;

	/* 온도 */
	@ExcelColumn(name="온도 화재", order=12)
	private int temperatureCount;

	/* 불꽃 */
	@ExcelColumn(name="불꽃 화재", order=13)
	private int flameCount;

	/* CO */
	@ExcelColumn(name="CO 화재", order=14)
	private int coCount;

	/* 합계 */
	@ExcelColumn(name="합계", order=15)
	private int rowSum;

}
