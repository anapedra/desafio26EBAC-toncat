package org.anasantana.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.anasantana.daos.generics.IUserDAO;
import org.anasantana.daos.generics.impl.UserDAOImpl;
import org.anasantana.dtos.ClientDTO;
import org.anasantana.services.IUserService;
import org.anasantana.services.impl.UserServiceImpl;

@Path("/api/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {

    private final IUserService service;

    public ClientResource() {
        EntityManager em = Persistence.createEntityManagerFactory("main-pu").createEntityManager();
        IUserDAO userDAO = new UserDAOImpl(em);
        this.service = new UserServiceImpl(userDAO);
    }

    @GET
    public Response listarClientes(@QueryParam("page") @DefaultValue("0") int page,
                                   @QueryParam("size") @DefaultValue("10") int size) {
        return Response.ok(service.findAll(page, size)).build();
    }

    @POST
    public Response cadastrar(ClientDTO dto) {
        try {
            ClientDTO novo = service.insert(dto);
            return Response.status(Response.Status.CREATED).entity(novo).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro ao cadastrar cliente: " + e.getMessage()).build();
        }
    }
}
