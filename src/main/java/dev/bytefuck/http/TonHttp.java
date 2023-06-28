package dev.bytefuck.http;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bytefuck.exception.ApiException;
import dev.bytefuck.model.AppConfigModel;
import dev.bytefuck.model.TransactionModel;
import dev.bytefuck.model.TransactionsResponseModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TonHttp {
    private final AppConfigModel config;
    @SuppressWarnings("ConstantConditions")
    public List<TransactionModel> getTransactionsByAddress(
            String address,
            String lastTransactionHash,
            String lastTransactionLt
    ) throws IOException {
        var client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(config.getBaseURL()).newBuilder()
                .addPathSegment("getTransactions")
                .addQueryParameter("address", address)
                .addQueryParameter("limit", "100")
                .addQueryParameter("to_lt", "0")
                .addQueryParameter("archival", "true");

        if (lastTransactionHash != null && lastTransactionLt != null) {
            httpUrlBuilder.addQueryParameter("hash", lastTransactionHash);
            httpUrlBuilder.addQueryParameter("lt", lastTransactionLt);
        }

        var request = new Request.Builder()
                .url(httpUrlBuilder.build())
                .addHeader("accept", "application/json")
                .addHeader("X-API-Key", config.getApiKey())
                .build();

        try (var response = client.newCall(request).execute(); var responseBody = response.body()) {
            if (responseBody == null) return Collections.emptyList();

            var apiResponse = OBJECT_MAPPER.readValue(responseBody.string(), TransactionsResponseModel.class);

            if (!apiResponse.isOk())
                throw new ApiException("API response is not ok");

            return apiResponse.getResult();
        }
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES);
}
