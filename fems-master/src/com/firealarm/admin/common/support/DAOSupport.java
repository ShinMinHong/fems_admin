package com.firealarm.admin.common.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.google.common.base.CaseFormat;

import framework.exception.AbleRuntimeException;
import com.firealarm.admin.appconfig.AppConfig;
import com.firealarm.admin.appconfig.AppConst;

/**
 * Repository Class 의 공통
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class DAOSupport extends ComponentSupport {

	/** 서버 Properties 설정 접근자 */
	@Autowired protected AppConfig appConfig;

	/** MessageSource 접근자 */
	@Autowired protected MessageSource messageSource;

	/** MyBatis SqlSession */
	@Autowired protected SqlSession sqlSession;

	/** Mapper namespace */
	protected String mapperNamespace = this.getClass().getName() + ".";

	/**
	 * Pageable 객체에서 MyBatis RowBounds 획득
	 */
    protected RowBounds getRowBounds(Pageable pageable) {
        RowBounds bounds = RowBounds.DEFAULT;
        if (null != pageable) {
            bounds = new RowBounds(pageable.getOffset(), pageable.getPageSize());
        }
        return bounds;
    }

    /**
     * Sort 객체의 toString값 (Field: Direction,Field: Direction)으로 쿼리의 Order By에 전달할 수 있게 포매팅
     * @param sort {@link Sort} 객체
     * @return Order By에 연결할 수 있는 문자열
     */
    protected String prepareSortParameter(Sort sort, Class<?> voClass) {
    	if(sort == null) {
    		return null;
    	}

    	List<String> filterPropertyNames = getPropertyNames(voClass);

    	StringBuilder sb = null;
    	for (Order order : sort) {
        	String orderProperty = order.getProperty();
        	//Skip Unknown Property
        	orderProperty = convertToKnownPropertyName(order, filterPropertyNames);
    		if(StringUtils.isEmpty(orderProperty)) {
    			continue;
    		}

        	Direction direction = (order.getDirection()==null) ? Sort.DEFAULT_DIRECTION : order.getDirection();

        	//Add order
        	if(sb == null) {
        		sb = new StringBuilder(); //first
        	} else {
        		sb.append(", "); //second~
        	}
        	sb.append(orderProperty + " " + direction);
		}
		return (sb == null) ? null : sb.toString();
    }

    /** 주어진 클래스의 Property명 목록 획득 */
    private List<String> getPropertyNames(Class<?> voClass) {
    	List<String> propertyNames = new ArrayList<String>();

    	//2016.03.31 박준성 수정
    	//boolean type의 필드명이 is로 시작할 경우 BeanUtils.getPropertyDescriptors에서 is를 제거한 필드명을 획득하여
    	//Grid sorting시 에러발생 (DB컬럼명과 매칭되는 필드명을 찾을 수 없어 sorting이 발생하지 않음)
    	//boolean type의 경우 is를 강제적으로 붙여주는 처리를 하려했으나
    	//DB컬럼명과의 정확한 매칭을 장담할 수 없어 reflection으로 DT의 필드명을 그대로 가져오도록 처리
    	/*
    	PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(voClass);
    	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
    		String name = propertyDescriptor.getName();
    		//필드가 boolean type이며, is로 시작하지 않는 필드명 일 경우 is를 붙여줌
    		Class<?> type = propertyDescriptor.getPropertyType();
    		if(( type == boolean.class || type == Boolean.class ) && name.startsWith("is") ) {
    			name = "is" + StringUtils.capitalize(name);
    		}
    		propertyNames.add(name);
    	}
    	*/
    	Field[] declaredFields = voClass.getDeclaredFields();
    	for (Field field : declaredFields) {
    		propertyNames.add(field.getName());
		}
    	//기본 DT를 확장하여 superClass를 가지는 경우 superClass의 필드명도 가져오기
    	Field[] declaredSuperFields = voClass.getSuperclass().getDeclaredFields();
    	if( declaredSuperFields != null ) {
    		for (Field field : declaredSuperFields) {
        		propertyNames.add(field.getName());
    		}
    	}
    	return propertyNames;
    }

    /** 주어진 Order가 VO에 적용가능한지 확인하여 올바른 속성명 반환 */
    protected String convertToKnownPropertyName(Order order, Class<?> voClass) {
    	List<String> knownPropertyNames = getPropertyNames(voClass);
    	return convertToKnownPropertyName(order, knownPropertyNames);
    }

    /** 주어진 Order가 주어진 Property 목록에 있는지 확인하여 올바른 속성명 반환 */
    protected String convertToKnownPropertyName(Order order, List<String> knownPropertyNames) {
    	String orderProperty = order.getProperty();

    	//CamelCase to UnderscoreCase
    	boolean applyCamelToUnderscore = true;

    	//check order property filter
    	for (String filterProperty : knownPropertyNames) {
			if(filterProperty.equalsIgnoreCase(orderProperty)) {
				if(applyCamelToUnderscore) {
		    		//myName => MY_NAME
		    		return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, filterProperty);
		    	} else {
		    		return filterProperty;
		    	}
			}
		}
		return null;
    }

	/**
	 * Pageable객체의 page와 size를 제한
	 * (page>=0, size>=1, 최대size제한)
	 * @param pageable Pagination 정보 객체
	 * @return 보정된 Pageable 객체
	 */
	protected Pageable ensureValidPageable(Pageable pageable, int total) {
		if (pageable == null) {
			return null;
		}
		if (pageable instanceof PageRequest) {
			int page = pageable.getPageNumber();
			int size = pageable.getPageSize();
			Sort sort = pageable.getSort();
			//size
			if (size > AppConst.PAGE_SIZE_LIMIT) {
				size = AppConst.PAGE_SIZE_LIMIT; // 최대 페이지 제한
			} else if (size < 1) {
				size = 10; // 기본 페이지당 표시 갯수
			}
			//page
			int totalPage = (size == 0) ? 1 : (int) Math.ceil((double) total / (double) size);
			if (page <= 0) {
				page = 0; //zero-based index
			} else if (page >= totalPage) {
				page = Math.max(totalPage - 1, 0);
			}
			Pageable newPageable = new PageRequest(page, size, sort);
			return newPageable;
		} else {
			throw new AbleRuntimeException("Unknown Pageable Type. type: " + pageable.getClass().getSimpleName());
		}
	}

}
