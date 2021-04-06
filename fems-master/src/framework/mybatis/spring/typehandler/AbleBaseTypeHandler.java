package framework.mybatis.spring.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MyBatis BaseTypeHandler
 * 
 * @author ByeongDon
 */
public abstract class AbleBaseTypeHandler<T> extends BaseTypeHandler<T> {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
}