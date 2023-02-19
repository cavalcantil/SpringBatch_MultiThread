package com.C134002.Batch_LC.config;

import com.C134002.Batch_LC.entity.Cliente;
import org.springframework.batch.item.ItemProcessor;

public class ClienteProcessor implements ItemProcessor<Cliente, Cliente> {

    @Override
    public Cliente process(Cliente cliente) throws Exception {
        return cliente;
    }
}
