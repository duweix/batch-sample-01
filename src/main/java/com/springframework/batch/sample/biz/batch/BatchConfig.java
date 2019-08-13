package com.springframework.batch.sample.biz.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing(modular = true)
public class BatchConfig {

    @Bean
    public ApplicationContextFactory prefectureBatchProc() {
        return new GenericApplicationContextFactory(PrefectureBatchProcConfiguration.class);
    }

    @Bean
    public ApplicationContextFactory batchTest() {
        return new GenericApplicationContextFactory(TestBatchConfiguration.class);
    }
}
