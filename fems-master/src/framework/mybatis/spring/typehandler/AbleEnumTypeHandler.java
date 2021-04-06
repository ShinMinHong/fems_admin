package framework.mybatis.spring.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

import framework.type.AbleValueEnum;
import framework.util.AbleEnumUtil;

/**
 * MyBatis Enum Type Handler
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 *
 * @param <E> Enum 타입
 */
public class AbleEnumTypeHandler<E extends Enum<E>> extends AbleBaseTypeHandler<E> {

	private Class<E> type;
	private final E[] enums;

	public AbleEnumTypeHandler(Class<E> type) {
		this.type = type;
		this.enums = type.getEnumConstants();
		if (this.enums == null) {
			throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
		}
	}

	private E parseToValue(String value) throws SQLException {
		return AbleEnumUtil.parseEnumValueOf(type, value);
	}

	private String convertToString(Enum<E> param) {
		String result = (param == null) ? "" : param.toString();
		if(param instanceof AbleValueEnum<?>) {
			AbleValueEnum<?> typedParam = (AbleValueEnum<?>)param;
			result = (typedParam.getValue() == null) ? "" : typedParam.getValue().toString();
		}
        if (logger.isTraceEnabled()) {
            logger.trace(param + " -> " + "\"" + result + "\"");
        }
        return result;
    }

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, convertToString(parameter));
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return parseToValue(rs.getString(columnName));
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return parseToValue(rs.getString(columnIndex));
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return parseToValue(cs.getString(columnIndex));
	}


}
