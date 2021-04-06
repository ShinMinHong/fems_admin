package framework.type;

/**
 * 내부값을 가지는 Labeled Enum이 지원할 기본 인터페이스
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 * @param <T> Labeled Enum의 값 타입
 */
@FunctionalInterface
public interface AbleValueEnum<T> {
	T getValue();
}

