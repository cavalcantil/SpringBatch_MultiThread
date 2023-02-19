package com.C134002.Batch_LC.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CLIENTES_EXTERNO_INFO")
public class ClienteExterno {

    @Id
    @Column(name = "NUM_CLIENTE")
    private int num;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "SOBRENOME")
    private String sobrenome;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SEXO")
    private String sexo;
    @Column(name = "TELEFONE")
    private String telefone;
    @Column(name = "PAIS")
    private String pais;
    @Column(name = "NASCIMENTO")
    private String nascimento;
}
