package com.apirest.bitcoin.service;

import com.apirest.bitcoin.builders.CustomerBuilder;
import com.apirest.bitcoin.domain.Customer;
import com.apirest.bitcoin.dto.BitBalanceRequestDTO;
import com.apirest.bitcoin.exception.MessageException;
import com.apirest.bitcoin.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

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

        BDDMockito.when(customerRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(customer));

        BDDMockito.when(customerRepository.save(CustomerBuilder.createValidCustomer().build()))
                .thenReturn(Mono.just(customer));

        BDDMockito.when(customerRepository.delete(ArgumentMatchers.any(Customer.class)))
                .thenReturn(Mono.empty());

        BDDMockito.when(customerRepository.findAll())
                .thenReturn(Flux.just(customer));
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
    public void dadoCustomer_quandoSalvar_entaoRetornaCustomerSalvo() throws MessageException {
        Customer customerToBeSaved = CustomerBuilder.createCustomerToBeSaved().build();
        StepVerifier.create(customerService.save(customerToBeSaved))
                .expectSubscription()
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    @DisplayName("Find Customer by Id")
    public void dadoCustomer_quandoPesquisarPeloId_entaoRetornaCustomer() {
        StepVerifier.create(customerService.findById(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns Mono error when customer does not exist")
    public void dadoCustomer_quandoPesquisarPeloId_entaoDeveRetornar() {
        BDDMockito.when(customerRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(customerService.findById(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("Update Customer")
    public void dadoCustomer_quandoAtualizar_entaoRetornaCustomerAtualizado() {
        StepVerifier.create(customerService.update(CustomerBuilder.createValidUpdateCustomer().build().getId(), new BitBalanceRequestDTO(BigDecimal.valueOf(100000))))
                .expectSubscription()
                .expectNext(CustomerBuilder.createValidUpdateCustomer().build())
                .verifyComplete();
    }

    @Test
    @DisplayName("update returns Mono error when customer does not exist")
    public void dadoCustomer_quandoUpdate_entaoRetornaErro() {
        BDDMockito.when(customerRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(customerService.update(CustomerBuilder.createValidCustomer().build().getId(), new BitBalanceRequestDTO(BigDecimal.valueOf(10000))))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("Delete Customer")
    public void dadoCustomer_quandoDeletar_entaoRetornarSucesso() {
        StepVerifier.create(customerService.delete(ArgumentMatchers.anyString()))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete returns Mono error when customer does not exist")
    public void dadoCustomer_quandoDeletar_entaoRetornaErro() {
        BDDMockito.when(customerRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(customerService.delete(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("FindAll return a flux of Customers")
    public void findAllCustomers() {
        StepVerifier.create(customerService.findAll())
                .expectSubscription()
                .expectNext(customer)
                .verifyComplete();
    }

}
