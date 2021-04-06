package framework.mybatis.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.Resource;

/**
 * RefreshableSqlSessionFactoryBean:
 * 		SqlSessionFactoryBean의 확장. Mapper XML의 수정을 체크하고 있다가, Refresh를 처리
 *
 * mapperRefreshInterval 빈 속성 - Mapper XML의 변경 체크 타이머 주기 (ms) (기본값:500)
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class RefreshableSqlSessionFactoryBean extends SqlSessionFactoryBean implements DisposableBean {
	private static Logger logger = LoggerFactory.getLogger(RefreshableSqlSessionFactoryBean.class);

	/** 부모 SqlSessionFactoryBean의 SqlSessionFactory의 Proxy */
	private SqlSessionFactory proxy;
	/** 감시대상 MapperLocation */
	private Resource[] mapperLocations;

	/** Mapper를 감시하는 타이머가 동작중인지 여부 */
	private boolean running = false;
	private Timer timer;
	private TimerTask timerTask;
	private int mapperRefreshInterval = 500;

	/** 동기화 객체 */
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();

	@Override
	public void setMapperLocations(Resource[] mapperLocations) {
		super.setMapperLocations(mapperLocations);
		this.mapperLocations = mapperLocations;
	}

	/**
	 * Mapper를 감시하는 타이머 Interval 설정 (Bean Property 설정용)
	 * @param interval Interval
	 */
	public void setMapperRefreshInterval(int mapperRefreshInterval) {
		this.mapperRefreshInterval = mapperRefreshInterval;
	}

	/**
	 * SqlSessionFactoryBean을 Refresh 처리
	 */
	public void refresh() throws Exception {
		if(logger.isInfoEnabled()) {
			logger.info("SQL Mapper changed. refreshing sqlMapClient.");
		}
		w.lock();
		try {
			super.afterPropertiesSet();
		} finally {
			w.unlock();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		setRefreshable();
	}

	/**
	 * 내부적으로 SqlSessionFactory의 Proxy를 생성하고, Timer를 기동
	 */
	private void setRefreshable() {
		proxy = (SqlSessionFactory) Proxy.newProxyInstance(
					SqlSessionFactory.class.getClassLoader(),
					new Class[] { SqlSessionFactory.class },
					new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							return method.invoke(getParentObject(), args);
						}
					});

		timerTask = new TimerTask() {

			private Map<Resource, Long> resourceLastModifiedMap = new HashMap<Resource, Long>();

			@Override
			public void run() {
				if(isModified()) {
					try {
						refresh();
					} catch (Exception e) {
						logger.error("caught exception.", e);
					}
				}
			}

			/**
			 * XML Mapper중 변경된 것이 있는지 확인
			 */
			private boolean isModified() {
				boolean modified = false;
				if(mapperLocations != null) {
					for (Resource mapperLocation : mapperLocations) {
						modified |= processResourceMidifiedCheck(mapperLocation);
					}
				}
				return modified;
			}


			/**
			 * XML Mapper가 변경되었는지 LastModified 시간정보로 체크하고, 정보를 Map에 캐싱
			 */
			private boolean processResourceMidifiedCheck(Resource resource) {
				boolean isModified = false;
				List<String> modifiedResources = new ArrayList<String>();
				try {
					long lastModified = resource.lastModified();
					if(resourceLastModifiedMap.containsKey(resource)) {
						long originalLastModified = ((Long) resourceLastModifiedMap.get(resource)).longValue();
						if(lastModified != originalLastModified) {
							resourceLastModifiedMap.put(resource, Long.valueOf(lastModified));
							modifiedResources.add(resource.getDescription());
							isModified = true;
						}
					} else {
						resourceLastModifiedMap.put(resource, Long.valueOf(lastModified));
					}
				} catch (Exception e) {
					logger.error("caught exception.", e);
				}

				if(isModified) {
					if(logger.isInfoEnabled()) {
						logger.info("modified SQL Mapper files : " + modifiedResources);
					}
				}
				return isModified;
			}
		};

		timer = new Timer(true);

		//타이머 시작
		startTimerTask();
	}

	/**
	 * 읽기 Lock을 걸고 부모 SqlSessionFactoryBean의 SqlSessionFactory를 획득
	 * @return SqlSessionFactory
	 */
	private Object getParentObject() throws Exception {
		r.lock();
		try {
			return super.getObject();
		} finally {
			r.unlock();
		}
	}

	@Override
	public SqlSessionFactory getObject() {
		return this.proxy;
	}

	@Override
	public Class<? extends SqlSessionFactory> getObjectType() {
		return (this.proxy != null ? this.proxy.getClass() : SqlSessionFactory.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * SQL Mapper XML을 모니터링 하는 타이머 주기 설정 및 실행중이였던 경우 타이머 재 시작
	 * @param interval Timer Interval (ms)
	 */
	public void resetRefreshInterval(int mapperRefreshInterval) {
		this.mapperRefreshInterval = mapperRefreshInterval;
		if(timer != null ) {
			startTimerTask();
		}
	}

	/**
	 * SQL Mapper XML을 모니터링 하는 타이머 시작/재시작 (단, Interval이 양수인 경우)
	 */
	private void startTimerTask() {

		if(this.running) {
			timer.cancel();
			running = false;
		}

		if(this.mapperRefreshInterval > 0) {
			timer.schedule(timerTask, 0, this.mapperRefreshInterval);
			running = true;
		}
	}

	public void destroy() {
		timer.cancel();
	}
}
