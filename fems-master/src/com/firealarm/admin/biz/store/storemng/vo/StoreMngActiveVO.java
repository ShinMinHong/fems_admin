package com.firealarm.admin.biz.store.storemng.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import framework.annotation.Display;
import framework.base.model.AbleView;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StoreMngActiveVO extends StoreMngVO {

	@Display(name = "00시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm00;

	@Display(name = "01시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm01;

	@Display(name = "02시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm02;

	@Display(name = "03시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm03;

	@Display(name = "04시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm04;

	@Display(name = "05시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm05;

	@Display(name = "06시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm06;

	@Display(name = "07시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm07;

	@Display(name = "08시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm08;

	@Display(name = "09시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm09;

	@Display(name = "10시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm10;

	@Display(name = "11시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm11;

	@Display(name = "12시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm12;

	@Display(name = "13시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm13;

	@Display(name = "14시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm14;

	@Display(name = "15시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm15;

	@Display(name = "16시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm16;

	@Display(name = "17시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm17;

	@Display(name = "18시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm18;

	@Display(name = "19시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm19;

	@Display(name = "20시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm20;

	@Display(name = "21시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm21;

	@Display(name = "22시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm22;

	@Display(name = "23시 알림제한")
	@JsonView({BaseView.class, BaseAction.class})
	private boolean noAlarm23;

	private List<StoreSmsUserList> storeSmsUserList = new ArrayList<StoreSmsUserList>();

	/** 공통 View */
	public interface BaseView extends AbleView.CommonBaseView {}

	/** 공통 Action */
	public interface BaseAction extends AbleView.CommonBaseView {}

}