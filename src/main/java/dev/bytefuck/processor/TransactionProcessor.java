package dev.bytefuck.processor;

import dev.bytefuck.exception.ApiException;
import dev.bytefuck.http.TonHttp;
import dev.bytefuck.model.TransactionModel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class TransactionProcessor implements Runnable, Closeable {
    private final TonHttp httpClient;
    private final Semaphore semaphore = new Semaphore(10);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @SneakyThrows
    public void processTransactions(String address) {
        String lastTransactionHash = null;
        String lastTransactionLt = null;

        while (true) {
            semaphore.acquire();

            String finalLastTransactionHash = lastTransactionHash;
            String finalLastTransactionLt = lastTransactionLt;

            List<TransactionModel> transactions;

            try {
                transactions = httpClient.getTransactionsByAddress(
                    address,
                    finalLastTransactionHash,
                    finalLastTransactionLt
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ApiException e) {
                LOGGER.error("Error while getting transactions: {}.", e.getMessage());
                continue;
            }

            if (transactions.isEmpty())
                break;

            transactions.parallelStream().forEach(transaction -> {
                var txText = transaction.getInMsg().getMessage();

                if (txText == null) return;

                if (txText.contains("upgrade")) {
                    var matcher = USERNAME_PATTERN.matcher(txText);

                    if (matcher.find()) {
                        String match = "@" + matcher.group(1);

                        try (FileWriter writer = new FileWriter("bot_usernames.txt", true)) {
                            writer.write(match + System.lineSeparator());
                        } catch (IOException e) {
                            LOGGER.error("Error while writing to file: {}.", e.getMessage());
                            return;
                        }

                        LOGGER.info("Bot's Premium username {} has been found and written to file.", match);
                    }
                }
            });

            TransactionModel lastTransaction = transactions.get(transactions.size() - 1);
            lastTransactionHash = lastTransaction.getTransactionId().getHash();
            lastTransactionLt = lastTransaction.getTransactionId().getLt();
        }
    }

    @Override
    public void run() {
        semaphore.release(10);
    }

    public void init() {
        scheduler.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProcessor.class);

    private static final Pattern USERNAME_PATTERN = Pattern.compile("upgrade\\s+(.*?)\\.t\\.me\\s+for");

    @Override
    public void close() {
        scheduler.shutdown();
    }
}
