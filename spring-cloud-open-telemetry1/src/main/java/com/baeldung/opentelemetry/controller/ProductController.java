package com.baeldung.opentelemetry.controller;

import com.baeldung.opentelemetry.api.client.PriceClient;
import com.baeldung.opentelemetry.model.Product;
import com.baeldung.opentelemetry.repository.ProductRepository;
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
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final PriceClient priceClient;

    private final ProductRepository productRepository;

    private final Tracer tracer = GlobalOpenTelemetry
            .getTracerProvider()
            .tracerBuilder("my-custom-tracer")
            .build();

    @Autowired
    public ProductController(PriceClient priceClient, ProductRepository productRepository) {
        this.priceClient = priceClient;
        this.productRepository = productRepository;
    }

    @GetMapping(path = "/product/{id}")
    public Product getProductDetails(@PathVariable("id") long productId){
        Product product = null;
        Span span = tracer
                .spanBuilder("my-span") //TODO Replace with the name of your span
                .setAttribute("my-key-1", "my-value-1") //TODO Add initial attributes
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("key-2", "value-2"); //TODO Add extra attributes if necessary

            //TODO your code Endpoint goes here

            LOGGER.info("Getting Product and Price Details With Product Id {}", productId);
            product = productRepository.getProduct(productId);
            product.setPrice(priceClient.getPrice(productId));



        } catch (Throwable throwable) {
            span.setStatus(StatusCode.ERROR, "Something bad happened!");
            span.recordException(throwable);
        } finally {
            span.end();

        }
        return product;

    }
}
