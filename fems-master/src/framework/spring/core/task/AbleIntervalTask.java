package framework.spring.core.task;

/**
 * Interval 프로세스를 실행하는 Runnable Task
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 *
 */
public class AbleIntervalTask implements Runnable {

	/**
	 * 주기적으로 로직을 처리하기 위한 객체
	 */
	protected AbleIntervalCallable intervalProcessBean;

	/**
	 * 생성자
	 * @param intervalProcessBean 주기적으로 로직을 처리하기 위한 객체
	 */
	public AbleIntervalTask(AbleIntervalCallable intervalProcessBean) {
		this.intervalProcessBean = intervalProcessBean;
	}

	/**
	 * AbleIntervalCallable객체의 처리 로직을 실행
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		intervalProcessBean.doIntervalProcess();
	}

}
