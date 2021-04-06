package framework.mybatis.spring.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

/**
 * MyBatis Type Handler
 *
 * org.joda.time.LocalTime - DB Date
 *
 * [설정방법]
 * sqlSessionFactory 빈에 typeHandlersPackage 프라퍼티등으로 설정
 *
 * @author ovcoimf
 */
public class LocalTimeTypeHandler extends AbleBaseTypeHandler<LocalTime> {

	@Override
	public LocalTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Time time = rs.getTime(columnName);
		if (time == null) {
			return null;
		}
		return new LocalTime(time);
	}

	@Override
	public LocalTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Time time = rs.getTime(columnIndex);
		if (time == null) {
			return null;
		}
		return new LocalTime(time);
	}

	@Override
	public LocalTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Time time = cs.getTime(columnIndex);
		if (time == null) {
			return null;
		}
		return new LocalTime(time);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, LocalTime parameter, JdbcType jdbcType) throws SQLException {
		if (parameter == null) {
			ps.setTime(i, null);
		} else {
			DateTime datetime = new DateTime(1970, 1, 1, parameter.getHourOfDay(), parameter.getMinuteOfHour(),
					parameter.getSecondOfMinute(), 0);
			ps.setTime(i, new Time(datetime.toDate().getTime()));
		}
	}

}
