package com.apirest.bitcoin.service;

import com.apirest.bitcoin.builders.CustomerBuilder;
import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;
    //
    @Mock
    private CustomerRepository customerRepository;

    private final Customer customer = CustomerBuilder.createValidCustomer().build();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setUp() {
        BDDMockito.when(customerRepository.save(CustomerBuilder.createCustomerToBeSaved().build()))
                .thenReturn(Mono.just(customer));
        BDDMockito.when(customerRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(customer));
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
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
    @DisplayName("Save a customer")
    public void dadoCustomer_quandoSalvar_entaoRetornaCustomerSalvo() {
        Customer customerToBeSaved = CustomerBuilder.createCustomerToBeSaved().build();
        StepVerifier.create(customerService.save(customerToBeSaved))
                .expectSubscription()
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    @DisplayName("Find Customer by Id")
    public void dadoCustomer_quandoPesquisarPeloId_entaoRetornaCustomer() {
        StepVerifier.create(customerService.findById(1L))
                .expectSubscription()
                .expectNext(customer)
                .verifyComplete();
    }


}
