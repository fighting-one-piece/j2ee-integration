package org.platform.utils.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	private Logger logger = Logger.getLogger(getClass());

	private static final String execution = "execution(* platform.modules.*.*.impl.*.*(..))";

	@Before(execution)
	public void logBefore(JoinPoint joinPoint){
		logger.info("------Log Before Method------" + joinPoint.getSignature().getName());
	}

	@After(execution)
	public void logAfter(JoinPoint joinPoint){
		logger.info("------Log After Method------" + joinPoint.getSignature().getName());
	}

	@AfterReturning(pointcut = execution, returning = "result")
	public void logAfterReturn(JoinPoint joinPoint, Object result) {
		logger.info("------Log After Returning Method------" + joinPoint.getSignature().getName());
		logger.info("------Log After Returning Method Return Value------" + result);
	}

	@AfterThrowing(pointcut = execution, throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception){
		logger.info("------Log After Throwing Method------" + joinPoint.getSignature().getName());
		logger.info("------Log After Throwing Method Exception------" + exception.getMessage());
	}

//	@Around(execution)
//	public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
//		logger.info("------Log Around Method------" + proceedingJoinPoint.getSignature().getName());
//		logger.info("------Log Around Method Arguments------" + Arrays.toString(proceedingJoinPoint.getArgs()));
//		logger.info("------Log Around Method Begin------");
//		proceedingJoinPoint.proceed();
//		logger.info("------Log Around Method End------");
//	}


}
