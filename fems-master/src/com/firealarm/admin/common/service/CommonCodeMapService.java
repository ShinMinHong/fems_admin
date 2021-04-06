package com.firealarm.admin.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firealarm.admin.appconfig.CodeMap.APP_USER_GRADE;
import com.firealarm.admin.common.dao.CommonCodeMapDAO;
import com.firealarm.admin.common.support.ServiceSupport;
import com.firealarm.admin.common.vo.AreaStoreNameVO;
import com.firealarm.admin.common.vo.SelectOption;
import com.firealarm.admin.security.util.UserSecurityUtil;
import com.firealarm.admin.security.vo.AppUserDetails;

@Service
public class CommonCodeMapService extends ServiceSupport {

	@Autowired CommonCodeMapDAO commonCodeMapDAO;

	/** selectOptions Map Converter */
	public Map<String, String> getCodepMapFromSelectOptions(List<SelectOption> selectOptions){
		Map<String, String> codeMap = new HashMap<String, String>();
		for ( SelectOption selectOption : selectOptions ){
			codeMap.put(selectOption.getValue(), selectOption.getText());
		}
		return codeMap;
	}

	/** 앱의 권한그룹코드 MAP */
	public List<SelectOption> getAppUserGradeCodeMap(){
		return commonCodeMapDAO.getAppUserGradeCodeMap();
	}

	/** 관리자의 접근가능한 관리자구분 */
	public List<SelectOption> getUserGradeCodeMap(){
		AppUserDetails currentUserDetails = UserSecurityUtil.getCurrentUserDetails();
		if(currentUserDetails == null) {
			return new ArrayList<>();
		}
		List<SelectOption> userGradeCodeMap = commonCodeMapDAO.getAppUserGradeCodeMap();
		switch (currentUserDetails.getRolegroupCode()) {
	 	 	case AREA_ADMIN:
	 	 		userGradeCodeMap.removeIf(obj -> APP_USER_GRADE.HQ_ADMIN.equals(APP_USER_GRADE.valueOf(obj.getValue())));
	 	 		break;
	 	 	case MARKET_ADMIN:
	 	 		userGradeCodeMap.removeIf(obj -> APP_USER_GRADE.HQ_ADMIN.equals(APP_USER_GRADE.valueOf(obj.getValue())));
	 	 		userGradeCodeMap.removeIf(obj -> APP_USER_GRADE.AREA_ADMIN.equals(APP_USER_GRADE.valueOf(obj.getValue())));
				break;
	 	 	case HQ_ADMIN:
		    default:
		    	break;
		 }
		 return userGradeCodeMap;
	}

	/** 지역코드 MAP */
	public List<SelectOption> getMngAreaNameCodeMap(){
		return commonCodeMapDAO.getMngAreaNameCodeMap();
	}

	/**
	 * 특정 관제 지역을 위한 지역코드 MAP
	 * @param mngAreaSeqAsString 특정 관제 지역 Seq의 String 값
	 */
	public List<SelectOption> getMngAreaNameCodeMapByMngAreaSeq(long mngAreaSeq){
		String mngAreaSeqAsString = Long.toString(mngAreaSeq);
		List<SelectOption> resultCodeMap = getMngAreaNameCodeMap();
		return resultCodeMap.stream().filter(c->c.getValue().equals(mngAreaSeqAsString)).collect(Collectors.toList());
	}

	/** 시장 Code Map */
	public List<SelectOption> getMarketNameCodeMap(){
		return commonCodeMapDAO.getMarketNameCodeMap();
	}


	/**
	 * 특정 관제 지역을 위한 시장코드 MAP
	 * @param mngAreaSeq 특정 관제 지역 Seq
	 */
	public List<SelectOption> getMarketNameCodeMapByMngAreaSeq(long mngAreaSeq){
		return commonCodeMapDAO.getMarketNameCodeMapByMngAreaSeq(mngAreaSeq);
	}

	/**
	 * 특정 시장을 위한 시장코드 MAP
	 * @param marketSeqAsString 특정 시장 Seq의 String 값
	 */
	public List<SelectOption> getMarketNameCodeMapByMarketSeq(long marketSeq){
		String marketSeqAsString = Long.toString(marketSeq);
		List<SelectOption> resultCodeMap = getMarketNameCodeMap();
		return resultCodeMap.stream().filter(c->c.getValue().equals(marketSeqAsString)).collect(Collectors.toList());
	}

	/** 관제지역 점포명 List */
	public List<AreaStoreNameVO> getStoreNameListByMngAreaSeq(long mngAreaSeq){
		return commonCodeMapDAO.getStoreNameListByMngAreaSeq(mngAreaSeq);
	}

	/** 시장 점포명 List */
	public List<AreaStoreNameVO> getStoreNameListByMarketSeq(long marketSeq){
		return commonCodeMapDAO.getStoreNameListByMarketSeq(marketSeq);
	}
}
