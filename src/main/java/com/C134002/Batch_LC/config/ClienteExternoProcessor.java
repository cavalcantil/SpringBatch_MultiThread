package com.C134002.Batch_LC.config;

import com.C134002.Batch_LC.entity.ClienteExterno;
import org.springframework.batch.item.ItemProcessor;

public class ClienteExternoProcessor implements ItemProcessor<ClienteExterno, ClienteExterno> {
    @Override
    public ClienteExterno process(ClienteExterno clienteExterno) throws Exception {
        if (clienteExterno.getPais().equals("Brazil") && clienteExterno.getSexo().equals("Female")){
            return clienteExterno;
        }else{
            return null;

    }
    }
}
