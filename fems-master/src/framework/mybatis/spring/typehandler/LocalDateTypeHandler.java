package framework.mybatis.spring.typehandler;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.joda.time.LocalDate;

/**
 * MyBatis Type Handler
 *
 * org.joda.time.LocalDateTime - DB Date
 *
 * [설정방법]
 * sqlSessionFactory 빈에 typeHandlersPackage 프라퍼티등으로 설정
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class LocalDateTypeHandler extends AbleBaseTypeHandler<LocalDate> {

	@Override
	public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Date date = rs.getDate(columnName);
		if (date == null) {
			return null;
		}
		return new LocalDate(date);
	}

	@Override
	public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Date date = rs.getDate(columnIndex);
		if (date == null) {
			return null;
		}
		return new LocalDate(date);
	}

	@Override
	public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
	    Date date = cs.getDate(columnIndex);
		if (date == null) {
			return null;
		}
		return new LocalDate(date);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
		if (parameter == null) {
			ps.setDate(i, null);
		} else {
			ps.setDate(i, new Date(parameter.toDateTimeAtStartOfDay().toDate().getTime()));
		}
	}

}

