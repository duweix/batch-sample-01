package com.springframework.batch.sample.biz.batch.helloBatch;

import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.springframework.batch.sample.biz.entity.MultiExecMgrEntity;

@Configuration
public class HelloBatchStep2Configuration {

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Step helloBatchStep2() {
        // @formatter:off
        return stepBuilders.get("helloBatchStep2")
                    .transactionManager(transactionManager)
                    .<String, String>chunk(10)
                    .reader(step2ItemReader())
                    .writer(step2ItemWriter())
                    .build();
        // @formatter:on
    }

    @Bean
    public ItemReader<String> step2ItemReader() {
        return new ItemReader<String>() {
            private StepExecution stepExecution;

            @Override
            public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                JobExecution jobExecution = stepExecution.getJobExecution();
                ExecutionContext jobContext = jobExecution.getExecutionContext();
                @SuppressWarnings("unchecked")
                List<MultiExecMgrEntity> entityList = (List<MultiExecMgrEntity>) jobContext.get("entityList");

                return entityList.toString();
            }

            @BeforeStep
            public void beforeStep(StepExecution stepExecution) {
                this.stepExecution = stepExecution;
            }
        };
    }

    @Bean
    public ItemWriter<String> step2ItemWriter() {
        return new ItemWriter<String>() {

            @Override
            public void write(List<? extends String> items) throws Exception {
                System.out.println("=======================================");
                items.stream().forEach(System.out::println);
                System.out.println("=======================================");
            }

        };
    }
}
