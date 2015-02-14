package org.platform.utils.exception;

import java.util.concurrent.TimeoutException;

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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

	private static final String EXECUTION = "execution(* org.platform.modules.*.business.impl.*.*(..))";

	@Before(EXECUTION)
	public void logBefore(JoinPoint joinPoint){
	}

	@After(EXECUTION)
	public void logAfter(JoinPoint joinPoint){
	}

	@AfterReturning(pointcut = EXECUTION, returning = "result")
	public void logAfterReturn(JoinPoint joinPoint, Object result) {
	}

	@AfterThrowing(pointcut = EXECUTION, throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception){
		LOG.info("------Log After Throwing Method------" + joinPoint.getSignature().getName());
		if (exception instanceof DataAccessException) {
			LOG.error(exception.getMessage(), exception);
			throw new BusinessException(ExceptionCode.DATABASE_ERROR, "数据异常或操作失败");
		}
		if (exception instanceof TimeoutException) {
			LOG.error(exception.getMessage(), exception);
			throw new BusinessException(ExceptionCode.DATABASE_ERROR, "数据异常或操作失败");
		}
	}

	@Around(EXECUTION)
	public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
//		logger.info("------Log Around Method------" + proceedingJoinPoint.getSignature().getName());
//		logger.info("------Log Around Method Arguments------" + Arrays.toString(proceedingJoinPoint.getArgs()));
		return proceedingJoinPoint.proceed();
	}


}
