package org.skellig.demo;

import com.typesafe.config.Config;
import org.jetbrains.annotations.NotNull;
import org.skellig.teststep.processor.cassandra.CassandraTestStepProcessor;
import org.skellig.teststep.processor.cassandra.model.factory.CassandraTestStepFactory;
import org.skellig.teststep.processor.http.HttpTestStepProcessor;
import org.skellig.teststep.processor.http.model.factory.HttpTestStepFactory;
import org.skellig.teststep.processor.ibmmq.IbmMqTestStepProcessor;
import org.skellig.teststep.processor.ibmmq.model.factory.IbmMqTestStepFactory;
import org.skellig.teststep.processor.jdbc.JdbcTestStepProcessor;
import org.skellig.teststep.processor.jdbc.model.factory.JdbcTestStepFactory;
import org.skellig.teststep.processor.performance.PerformanceTestStepProcessor;
import org.skellig.teststep.processor.performance.metrics.prometheus.PrometheusMetricsFactory;
import org.skellig.teststep.processor.performance.model.factory.PerformanceTestStepFactory;
import org.skellig.teststep.processor.rmq.RmqConsumableTestStepProcessor;
import org.skellig.teststep.processor.rmq.RmqTestStepProcessor;
import org.skellig.teststep.processor.rmq.model.factory.RmqConsumableTestStepFactory;
import org.skellig.teststep.processor.rmq.model.factory.RmqTestStepFactory;
import org.skellig.teststep.processor.tcp.TcpConsumableTestStepProcessor;
import org.skellig.teststep.processor.tcp.TcpTestStepProcessor;
import org.skellig.teststep.processor.tcp.model.factory.TcpConsumableTestStepFactory;
import org.skellig.teststep.processor.tcp.model.factory.TcpTestStepFactory;
import org.skellig.teststep.processor.unix.UnixShellTestStepProcessor;
import org.skellig.teststep.processor.unix.model.factory.UnixShellTestStepFactory;
import org.skellig.teststep.runner.context.SkelligTestContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkelligDemoContext extends SkelligTestContext {

    @NotNull
    @Override
    protected List<TestStepProcessorDetails> getTestStepProcessors() {
        Config config = getConfig();

        return Stream.of(
                createTestStepProcessorFrom(
                        new HttpTestStepProcessor.Builder()
                                .withHttpService(config)
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        HttpTestStepFactory::new
                ),
                createTestStepProcessorFrom(
                        new JdbcTestStepProcessor.Builder()
                                .withDbServers(config)
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        JdbcTestStepFactory::new
                ),
                createTestStepProcessorFrom(
                        new CassandraTestStepProcessor.Builder()
                                .withDbServers(config)
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        CassandraTestStepFactory::new
                ),
                createTestStepProcessorFrom(
                        new RmqTestStepProcessor.Builder()
                                .rmqChannels(config)
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        RmqTestStepFactory::new
                ),
                createTestStepProcessorFrom(
                        new RmqConsumableTestStepProcessor.Builder()
                                .rmqChannels(config)
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        RmqConsumableTestStepFactory::new
                ),
                createTestStepProcessorFrom(
                        new IbmMqTestStepProcessor.Builder()
                                .ibmMqChannels(config)
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        IbmMqTestStepFactory::new
                ),
                createTestStepProcessorFrom(
                        new TcpTestStepProcessor.Builder()
                                .tcpChannels(config)
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        TcpTestStepFactory::new
                ),
                createTestStepProcessorFrom(
                        new TcpConsumableTestStepProcessor.Builder()
                                .tcpChannels(config)
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        TcpConsumableTestStepFactory::new
                ),
                createTestStepProcessorFrom(
                        new UnixShellTestStepProcessor.Builder()
                                .withHost(config)
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        UnixShellTestStepFactory::new
                ),
                createTestStepProcessorFrom(
                        rootTestStepProcessor ->
                                new PerformanceTestStepProcessor.Builder()
                                        .testStepProcessor(rootTestStepProcessor)
                                        .testStepRegistry(getTestStepRegistry())
                                        .metricsFactory(new PrometheusMetricsFactory())
                                        .build(),
                        PerformanceTestStepFactory::new
                )
        ).collect(Collectors.toList());
    }
}
