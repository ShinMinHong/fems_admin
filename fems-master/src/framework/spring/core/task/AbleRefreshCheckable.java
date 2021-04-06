package framework.spring.core.task;

/**
 * Refresh Check를 지원하는 Interface
 * 
 * @author ByeongDon
 */
public interface AbleRefreshCheckable {

	/**
	 * refresh를 체크 
	 */
	void refreshCheck();

	
	/**
	 * refresh 체크를 위한 Task를 시작 (AbleRefreshCheckTask) 
	 */
	void runRefreshCheckTask();
	
}
