package org.anasantana.repository;

import org.anasantana.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private static final List<Cliente> clientes = new ArrayList<>();

    public void adicionar(Cliente cliente) {
        clientes.add(cliente);
    }

    public List<Cliente> listar() {
        return new ArrayList<>(clientes);
    }
}
