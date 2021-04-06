package com.firealarm.admin.biz.statistics.storeeventstats.vo;

import framework.annotation.ExcelColumn;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StoreEventStatsVO {

	/* 순번 */
	@ExcelColumn(name="순번", order=1)
	private Integer rn;

	/* 관제지역고유번호 */
	private long mngAreaSeq;

	/* 관제지역명 */
	@ExcelColumn(name="관제지역명", order=3)
	private String mngAreaName;

	/* 전통시장고유번호 */
	private long marketSeq;

	/* 전통시장명 */
	@ExcelColumn(name="전통시장명", order=5)
	private String marketName;

	/* 점포고유번호 */
	private long storeSeq;

	/* 점포명 */
	@ExcelColumn(name="점포명", order=7)
	private String storeName;

	/* 화재감지기고유번호 */
	private Long fireDetectorSeq;

	/* 연기 */
	@ExcelColumn(name="연기", order=11)
	private int smokeCount;

	/* 온도 */
	@ExcelColumn(name="온도", order=12)
	private int temperatureCount;

	/* 불꽃 */
	@ExcelColumn(name="불꽃", order=13)
	private int flameCount;

	/* CO */
	@ExcelColumn(name="CO", order=14)
	private int coCount;

	/* 합계 */
	@ExcelColumn(name="합계", order=15)
	private int rowSum;

}
