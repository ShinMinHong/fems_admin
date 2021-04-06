package framework.spring.core.task;

/**
 * Interval Job 프로세스 인터페이스
 * 
 * @author Min ByeongDon <deepfree@gmail.com>
 *
 */
public interface AbleIntervalCallable extends AbleJobCallable {

	/**
	 * Interval 로직 처리 
	 */
	void doIntervalProcess();
	
	/**
	 * refresh 체크를 위한 Task를 시작 (AbleRefreshCheckTask) 
	 */
	void runIntervalProcessTask();
}
