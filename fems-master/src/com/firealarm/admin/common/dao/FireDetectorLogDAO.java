package com.firealarm.admin.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.firealarm.admin.biz.home.vo.MapFireDetectorEventLogVO;
import com.firealarm.admin.common.support.DAOSupport;
import com.firealarm.admin.common.vo.FireDetectorLogDT;

import framework.util.AbleUtil;

@Repository
public class FireDetectorLogDAO extends DAOSupport {

	public List<MapFireDetectorEventLogVO> getRecentListsByDeviceSeqAndStoreSeq(long detector, long storeSeq) {
		Map<String, Object> param = new HashMap<>();
		param.put("detector", detector);
		param.put("storeSeq", storeSeq);
		return sqlSession.selectList(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}

	/**
	 * 신호목록을 fa_fire_detector_log에 Insert All
	 */
	public int insertAll(List<FireDetectorLogDT> list) {
		Map<String, Object> param = new HashMap<>();
		param.put("list", list);
		return sqlSession.insert(mapperNamespace + AbleUtil.getCurrentMethodName(), param);
	}
}
