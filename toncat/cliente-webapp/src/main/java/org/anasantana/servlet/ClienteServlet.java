package org.anasantana.servlet;

import org.anasantana.model.Cliente;
import org.anasantana.repository.ClienteRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {
    private final ClienteRepository repo = new ClienteRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome = req.getParameter("nome");
        String email = req.getParameter("email");

        repo.adicionar(new Cliente(nome, email));

        req.setAttribute("clientes", repo.listar());
        req.getRequestDispatcher("clientes.jsp").forward(req, resp);
    }
}
