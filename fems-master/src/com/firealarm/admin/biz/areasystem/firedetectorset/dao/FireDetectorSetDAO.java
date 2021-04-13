package com.firealarm.admin.biz.areasystem.firedetectorset.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetVO;
import com.firealarm.admin.biz.areasystem.firedetectorset.vo.FireDetectorSetActiveVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 * 화재감지기 설정 관리 DAO
 * @author SMH
 *
 */
@Repository
public class FireDetectorSetDAO extends DAOSupport {

	/** 전체 목록 조회 */
	public List<FireDetectorSetVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, FireDetectorSetVO.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		List<FireDetectorSetVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 목록 조회 */
	public Page<FireDetectorSetVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), FireDetectorSetVO.class));
		List<FireDetectorSetVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<FireDetectorSetVO> page = new PageImpl<FireDetectorSetVO>(list, pageableToApply, total);
		return page;
	}

	/** 화재감지기 설정 상세 */
	public FireDetectorSetVO getByFireDetectorSetSeq(long fireDetectorSetSeq) {
		return sqlSession.selectOne(mapperNamespace  + AbleUtil.getCurrentMethodName(), fireDetectorSetSeq);
	}

	/** 화재감지기설정 등록 */
	public long insert(FireDetectorSetActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return vo.getFireDetectorSetSeq();
	}

	/** 화재감지기 설정 수정 */
	public int update(FireDetectorSetActiveVO vo) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", vo);
		return sqlSession.update(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/** 화재감지기 설정 삭제 */
	public int delete(long fireDetectorSetSeq) {
		return sqlSession.delete(mapperNamespace + AbleUtil.getCurrentMethodName(), fireDetectorSetSeq);
	}
}