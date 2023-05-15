package pizzashop;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

public class PaymentServiceRepoMockEntity {

    public MenuRepository menuRepository = mock(MenuRepository.class);
    public PaymentRepository paymentRepository = new PaymentRepository("data/tests.txt");

    private Payment paymentMock = mock(Payment.class);

    @AfterEach
    public void setUp() {
        try {
            FileWriter fileWriter = new FileWriter("data/tests.txt");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getTotalAmountTest() {

        when(paymentMock.getTableNumber()).thenReturn(2);
        when(paymentMock.getType()).thenReturn(PaymentType.Card);
        when(paymentMock.getAmount()).thenReturn(25.0);

        try {
            Field paymentList = PaymentRepository.class.getDeclaredField("paymentList");
            paymentList.setAccessible(true);
            paymentList.set(paymentRepository, List.of(
                    new Payment(1, PaymentType.Cash, 10),
                    new Payment(2, PaymentType.Cash, 20),
                    new Payment(3, PaymentType.Card, 30)
            ));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Create the PizzaService (S)
        PizzaService pizzaService = new PizzaService(menuRepository, paymentRepository);

        // Check that PizzaService (S) calls the methods on PaymentRepository (R) correctly
        double result = pizzaService.getTotalAmount(PaymentType.Cash);
        Assertions.assertEquals(result, 30);


        // Check that PizzaService (S) calls the methods on Payment (E) correctly
        verify(paymentMock, never()).getTableNumber();
        verify(paymentMock, never()).getType();
        verify(paymentMock, never()).getAmount();

    }


    @Test
    public void getPaymentsTest() {

        // Create the PizzaService (S)
        PizzaService pizzaService = new PizzaService(menuRepository, paymentRepository);

        List<Payment> result;

        // Check that PizzaService (S) calls the methods on PaymentRepository (R) correctly
        result = pizzaService.getPayments();
        Assertions.assertEquals(result.size(), 0);

        int table = 1;
        PaymentType type = PaymentType.Cash;
        double amount = 15;

        pizzaService.addPayment(table, type, amount);

        result = pizzaService.getPayments();
        Assertions.assertEquals(result.size(), 1);


        // Check that PizzaService (S) calls the methods on Payment (E) correctly
        when(paymentMock.getTableNumber()).thenReturn(2);
        when(paymentMock.getType()).thenReturn(PaymentType.Card);
        when(paymentMock.getAmount()).thenReturn(25.0);

        verify(paymentMock, never()).getTableNumber();
        verify(paymentMock, never()).getType();
        verify(paymentMock, never()).getAmount();

        pizzaService.addPayment(paymentMock.getTableNumber(), paymentMock.getType(), paymentMock.getAmount());

        result = pizzaService.getPayments();

        Assertions.assertEquals(result.size(), 2);
        Assertions.assertEquals(result.get(1).getTableNumber(), 2);
        Assertions.assertEquals(result.get(1).getType(), PaymentType.Card);
        Assertions.assertEquals(result.get(1).getAmount(), 25.0);

        verify(paymentMock).getTableNumber();
        verify(paymentMock, times(1)).getType();
        verify(paymentMock, atLeastOnce()).getAmount();
    }
}
