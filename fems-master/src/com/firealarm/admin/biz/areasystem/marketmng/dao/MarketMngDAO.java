package com.firealarm.admin.biz.areasystem.marketmng.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.areasystem.marketmng.vo.MarketMngActiveVO;
import com.firealarm.admin.biz.areasystem.marketmng.vo.MarketMngVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 *  전통시장 관리 DAO
 * @author JKS
 */

@Repository
public class MarketMngDAO extends DAOSupport  {

	/** 전체 목록 조회 */
	public List<MarketMngVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, MarketMngVO.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		List<MarketMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 목록 조회 */
	public Page<MarketMngVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), MarketMngVO.class));
		List<MarketMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<MarketMngVO> page = new PageImpl<MarketMngVO>(list, pageableToApply, total);
		return page;
	}

	/** 특정 관제지역의 시장목록 조회 */
	public List<MarketMngVO> getListByMngAreaSeq(long mngAreaSeq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mngAreaSeq", mngAreaSeq);
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 상세 */
	public MarketMngVO getByMarketSeq(long marketSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), marketSeq);
	}

	/** 등록 */
	public void insert(MarketMngActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 수정 */
	public int update(MarketMngActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 삭제 */
	public int delete(long marketSeq) {
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), marketSeq);
	}

	/** 시장 전체 점포의 연기이벤트 전송제외/복구 저장 */
	public void smokealarm(long marketSeq, boolean alarmYn) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("marketSeq", marketSeq);
		param.put("alarmYn", alarmYn);
		sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

}
