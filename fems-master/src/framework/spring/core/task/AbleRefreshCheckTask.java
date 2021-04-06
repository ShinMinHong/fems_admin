package framework.spring.core.task;

/**
 * Refresh를 체크하는 Runnable.
 *
 * AbleRefreshCheckable 객체에 체크를 위임
 *
 * @author ByeongDon
 *
 */
public class AbleRefreshCheckTask implements Runnable {

	/**
	 * refresh를 체크할 객체
	 */
	protected AbleRefreshCheckable checkTargetBean;

	/**
	 * 생성자
	 * @param checkTargetBean refresh를 체크할 객체
	 */
	public AbleRefreshCheckTask(AbleRefreshCheckable checkTargetBean) {
		this.checkTargetBean = checkTargetBean;
	}

	/**
	 * AbleRefreshCheckable 객체에 Refresh 체크를 위임
	 */
	@Override
	public void run() {
		checkTargetBean.refreshCheck();
	}
}
