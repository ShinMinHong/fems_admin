package com.firealarm.admin.biz.areasystem.fire119sendlog.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.areasystem.fire119sendlog.vo.Fire119SendLogVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.security.util.UserSecurityUtil;

import framework.util.AbleUtil;
import framework.vo.SearchMap;

/**
 *  119 다매체 발신 관리 DAO
 * @author SMH
 */
@Repository
public class Fire119SendLogDAO extends DAOSupport {

	/** 전체 목록 조회 */
	public List<Fire119SendLogVO> getListAll(Sort sort, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", prepareSortParameter(sort, Fire119SendLogVO.class));
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		List<Fire119SendLogVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
		return list;
	}

	/** 페이지 목록 조회 */
	public Page<Fire119SendLogVO> getList(Pageable pageable, SearchMap search) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		param.put("me", UserSecurityUtil.getCurrentUserDetails());
		int total = sqlSession.selectOne(mapperNamespace + AbleUtil.getCurrentMethodName() + "Count", param);
		Pageable pageableToApply = ensureValidPageable(pageable, total);
		param.put("page", pageableToApply);
		param.put("sort", prepareSortParameter(pageableToApply.getSort(), Fire119SendLogVO.class));
		List<Fire119SendLogVO> list = sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName() + "Page", param);
		Page<Fire119SendLogVO> page = new PageImpl<Fire119SendLogVO>(list, pageableToApply, total);
		return page;
	}
}
