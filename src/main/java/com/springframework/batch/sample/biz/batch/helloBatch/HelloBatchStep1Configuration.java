package com.springframework.batch.sample.biz.batch.helloBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.H2PagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.springframework.batch.sample.biz.entity.MultiExecMgrEntity;

@Configuration
public class HelloBatchStep1Configuration {

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Step helloBatchStep1() {
        // @formatter:off
        return stepBuilders.get("helloBatchStep1")
                    .transactionManager(transactionManager)
                    .<MultiExecMgrEntity, MultiExecMgrEntity>chunk(10)
                    .reader(step1ItemReader(null, null))
                    .writer(step1ItemWriter())
                    .listener(promotionListener())
                    .build();
        // @formatter:on
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<MultiExecMgrEntity> step1ItemReader(@Value("#{jobParameters[REQ_ID]}") Long reqId, @Value("#{jobParameters[MULTI_PROC_NO]}") Long multiProcNo) {
        H2PagingQueryProvider queryProvider = new H2PagingQueryProvider();
        queryProvider.setSelectClause("SELECT REQ_ID, MULTI_PROC_NO, PROC_TARGET, PROC, STATES, ERR_INFO");
        queryProvider.setFromClause("FROM MULTI_EXEC_MGR");
        queryProvider.setWhereClause("WHERE REQ_ID = :REQ_ID AND MULTI_PROC_NO = :MULTI_PROC_NO");
        queryProvider.setSortKeys(new HashMap<>() {
            {
                put("REQ_ID", Order.ASCENDING);
                put("MULTI_PROC_NO", Order.ASCENDING);
            }
        });

        JdbcPagingItemReader<MultiExecMgrEntity> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setPageSize(10);
        itemReader.setQueryProvider(queryProvider);
        itemReader.setParameterValues(new HashMap<>() {
            {
                put("REQ_ID", reqId);
                put("MULTI_PROC_NO", multiProcNo);
            }
        });
        itemReader.setRowMapper(new BeanPropertyRowMapper<>(MultiExecMgrEntity.class));
        return itemReader;
    }

    @Bean
    public ItemWriter<MultiExecMgrEntity> step1ItemWriter() {
        return new ItemWriter<MultiExecMgrEntity>() {
            private StepExecution stepExecution;

            @Override
            public void write(List<? extends MultiExecMgrEntity> items) throws Exception {
                ExecutionContext stepContext = this.stepExecution.getExecutionContext();
                @SuppressWarnings("unchecked")
                List<MultiExecMgrEntity> entityList = (List<MultiExecMgrEntity>) stepContext.get("entityList");
                if (entityList == null) {
                    entityList = new ArrayList<>();
                    stepContext.put("entityList", entityList);
                }
                entityList.addAll(items);
            }

            @BeforeStep
            public void beforeStep(StepExecution stepExecution) {
                this.stepExecution = stepExecution;
            }
        };
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[] { "entityList" });
        return listener;
    }
}
