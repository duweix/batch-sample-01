package com.springframework.batch.sample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BatchSample01App {

    public static void main(String[] args) throws Exception {
        String[] contextConfigLocations = { "spring-config/application-context.xml" };
        try (ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(contextConfigLocations)) {
            Job jobPrefectureBatchProc = (Job) applicationContext.getBean("jobPrefectureBatchProc");
            JobLauncher jobLauncher = (JobLauncher) applicationContext.getBean("jobLauncher");
//            long requestId = 1L;
//            for (long multiProcNo = 1L; multiProcNo <= 8L; multiProcNo++) {
//                jobLauncher.run(jobPrefectureBatchProc, new JobParametersBuilder().addLong("REQ_ID", requestId, true).addLong("MULTI_PROC_NO", multiProcNo, true).toJobParameters());
//            }
            jobLauncher.run(jobPrefectureBatchProc, new JobParametersBuilder().addLong("REQ_ID", 1L, true).addLong("MULTI_PROC_NO", 1L, true).toJobParameters());

            System.in.read();
        }
    }
}
