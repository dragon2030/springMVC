
package com.bigDragon.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 日志记录切面类
 */
@Component
@Aspect
public class LogAspect {

    // 获取日志对象
//    private final Logger logger_biz = LoggerFactory.getLogger( "logger_biz" );
//    private final Logger logger_exp = LoggerFactory.getLogger( "logger_exp" );
    //本地异常日志记录对象
    private  static  final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // 保存方法开始执行时间
    private final ThreadLocal<Long> tlo = new ThreadLocal<>();

    // 匹配所有方法
    //@Pointcut("(execution(* *(..)))")
    @Pointcut("(execution( * com.bigDragon..*.controller..*.*(..)) || execution(* com.bigDragon..*.service..*.*(..)))")
    public void pointcut() {
    }

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("pointcut()")
    public void beforMethod(JoinPoint joinPoint){
//    	try{
//	        String methodName = joinPoint.getSignature().getName();
//	        List<Object> args = Arrays.asList(joinPoint.getArgs());
//	        logger_biz.info("this method "+methodName+" begin. param<"+ args+">");
//    	}catch(Exception e){
//    		logger_exp.error("前置通知异常"+e);
//    	}
        try {
            //*========控制台输出=========*//
            logger.info("=====前置通知开始=====");
            logger.info("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            logger.info("请求参数:" + Arrays.asList(joinPoint.getArgs()));
            logger.info("=====前置通知结束=====");
        }  catch (Exception e) {
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }
    /**
     * 后置通知（无论方法是否发生异常都会执行,所以访问不到方法的返回值）
     * @param joinPoint
     */
//    @After("pointcut()")
//    public void afterMethod(JoinPoint joinPoint){
//    	try{
//	        String methodName = joinPoint.getSignature().getName();
//	        logger_biz.info("this method "+methodName+" end.");
//    	}catch(Exception e){
//    		logger_exp.error("后置通知异常"+e);
//    	}
//    }
    /**
     * 返回通知（在方法正常结束执行的代码）
     * 返回通知可以访问到方法的返回值！
     * @param joinPoint
     */
    @AfterReturning(value="pointcut()",returning="result")
    public void afterReturnMethod(JoinPoint joinPoint,Object result){
//    	try{
//	        String methodName = joinPoint.getSignature().getName();
//	        logger_biz.info("this method "+methodName+" end.result<"+result+">");
//    	}catch(Exception e){
//    		logger_exp.error("返回通知异常"+e);
//    	}
        try{
            String methodName = joinPoint.getSignature().getName();
            logger.info("=====返回通知开始=====");
            logger.info("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            logger.info("返回结果:" + result);
            logger.info("=====返回通知结束=====");
        }catch(Exception e){
            logger.error("返回通知异常" + e);
        }
    }
    /**
     * 异常通知（方法发生异常执行的代码）
     * 可以访问到异常对象；且可以指定在出现特定异常时执行的代码
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value="pointcut()",throwing="e")
    public void afterThrowingMethod(JoinPoint joinPoint,Exception e){
//    	try{
//	        String methodName = joinPoint.getSignature().getName();
//	        logger_exp.error("this method "+methodName+" end.ex message<"+ex+">");
//    	}catch(Exception e){
//    		logger_exp.error("异常通知异常"+e);
//    	}
        try {
            /*========控制台输出=========*/
            logger.info("=====异常通知开始=====");
            logger.info("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            logger.info("异常代码:" + e.getClass().getName());
            logger.info("异常信息:" + e.getMessage());
            logger.info("=====异常通知结束=====");
        }  catch (Exception ex) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
    }
    /**
     * 环绕通知(需要携带类型为ProceedingJoinPoint类型的参数)
     * 环绕通知包含前置、后置、返回、异常通知；ProceedingJoinPoint 类型的参数可以决定是否执行目标方法
     * 且环绕通知必须有返回值，返回值即目标方法的返回值
     * @param point
     */
    /*@Around(value="pointcut()")
    public Object aroundMethod(ProceedingJoinPoint point){

        Object result = null;
        String methodName = point.getSignature().getName();
        try {
            //前置通知
        	logger_biz.info("The method "+ methodName+" start. param<"+ ArrayTest.asList(point.getArgs())+">");
            //执行目标方法
            result = point.proceed();
            //返回通知
            logger_biz.info("The method "+ methodName+" end. result<"+ result+">");
        } catch (Throwable e) {
            //异常通知
        	logger_exp.error("环绕通知异常 "+methodName+" end.ex message<"+e+">");
            throw new RuntimeException(e);
        }
        //后置通知
        logger_biz.info("The method "+ methodName+" end.");
        return result;
    }*/

}
