package com.firealarm.admin.biz.system.mngareamng.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.system.mngareamng.vo.MngAreaMngActiveVO;
import com.firealarm.admin.biz.system.mngareamng.vo.MngAreaMngVO;
import com.firealarm.admin.common.support.DAOSupport;

import framework.spring.web.rest.jsonview.AblePageImpl;
import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 관제지역관리 DAO
 * @role ovcoimf
 */
@Repository
public class MngAreaMngDAO extends DAOSupport {

	/** 목록 조회 */
	public List<MngAreaMngVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, MngAreaMngVO.class));
		param.put("search", search);
		List<MngAreaMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 조회 */
	public Page<MngAreaMngVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), MngAreaMngVO.class));
		List<MngAreaMngVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<MngAreaMngVO> page = new AblePageImpl<>(list, pageableToApply, total);
		return page;
	}

	/** 상세 */
	public MngAreaMngVO getByMngAreaSeq(long mngAreaSeq) {
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), mngAreaSeq);
	}

	/** 상세 */
	public MngAreaMngVO getByMngAreaName(String mngAreaName) {
		return sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName(), mngAreaName);
	}

	/** 등록 */
	public int insert(MngAreaMngActiveVO dt) {
		return sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), dt);
	}

	/** 수정 */
	public int update(MngAreaMngActiveVO dt) {
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), dt);
	}

	/** 삭제 */
	public int delete(long mngAreaSeq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mngAreaSeq", mngAreaSeq);
		return sqlSession.delete(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

}