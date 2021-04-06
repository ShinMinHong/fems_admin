package framework.spring.core.task;

/**
 * Job을 호출 할 수 있는 인터페이스
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
@FunctionalInterface
public interface AbleJobCallable {

	/**
	 * Interval 로직 처리
	 */
	void doIntervalProcess();
}