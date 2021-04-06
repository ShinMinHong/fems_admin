package framework.spring.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;

import framework.exception.AbleConfigurationException;
import framework.mybatis.spring.support.AbleSqlSessionDaoSupport;
import framework.spring.core.task.AbleIntervalCallable;
import framework.spring.core.task.AbleIntervalTask;
import framework.util.AbleHostingUtil;

/**
 * MyBatis의 프로시져를 주기적으로 호출하기 위한 Job
 *
 * <pre>
 * [실행방법]
 *
 * 특정 MyBatis 구문을 주기적으로 실행. 주로 DB Procedure의 Batch 실행
 *
 * &lt;bean id="taskBatchJob1" class="com.ablecoms.framework.spring.job.AbleMyBatisProcedureJob"&gt;
 *  &lt;constructor-arg name="0" value="was1"/&gt;
 *  &lt;constructor-arg name="1" value="persistence.BatchJobDAO.eslrpcDummyTest"/&gt;
 *  &lt;constructor-arg name="2" ref="executor"/&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AbleMyBatisProcedureJob extends AbleSqlSessionDaoSupport implements InitializingBean, AbleIntervalCallable {

	protected final Logger logger = LoggerFactory.getLogger(AbleMyBatisProcedureJob.class);

	/** 배치를 실행할 InstanceName (없거나 null이여도 실행) */
	protected String instanceNameToRunning;

	/** 실행할 MyBatis Statement */
	private String statement;

	/** 로직 반복실행에 사용할 TaskExecutor */
	protected TaskExecutor taskExecutor;

	/**
	 * 생성자
	 * @param instanceNameToRunning 실행할 서버 WasContainerName (설정시 해당 WasContainer의 인스턴스만 실행됨. WAS Pool에서 특정 WAS에서만 실행하는 기능)
	 * @param statementName 주기적으로 실행할 MyBatis Statement Name
	 * @param taskExecutor Job실행에 사용할 taskExecutor
	 */
	public AbleMyBatisProcedureJob(String instanceNameToRunning, String statement, TaskExecutor taskExecutor) {
		super();
		this.instanceNameToRunning = instanceNameToRunning;
		this.statement = statement;
		this.taskExecutor = taskExecutor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if(StringUtils.isEmpty(this.statement)) {
			throw new AbleConfigurationException("AbleMyBatisProcedureJob.statement is required");
		}
		if(this.taskExecutor == null) {
			throw new AbleConfigurationException("AbleMyBatisProcedureJob.taskExecutor is required");
		}
	}

	/* (non-Javadoc)
	 * @see com.ablecoms.framework.spring.core.task.AbleIntervalCallable#runIntervalProcessTask()
	 */
	public void runIntervalProcessTask() {
		if(taskExecutor == null) {
			logger.debug("taskExecutor configuration required.");
			return;
		}

		String currentInstanceName = AbleHostingUtil.getWasContainerName();
		if(StringUtils.isNotBlank(this.instanceNameToRunning)) {
			if(currentInstanceName == null || !this.instanceNameToRunning.equals(currentInstanceName)) {
				logger.trace("#### SKIP BATCH #### instanceNameToRunning:{}, currentInstanceName:{}", instanceNameToRunning, currentInstanceName);
				return;
			}
		}
		logger.trace("#### START BATCH #### instanceNameToRunning:{}, currentInstanceName:{}", instanceNameToRunning, currentInstanceName);

		taskExecutor.execute(new AbleIntervalTask(this));
	}

	/**
	 * 설정된 MyBatis statement를 실행
	 * @see com.ablecoms.framework.spring.core.task.AbleIntervalCallable#doIntervalProcess()
	 */
	public void doIntervalProcess() {
		if(logger.isDebugEnabled()) { logger.debug("SQL ===>[" + statement + "]"); }
		Map<String, Object> map = new HashMap<String, Object>();
		getSqlSession().update(this.statement, map);
	}

}
