package kim.jungbin.springboot.common.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kim.jungbin.springboot.common.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.*;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.springframework.transaction.TransactionDefinition.*;

public class Transaction {

    @JsonIgnore
    private Deque<TransactionStatus> statusStack = new ArrayDeque<>();

    @JsonIgnore
    @Autowired
    private PlatformTransactionManager transactionManager;

    protected <T> T getBean(Class<T> clazz) {
        return SpringUtil.getBean(clazz);
    }

    public Transaction(boolean readOnly, int isolationLevel) {
        SpringUtil.injectDependency(this);
        beginTransaction(readOnly, isolationLevel);
    }

    protected void beginTransaction(boolean readOnly) {
        beginTransaction(readOnly, ISOLATION_DEFAULT);
    }

    protected void beginTransaction(boolean readOnly, int isolationLevel) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(isolationLevel);
        def.setReadOnly(readOnly);
        statusStack.push(transactionManager.getTransaction(def));
    }

    protected void commitTransaction() {
        TransactionStatus status = statusStack.pop();
        if (status == null) {
            return;
        }

        if (status.isNewTransaction()) {
            try {
                transactionManager.commit(status);
            } catch (TransactionException e) {
                statusStack.push(status);
            }
        }
    }

    protected void rollbackTransaction() {
        TransactionStatus status = statusStack.pop();
        if (status == null) {
            return;
        }

        if (status.isNewTransaction()) {
            transactionManager.rollback(status);
        }
    }

    protected boolean isInTransaction() {
        return !statusStack.isEmpty();
    }
}