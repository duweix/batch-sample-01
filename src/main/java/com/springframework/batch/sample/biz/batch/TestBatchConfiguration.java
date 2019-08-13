package com.springframework.batch.sample.biz.batch;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.springframework.batch.sample.biz.entity.User;

@Configuration
public class TestBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private DataSource dataSource;

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Job testJob1(JobRepository jobRepository, Step testStep1) {
        return jobBuilders.get("testJob1").repository(jobRepository).start(testStep1).build();
    }

    @Bean
    public Step testStep1(PlatformTransactionManager transactionManager, ItemReader<User> fooDbReader, ItemWriter<String> barDbWriter) {
        //@formatter:off
        return stepBuilders.get("testStep1")
                    .transactionManager(transactionManager)
                    .<User, String>chunk(10)
                    .reader(fooDbReader)
                    .writer(barDbWriter)
                    .build();
      //@formatter:on
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<User> fooDbReader(@Value("#{jobParameters[REQUEST_ID]}") Long requestId, @Value("#{jobParameters[MULTI_PROC_NO]}") Long multiProcNo) {
        System.out.println("REQUEST_ID: " + requestId + ", MULTI_PROC_NO: " + multiProcNo + "+++++++++++++++++++++++++");
        JdbcCursorItemReader<User> fooDbReader = new JdbcCursorItemReader<>();
        fooDbReader.setDataSource(dataSource);
        fooDbReader.setSql("SELECT id FROM users");
        fooDbReader.setRowMapper(new BeanPropertyRowMapper<>(User.class));
        System.out.println("+++++++++++++++++++++++++");
        return fooDbReader;
    }

    @Bean
    public ItemWriter<String> barDbWriter() {
        ItemWriter<String> barDbWriter = new ItemWriter<>() {

            @Override
            public void write(List<? extends String> items) throws Exception {
                System.out.println("============================");
                System.out.println(items);
                System.out.println("============================");
            }

        };
        return barDbWriter;
    }
}
