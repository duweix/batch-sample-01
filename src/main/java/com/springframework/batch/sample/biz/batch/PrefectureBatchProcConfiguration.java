package com.springframework.batch.sample.biz.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.springframework.batch.sample.biz.entity.FileDataTypeAEntity;
import com.springframework.batch.sample.biz.entity.MultiExecMgrEntity;

@Configuration
public class PrefectureBatchProcConfiguration {

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

    // @formatter:off
    @Bean
    public Job jobPrefectureBatchProc(JobRepository jobRepository, Step stepMultiExecMgr, Step stepReadFile) {
        return jobBuilders.get("jobPrefectureBatchProc")
                    .repository(jobRepository)
                    .start(stepMultiExecMgr)
                    .next(stepReadFile)
//                    .next(stepWriteDb())
                    .build();
    }
    // @formatter:on

    // @formatter:off
    @Bean
    public Step stepReadFile(PlatformTransactionManager transactionManager,
                              MultiResourceItemReader<FileDataTypeAEntity> multiFileItemReader,
                              ItemWriter<FileDataTypeAEntity> readFileWriter) {
        return stepBuilders.get("stepReadFile")
                    .transactionManager(transactionManager)
                    .<FileDataTypeAEntity, FileDataTypeAEntity>chunk(1)
                    .reader(multiFileItemReader)
                    .writer(readFileWriter)
                    .build();
    }
    // @formatter:on

    @Bean
    public MultiResourceItemReader<FileDataTypeAEntity> readFileReader(FlatFileItemReader<FileDataTypeAEntity> fileItemReader) {
        MultiResourceItemReader<FileDataTypeAEntity> reader = new MultiResourceItemReader<FileDataTypeAEntity>();
        reader.setResources(new Resource[] { new ClassPathResource("data-files/*.csv") });
        reader.setDelegate(fileItemReader);
        return reader;
    }

    @Bean
    public FlatFileItemReader<FileDataTypeAEntity> fileItemReader() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        DefaultLineMapper<FileDataTypeAEntity> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);

        FlatFileItemReader<FileDataTypeAEntity> reader = new FlatFileItemReader<>();
        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean
    public ItemWriter<FileDataTypeAEntity> readFileWriter() {
        return new ItemWriter<FileDataTypeAEntity>() {

            @Override
            public void write(List<? extends FileDataTypeAEntity> items) throws Exception {
                // TODO 自動生成されたメソッド・スタブ

            }

        };
    }

//    @Bean
//    public Step stepWriteDb() {
//        return null;
//    }

    // @formatter:off
    @Bean
    public Step stepMultiExecMgr(PlatformTransactionManager transactionManager,
                                  JdbcPagingItemReader<MultiExecMgrEntity> multiExecMgrReader,
                                  ItemWriter<MultiExecMgrEntity> multiExecMgrWriter) {

        return stepBuilders.get("stepMultiExecMgr")
                    .transactionManager(transactionManager)
                    .<MultiExecMgrEntity, MultiExecMgrEntity>chunk(1)
                    .reader(multiExecMgrReader)
                    .writer(multiExecMgrWriter)
                    .build();
    }
    // @formatter:on

    @Bean
    @StepScope
    // @formatter:off
    public JdbcPagingItemReader<MultiExecMgrEntity> multiExecMgrReader(@Value("#{jobParameters[REQ_ID]}") Long reqId,
                                                                        @Value("#{jobParameters[MULTI_PROC_NO]}") Long multiProcNo) throws Exception {
    // @formatter:on
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT REQ_ID, MULTI_PROC_NO, PROC_TARGET, PROC, STATES, ERR_INFO");
        queryProvider.setFromClause("FROM MULTI_EXEC_MGR");
        queryProvider.setWhereClause("WHERE REQ_ID = :REQ_ID AND MULTI_PROC_NO = :MULTI_PROC_NO");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("REQ_ID", Order.ASCENDING);
        sortKeys.put("MULTI_PROC_NO", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        JdbcPagingItemReader<MultiExecMgrEntity> reader = new JdbcPagingItemReader<MultiExecMgrEntity>();
        reader.setDataSource(dataSource);
        reader.setQueryProvider(queryProvider.getObject());

        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("REQ_ID", reqId);
        parameterValues.put("MULTI_PROC_NO", multiProcNo);
        reader.setParameterValues(parameterValues);

        reader.setPageSize(10);

        reader.setRowMapper(new BeanPropertyRowMapper<MultiExecMgrEntity>(MultiExecMgrEntity.class));
        return reader;
    }

    @Bean
    public ItemWriter<MultiExecMgrEntity> multiExecMgrWriter() {
        return new ItemWriter<MultiExecMgrEntity>() {

            @Override
            public void write(List<? extends MultiExecMgrEntity> items) throws Exception {

            }

        };
    }
}
