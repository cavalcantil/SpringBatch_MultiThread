package com.C134002.Batch_LC.config;

import com.C134002.Batch_LC.entity.Cliente;
import com.C134002.Batch_LC.entity.ClienteExterno;
import com.C134002.Batch_LC.repository.ClienteExternoRepository;
import com.C134002.Batch_LC.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;


@Configuration
@EnableBatchProcessing
@AllArgsConstructor

public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    ///Criacao do Bean StepProcessor
    private ClienteRepository clienteRepository;

    private ClienteExternoRepository clienteExternoRepository;


    /// Criacao para ItemReader
    public FlatFileItemReader <Cliente> readerCliente(){
        FlatFileItemReader <Cliente> itemReader=new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/planilha_clientes.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;

    }

    private LineMapper<Cliente> lineMapper(){
        DefaultLineMapper<Cliente> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
        BeanWrapperFieldSetMapper<Cliente> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Cliente.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }


    public FlatFileItemReader <ClienteExterno> readerClienteExterno(){
        FlatFileItemReader <ClienteExterno> itemReader2=new FlatFileItemReader<>();
        itemReader2.setResource(new FileSystemResource("src/main/resources/planilha_clientes_externo.csv"));
        itemReader2.setName("csvReader2");
        itemReader2.setLinesToSkip(1);
        itemReader2.setLineMapper(lineMapper2());
        return itemReader2;


    }


    private LineMapper<ClienteExterno> lineMapper2(){
        DefaultLineMapper<ClienteExterno> lineMapper2 = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer2 = new DelimitedLineTokenizer();
        lineTokenizer2.setDelimiter(",");
        lineTokenizer2.setStrict(false);
        lineTokenizer2.setNames("num", "nome", "sobrenome", "email", "sexo", "telefone", "pais", "nascimento");
        BeanWrapperFieldSetMapper<ClienteExterno> fieldSetMapper2 = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper2.setTargetType(ClienteExterno.class);

        lineMapper2.setLineTokenizer(lineTokenizer2);
        lineMapper2.setFieldSetMapper(fieldSetMapper2);


        return lineMapper2;

    }


    /// Criacao de ItemProcessor ///
    @Bean
    public ClienteProcessor clienteProcessor() {
        return new ClienteProcessor();

    }

    @Bean
    public ClienteExternoProcessor clienteExternoProcessor() {
        return new ClienteExternoProcessor();

    }


    /// Criacao do ItemWriter ////

    @Bean
    public RepositoryItemWriter<Cliente>writerCliente() {
        RepositoryItemWriter<Cliente> writerCliente = new RepositoryItemWriter<>();
        writerCliente.setRepository(clienteRepository);
        writerCliente.setMethodName("save");
        return writerCliente;
    }

    @Bean
    public RepositoryItemWriter<ClienteExterno>writerCliente2(){
        RepositoryItemWriter<ClienteExterno> writerCliente2 = new RepositoryItemWriter<>();
        writerCliente2.setRepository(clienteExternoRepository);
//        writerCliente2.setMethodName("save");
        return writerCliente2;

    }


    /// CRIACAO DOS STEPS PARA O JOB ///

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("csv-step-cliente").<Cliente,Cliente> chunk(10)
                .reader(readerCliente())
                .processor(clienteProcessor())
                .writer(writerCliente())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("csv-step-cliente_externo").<ClienteExterno, ClienteExterno> chunk(10)
                .reader(readerClienteExterno())
                .processor(clienteExternoProcessor())
                .writer(writerCliente2())
                .taskExecutor(taskExecutor())
                .build();
    }


    /// PROCESSO PARA O FLUXO ENTRE O STEP E O JOB
    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("ïmportClientesGeral")
                .flow(step1())
                .next(step2())
                .end().build();

    }

    ///MULTI-THREAD COCURRENCY EXECUTION
    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutorLC = new SimpleAsyncTaskExecutor();
        asyncTaskExecutorLC.setConcurrencyLimit(10);
        return asyncTaskExecutorLC;
    }


}


