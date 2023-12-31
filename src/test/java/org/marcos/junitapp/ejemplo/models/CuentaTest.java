package org.marcos.junitapp.ejemplo.models;

import org.junit.jupiter.api.*;
import org.marcos.junitapp.ejemplo.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    Cuenta cuenta;
    @BeforeEach
    void initMetodoTest() {

        this.cuenta = new Cuenta("Marcos", new BigDecimal("1000.12345"));

        System.out.println("Iniciando el método. ");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el método de prueba");
    }

    @Test
    @DisplayName("Probando nombre de la cuenta corriente!")
    void testNombreCuenta() {

        //   cuenta.setPersona("Marcos");
        String esperado = "Marcos";
        String real = cuenta.getPersona();
        assertNotNull(real, () -> "La cuenta no puede ser nula");
        assertEquals(esperado, real, () -> "El nombre de la cuenta no es el que se esperaba: " + esperado
                + " Sin embargo fue: " + real );
        assertTrue(real.equals("Marcos"), () ->"Nombre cuenta esperada debe ser igual a la real");

    }

    @Test
    @DisplayName("Probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valor esperado.")
    void testSaldoCuenta() {

        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Referencia que sean iguales con el método equals. ")
    void testReferenciaCuenta() {
        cuenta = new Cuenta("John Doe", new BigDecimal("8900.9977"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9977"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }

    @Test
    @DisplayName("Que el saldo no sea null ")
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {

        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuenta() {
        Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Pablo", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());


    }

    @Test
    //@Disabled
    @DisplayName("Probando relaciones entre las cuentas y el banco con assertAll.")
    void testRelacionesBancoCuentas() {
        //fail();
        Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Pablo", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertAll(() -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                                        () -> " El valor del saldo de la cuenta2 no es el esperado. "),
                () -> assertEquals("3000", cuenta1.getSaldo().toPlainString(),
                        () -> " El valor del saldo de la cuenta1 no es el esperado. "),
                () -> assertEquals(2, banco.getCuentas().size(),
                        () -> " El banco no tiene las cuentas esperadas"),
                () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Pablo", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Pablo"))
                            .findFirst().get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("Jhon Doe"))));


    }
}