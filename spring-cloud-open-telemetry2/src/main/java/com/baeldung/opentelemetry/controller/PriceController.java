package com.baeldung.opentelemetry.controller;

import com.baeldung.opentelemetry.model.Price;
import com.baeldung.opentelemetry.repository.PriceRepository;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceController.class);

    private final PriceRepository priceRepository;

    private final Tracer tracer = GlobalOpenTelemetry
            .getTracerProvider()
            .tracerBuilder("my-custom-tracer")
            .build();

    @Autowired
    public PriceController(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @GetMapping(path = "/price/{id}")
    public Price getPrice(@PathVariable("id") long productId) {

        Span span = tracer
                .spanBuilder("my-span-price") //TODO Replace with the name of your span
                .setAttribute("my-key-1-price", "my-value-1-price") //TODO Add initial attributes
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("key-2-price", "value-2-price"); //TODO Add extra attributes if necessary

            //TODO your code Endpoint goes here

            LOGGER.info("Getting Price details for Product Id {}", productId);
            return priceRepository.getPrice(productId);

        } catch (Throwable throwable) {
            span.setStatus(StatusCode.ERROR, "Something bad happened!");
            span.recordException(throwable);
        } finally {
            span.end();
        }


  }
}
