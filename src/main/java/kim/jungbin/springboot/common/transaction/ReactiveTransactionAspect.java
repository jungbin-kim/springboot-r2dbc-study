package kim.jungbin.springboot.common.transaction;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

@Aspect
@Component
public class ReactiveTransactionAspect {

    @Autowired
    private TransactionService transactionService;

    @Pointcut("@annotation(kim.jungbin.springboot.common.transaction.ReactiveTransactional)")
    public void reactiveTransactionalAnnotationPointcut() {
        // Pointcut 을 위한 empty method
    }

    @SneakyThrows
    @Around("reactiveTransactionalAnnotationPointcut()")
    public Object aroundReactiveTransaction(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature)pjp.getSignature();

        Class<?> returnType = signature.getReturnType();
        Method method = signature.getMethod();
        boolean readOnly = method.getAnnotation(ReactiveTransactional.class).readOnly();
        if (returnType == Flux.class) {
            return transactionService.transactionFlux((Flux)(pjp.proceed(args)), readOnly);
        } else if (returnType == Mono.class) {
            return transactionService.transactionMono((Mono)(pjp.proceed(args)), readOnly);
        }
        return pjp.proceed(args);
    }

}
