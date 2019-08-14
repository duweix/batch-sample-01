package com.springframework.batch.sample.biz.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springframework.batch.sample.biz.batch.helloBatch.HelloBatchConfiguration;
import com.springframework.batch.sample.biz.batch.helloBatch.HelloBatchStep1Configuration;
import com.springframework.batch.sample.biz.batch.helloBatch.HelloBatchStep2Configuration;
import com.springframework.batch.sample.biz.batch.helloBatch.HelloBatchStep3Configuration;

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

    @Bean
    public ApplicationContextFactory helloBatch() {
        return new GenericApplicationContextFactory(HelloBatchConfiguration.class, HelloBatchStep1Configuration.class, HelloBatchStep2Configuration.class, HelloBatchStep3Configuration.class);
    }
}
