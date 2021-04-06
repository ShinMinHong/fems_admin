package com.firealarm.admin.common.vo;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;
import framework.annotation.ExcelColumn;
import framework.base.model.AbleView;
import lombok.Data;

@Data
public class ManagerAuditDT {

	/** 액션SEQ */
	@JsonView({BaseView.class, DetailsView.class})
	@ExcelColumn(name="액션SEQ", order=10)
	protected Long auditSeq;

	/** 액션SEQ */
	@JsonView({BaseView.class, DetailsView.class})
	@ExcelColumn(name="관제지역SEQ", order=11)
	protected Long mngAreaSeq;

	/** 작업메뉴명 */
	@JsonView({BaseView.class, BaseAction.class, DetailsView.class})
	@ExcelColumn(name="작업메뉴명", order=20)
	protected String menuName;

	/** 작업명 */
	@JsonView({BaseView.class, BaseAction.class, DetailsView.class})
	@ExcelColumn(name="작업명", order=21)
	protected String actionName;

	/** 작업상세정보 */
	@JsonView({BaseView.class, BaseAction.class, DetailsView.class})
	@ExcelColumn(name="작업상세정보", order=30)
	protected String actionDetail;

	/** 아이디 **/
	@JsonView({BaseView.class, DetailsView.class})
	@ExcelColumn(name="아이디", order=40)
	 private String managerId;

	/** 이름 */
	@JsonView({BaseView.class, DetailsView.class})
	@ExcelColumn(name="이름", order=50)
	protected String managerName;

	/** 등록일 **/
	@JsonView({BaseView.class, DetailsView.class})
	@ExcelColumn(name="등록일", order=130)
	 private LocalDateTime regDate;


	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {};
	/** 공통 View */
	public interface DetailsView extends AbleView.CommonBaseView {};

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}
	public interface CreateAction extends BaseAction {}
	public interface UpdateAction extends BaseAction {}
}
