package framework.mybatis.spring.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

/**
 * MyBatis Type Handler
 *
 * Java boolean - DB Y/N
 *
 * [설정방법]
 * sqlSessionFactory 빈에 typeHandlersPackage 프라퍼티등으로 설정
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class BooleanYNTypeHandler extends AbleBaseTypeHandler<Boolean> {

	private Boolean parseBoolean(String value) throws SQLException {
		Boolean result;
		if("Y".equalsIgnoreCase(value) || "T".equalsIgnoreCase(value)) {
			result = true;
		} else if("N".equalsIgnoreCase(value) || "F".equalsIgnoreCase(value)) {
			result = false;
		} else {
			result = null;
		}
		if(logger.isTraceEnabled()) {
			logger.trace("\"" + value + "\" -> " + result);
		}
		return result;
	}

	private String convertToString(Boolean value) {
        String result = (value != null && value.booleanValue()) ? "Y" : "N";
        if (logger.isTraceEnabled()) {
            logger.trace(value + " -> " + "\"" + result + "\"");
        }
        return result;
    }

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, convertToString(parameter));
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return parseBoolean(rs.getString(columnName));
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return parseBoolean(rs.getString(columnIndex));
	}

	@Override
	public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return parseBoolean(cs.getString(columnIndex));
	}

}

