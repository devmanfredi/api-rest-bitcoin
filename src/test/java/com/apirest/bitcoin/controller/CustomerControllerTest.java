package com.apirest.bitcoin.controller;

import com.apirest.bitcoin.builders.CustomerBuilder;
import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.exception.MessageException;
import com.apirest.bitcoin.service.CustomerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    private final Customer customer = CustomerBuilder.createValidCustomer().build();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setUp() throws MessageException {
        BDDMockito.when(customerService.findAll())
                .thenReturn(Flux.just(customer));

        BDDMockito.when(customerService.save(CustomerBuilder.createCustomerToBeSaved().build()))
                .thenReturn(Mono.just(customer));

        BDDMockito.when(customerService.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(customer));

        BDDMockito.when(customerService.update(CustomerBuilder.createValidCustomer().build()))
                .thenReturn(Mono.just(customer));

        BDDMockito.when(customerService.delete(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

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
    @DisplayName("list All returns a flux of customer")
    public void listAll_ReturnCustomers_WhenSuccessFul() {
        StepVerifier.create(customerController.findAll())
                .expectSubscription()
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    @DisplayName("Save customer")
    public void dadoCustomer_quandoSalvar_entaoRetornaCustomerSalvo() throws MessageException {
        Customer customerToBeSaved = CustomerBuilder.createCustomerToBeSaved().build();
        StepVerifier.create(customerController.save(customerToBeSaved))
                .expectSubscription()
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    @DisplayName("Find Customer by Id")
    public void dadoCustomer_quandoPesquisarPeloId_entaoRetornaCustomer() {
        StepVerifier.create(customerController.findById(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    @DisplayName("Update Customer")
    public void dadoCustomer_quandoAtualizar_entaoRetornaCustomerAtualizado() {
        StepVerifier.create(customerController.update(ArgumentMatchers.anyString(), CustomerBuilder.createValidCustomer().build()))
                .expectSubscription()
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete Customer")
    public void dadoCustomer_quandoDeletar_entaoRetornarSucesso() {
        StepVerifier.create(customerController.delete(ArgumentMatchers.anyString()))
                .expectSubscription()
                .verifyComplete();
    }

}
