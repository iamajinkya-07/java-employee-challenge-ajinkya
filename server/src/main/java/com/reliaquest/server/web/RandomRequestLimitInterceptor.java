package com.reliaquest.server.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class RandomRequestLimitInterceptor implements HandlerInterceptor {

    private static final Random RANDOM = new Random();

    private static final int REQUEST_LIMIT = RANDOM.nextInt(5, 10); // 5 to 9 inclusive
    private static final Duration REQUEST_BACKOFF_DURATION =
            Duration.ofSeconds(RANDOM.nextInt(30, 90)); // 30 to 89 seconds

    private final AtomicReference<RequestLimit> requestLimit = new AtomicReference<>(RequestLimit.init());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RequestLimit current = requestLimit.get();
        if (current.count() >= REQUEST_LIMIT) {
            Instant threshold = Instant.now().minus(REQUEST_BACKOFF_DURATION);
            if (threshold.isBefore(current.lastRequested())) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                return false;
            } else {
                requestLimit.set(RequestLimit.init());
            }
        } else {
            requestLimit.getAndUpdate(
                    currentRequestLimit -> new RequestLimit(currentRequestLimit.count() + 1, Instant.now()));
        }

        return true;
    }


    private record RequestLimit(@Getter int count, @Getter Instant lastRequested) {
        public static RequestLimit init() {
            return new RequestLimit(0, Instant.now());
        }
    }
}
