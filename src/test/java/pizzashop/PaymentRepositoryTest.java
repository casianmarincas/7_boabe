package pizzashop;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

public class PaymentRepositoryTest {

    private final PaymentRepository paymentRepository = new pizzashop.repository.PaymentRepository("data/tests.txt");

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
    public void testAddPayment() {
        Payment payment = new Payment(1, PaymentType.Card, 23);
        paymentRepository.add(payment);
        assertEquals(paymentRepository.getAll().get(0), payment);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("data/tests.txt"));
            assertEquals(bufferedReader.readLine(), payment.toString());
            assertNull(bufferedReader.readLine());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testGetAll() {
        List<Payment> paymentList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            paymentList.add(new Payment(1, PaymentType.Card, 3));
        }
        paymentList.forEach(paymentRepository::add);
        assertEquals(paymentList, paymentRepository.getAll());
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("data/tests.txt"));
            for (Payment p: paymentList) {
                assertEquals(bufferedReader.readLine(), p.toString());
            }
            assertNull(bufferedReader.readLine());
        } catch (IOException e) {
            fail();
        }
    }

}
