package framework.spring.web.log;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging Aspect for every invokation @RequestMapping annotated methods in @Controller annotated beans
 *
 * @author ByeongDon
 */
@Aspect
public class AbleControllerLoggingAspect {

	/** Logger available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controller() { /**/ }

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void restController() { /**/ }

    @Pointcut("execution(* *(..))")
    public void methodPointcut() { /**/ }

    //@Pointcut("within(@org.springframework.web.bind.annotation.RequestMapping *)") //@RequestMapping Annotated Class
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)") //@RequestMapping Annotated Method
    public void requestMapping() { /**/ }

    @Before("(controller() || restController()) && methodPointcut() && requestMapping()")
    public void beforeControllerMethod(JoinPoint joinPoint) {
    	if(logger.isDebugEnabled()) {
    		AbleControllerLoggingPolicy policy = getPolicyAnnotation(joinPoint);
    		if(policy == null || !policy.hideStartEndLog()) {
	        	logger.debug("\r\n\t##################################################"
	        			+ "\r\n\t#### START " + niceNameForStart(policy, joinPoint));
    		}
    	}
    }

    @AfterReturning(pointcut = "(controller() || restController()) && methodPointcut() && requestMapping()", returning="retVal")
    public void afterControllerMethod(JoinPoint joinPoint, Object retVal) {
    	if(logger.isDebugEnabled()) {
    		AbleControllerLoggingPolicy policy = getPolicyAnnotation(joinPoint);
    		if(policy == null || !policy.hideStartEndLog()) {
	    		logger.debug("\r\n\t#### END " + niceNameForEnd(policy, joinPoint, retVal)
	    			+ "\r\n\t##################################################");
    		}
    	}
    }

    @AfterThrowing(pointcut = "(controller() || restController()) && methodPointcut() && requestMapping()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
    	if(logger.isDebugEnabled()) {
    		AbleControllerLoggingPolicy policy = getPolicyAnnotation(joinPoint);
    		if(policy == null || !policy.hideExceptionLog()) {
	    		logger.debug("\r\n\t#### EXCEPTION AT " + niceNameForException(policy, joinPoint, ex)
					+ "\r\n\t##################################################");
    		}
    	}
    }

    private AbleControllerLoggingPolicy getPolicyAnnotation(JoinPoint joinPoint) {
		try {
			if(joinPoint.getSignature() instanceof MethodSignature) {
				MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
				AbleControllerLoggingPolicy policy = methodSignature.getMethod().getAnnotation(AbleControllerLoggingPolicy.class);
				return policy;
			}
		} catch (Exception e) {
			logger.trace("{} - {}", e.getClass().getName(), e.getMessage(), e);
		}
		return null;
	}

	/**
     * joinPoint의 Before 로깅내용 획득
     */
    private String niceNameForStart(AbleControllerLoggingPolicy policy, JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getSimpleName()
                + "#" + joinPoint.getSignature().getName()
                + argsToString(joinPoint.getArgs());
    }

    /**
     * Method Arguments의 로깅내용 획득
     * @param args Arguments
     * @return Arguments 로깅 문자열
     */
    private String argsToString(Object[] args) {
    	StringBuilder sb = new StringBuilder();
    	if(args == null || args.length == 0) {
    		return "\t\tno arguments.";
    	}
    	for (int i = 0; i < args.length; i++) {
    		sb.append(String.format("%n\t####  - args[%d]: %s", i, argToString(args[i])));
		}
    	return sb.toString();
    }

	private String argToString(Object arg) {
		if(arg == null) {
			return "<null>";
		}
		if(arg instanceof HttpServletRequest) {
			return arg.toString() + " - " + getLoggableRequestMap((HttpServletRequest)arg);
		}
		return arg.toString();
	}

    /**
     * joinPoint의 AfterReturning 로깅내용 획득
     */
    private String niceNameForEnd(AbleControllerLoggingPolicy policy, JoinPoint joinPoint, Object retVal) {
        return joinPoint.getTarget().getClass().getSimpleName()
                + "#" + joinPoint.getSignature().getName()
                + argsToString(joinPoint.getArgs())
                + retValToString(policy, joinPoint, retVal);
    }

	private String retValToString(AbleControllerLoggingPolicy policy, JoinPoint jointPoint, Object retVal) {
		if(policy != null && policy.hideResponseBody()) {
			return "\r\n\t####  - return: <SKIP>";
		}
		return "\r\n\t####  - return: " + ((null==retVal) ? "" : retVal.toString());
	}

    /**
     * joinPoint의 AfterThrowing 로깅내용 획득
     */
    private String niceNameForException(AbleControllerLoggingPolicy policy, JoinPoint joinPoint, Throwable ex) {
    	return joinPoint.getTarget().getClass().getSimpleName()
                + "#" + joinPoint.getSignature().getName()
                + argsToString(joinPoint.getArgs())
                + exceptionToString(ex);
	}

	private String exceptionToString(Throwable ex) {
		return "\r\n\t####  - EXCEPTION : " + ex.toString();
	}

	/**
	 * 요청객체의 파라미터를 로깅이 가능하도록 Map<String, String> 형태로 반환
	 *
	 * request.getParameterMap()은 Map<String, String[]>인데 Map<String, String>으로 로깅이 가능하게 변환하여 반환
	 *
	 * @param request 요청 객체
	 * @return Map<String, String> 형태의 요청 파라미터맵
	 */
	private Map<String, String> getLoggableRequestMap(ServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		String key;
		String[] value;
		for(Entry<String, String[]> item : parameterMap.entrySet()) {
			key = item.getKey();
			value = item.getValue();
			result.put(key, Arrays.asList(value).toString());
		}
		return result;
	}

}
