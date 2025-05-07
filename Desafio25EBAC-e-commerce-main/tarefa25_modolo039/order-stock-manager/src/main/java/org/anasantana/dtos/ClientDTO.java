package org.anasantana.dtos;

import jakarta.validation.constraints.NotBlank;
import org.anasantana.annotetions.CPF;
import org.anasantana.model.User;

import java.io.Serializable;
import java.util.Objects;

public class ClientDTO implements Serializable {
    private static final long serialVersionUID=1L;
    private Long id;
    @NotBlank(message = "Campo obrigatório")
    private String name;
  //  @CPF
    private String cpf;
    private String phone;
    private String email;

    public ClientDTO(){
    }

    public ClientDTO(Long id, String name, String cpf, String phone, String email) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
    }

    public ClientDTO(User entyty) {
        id = entyty.getId();
        name = entyty.getName();
        cpf = entyty.getCpf();
        phone = entyty.getMainPhone();
        email = entyty.getRegistrationEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientDTO)) return false;
        ClientDTO clientDTO = (ClientDTO) o;
        return Objects.equals(id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}