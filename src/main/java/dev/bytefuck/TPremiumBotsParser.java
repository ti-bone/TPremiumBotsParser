package dev.bytefuck;

import dev.bytefuck.helper.AppHelper;
import dev.bytefuck.http.TonHttp;
import dev.bytefuck.model.AppConfigModel;
import dev.bytefuck.processor.TransactionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TPremiumBotsParser implements Runnable {
    private final AppConfigModel config = AppHelper.loadConfig();

    @Override
    public void run() {
        LOGGER.info("Starting...");
        try (var processor = new TransactionProcessor(new TonHttp(config))) {
            processor.init();

            LOGGER.info("Inited TransactionProcessor.");

            processor.processTransactions(config.getFragmentWallet());

            LOGGER.info("Transactions processed.");
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TPremiumBotsParser.class);

    public static void main(String[] args) {
        AppHelper.createDirectories();
        new TPremiumBotsParser().run();
    }
}