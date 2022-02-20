package kim.jungbin.springboot.common.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class TransactionService {
    private Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    public <T> Mono<T> transactionMono(Mono<T> source, boolean readOnly) {
        String transactionId = UUID.randomUUID().toString();
        return Mono.fromCallable(() -> beginTransaction(readOnly, TransactionDefinition.ISOLATION_DEFAULT, transactionId))
            .then(source)
            .doOnSuccess(any -> {
                commit(transactionId);
            })
            .doOnError(e -> {
                rollback(transactionId);
            });
    }

    public <T> Flux<T> transactionFlux(Flux<T> source, boolean readOnly) {
        String transactionId = UUID.randomUUID().toString();
        return Mono.fromCallable(() -> beginTransaction(readOnly, TransactionDefinition.ISOLATION_DEFAULT, transactionId))
            .thenMany(source)
            .doOnComplete(() -> {
                commit(transactionId);
            })
            .doOnError(e -> {
                rollback(transactionId);
            });
    }

    private Transaction beginTransaction(boolean readOnly, int isolationLevel, String transactionId) {

        Transaction transaction = transactions.get(transactionId);
        if (transaction == null) {
            transaction = new Transaction(readOnly, isolationLevel);
            transactions.put(transactionId, transaction);
        } else {
            transaction.beginTransaction(readOnly);
        }

        return transaction;
    }

    private void commit(String transactionId) {
        doAndRemove(Transaction::commitTransaction, transactionId);
    }

    private void rollback(String transactionId) {
        doAndRemove(Transaction::rollbackTransaction, transactionId);
    }

    private void doAndRemove(Consumer<Transaction> consumer, String transactionId) {
        Optional.ofNullable(transactions.get(transactionId)).ifPresent(transaction -> {
            consumer.accept(transaction);
            if (transaction.isInTransaction()) {
                return;
            }

            transactions.remove(transactionId);
        });
    }

}