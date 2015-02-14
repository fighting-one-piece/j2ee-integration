package org.platform.utils.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	private Logger LOG = LoggerFactory.getLogger(LogAspect.class);

	private static final String EXECUTION = "execution(* org.platform.modules.*.*.impl.*.*(..))";

	@Before(EXECUTION)
	public void logBefore(JoinPoint joinPoint){
		LOG.info("------Log Before Method------" + joinPoint.getSignature().getName());
	}

	@After(EXECUTION)
	public void logAfter(JoinPoint joinPoint){
		LOG.info("------Log After Method------" + joinPoint.getSignature().getName());
	}

	@AfterReturning(pointcut = EXECUTION, returning = "result")
	public void logAfterReturn(JoinPoint joinPoint, Object result) {
		LOG.info("------Log After Returning Method------" + joinPoint.getSignature().getName());
		LOG.info("------Log After Returning Method Return Value------" + result);
	}

	@AfterThrowing(pointcut = EXECUTION, throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception){
		LOG.info("------Log After Throwing Method------" + joinPoint.getSignature().getName());
		LOG.info("------Log After Throwing Method Exception------" + exception.getMessage());
	}

	@Around(EXECUTION)
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		LOG.info("------Log Around Method------" + proceedingJoinPoint.getSignature().getName());
		return proceedingJoinPoint.proceed();
	}


}
