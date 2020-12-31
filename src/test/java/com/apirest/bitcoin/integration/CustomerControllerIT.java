package com.apirest.bitcoin.integration;

import com.apirest.bitcoin.builders.CustomerBuilder;
import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.repository.CustomerRepository;
import com.apirest.bitcoin.service.CustomerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(CustomerService.class)
public class CustomerControllerIT {

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private WebTestClient testClient;

    private final Customer customer = CustomerBuilder.createValidCustomer().build();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setUp() {
        BDDMockito.when(customerRepository.findAll())
                .thenReturn(Flux.just(customer));
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0); //NOSONAR
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    @DisplayName("findAll returns a flux customer")
    public void findAllCustomers_returnFLuxOfCustomer_whenSuccess() {
        testClient
                .get()
                .uri("/api/v1/rest/bitcoin/customer")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(customer.getId())
                .jsonPath("$.[0].document").isEqualTo(customer.getDocument())
    }
}
