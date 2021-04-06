package com.firealarm.admin.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.AreaStoreNameVO;
import com.firealarm.admin.common.vo.SelectOption;

import framework.util.AbleUtil;

@Repository
public class CommonCodeMapDAO extends DAOSupport {
	/** 권한그룹코드 MAP */
	public List<SelectOption> getAppUserGradeCodeMap() {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), null);
	}

	/** 관제지역코드 MAP */
	public List<SelectOption> getMngAreaNameCodeMap() {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), null);
	}

	/** 시장 코드 MAP */
	public List<SelectOption> getMarketNameCodeMap() {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), null);
	}

	/** 관제지역-시장 코드 MAP */
	public List<SelectOption> getMarketNameCodeMapByMngAreaSeq(long mngAreaSeq) {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), mngAreaSeq);
	}

	/** 관제지역-점포 Code Map */
	public List<AreaStoreNameVO> getStoreNameListByMngAreaSeq(long mngAreaSeq) {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), mngAreaSeq);
	}

	/** 시장-점포 Code Map */
	public List<AreaStoreNameVO> getStoreNameListByMarketSeq(long marketSeq) {
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), marketSeq);
	}
}
