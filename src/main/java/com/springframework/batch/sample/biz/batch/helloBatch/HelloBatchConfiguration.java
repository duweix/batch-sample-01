package com.springframework.batch.sample.biz.batch.helloBatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class HelloBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilders;

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }

    @Bean
    public Job helloBatchJob(JobRepository jobRepository, Step helloBatchStep1, Step helloBatchStep2/* , Step helloBatchStep3 */) {
        return jobBuilders.get("helloBatchJob").start(helloBatchStep1).next(helloBatchStep2)./* next(helloBatchStep3). */build();
    }
}
