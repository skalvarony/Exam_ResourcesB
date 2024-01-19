package com.example.demo;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Observation2Tests {

    private Observation2Mock observationMock;

    @Before
    public void setup() {
        observationMock = new Observation2Mock("MS123", "2022", "CODE1", 10.5f, 2.3f, 5.0f, 15.0f, "Flag1");
    }

    @Test
    public void testGetMscode() {
        assertEquals("MS123", observationMock.getMscode());
    }

    @Test
    public void testGetYear() {
        assertEquals("2022", observationMock.getYear());
    }

    @Test
    public void testGetEstCode() {
        assertEquals("CODE1", observationMock.getEstCode());
    }

    // Continúa con pruebas similares para el resto de los getters

    @Test
    public void testGetId() {
        assertEquals("idprueba", observationMock.get_id());
        System.out.println("Mock get_id() called successfully");
    }

    // Aquí puedes añadir más pruebas para verificar cualquier lógica adicional
    // o comportamientos específicos de tu clase Observation2.
}
