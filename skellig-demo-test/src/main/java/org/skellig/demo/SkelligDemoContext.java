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
import org.skellig.teststep.processor.rmq.RmqTestStepProcessor;
import org.skellig.teststep.processor.rmq.model.factory.RmqTestStepFactory;
import org.skellig.teststep.processor.tcp.TcpTestStepProcessor;
import org.skellig.teststep.processor.tcp.model.factory.TcpTestStepFactory;
import org.skellig.teststep.processor.unix.UnixShellTestStepProcessor;
import org.skellig.teststep.processor.unix.model.factory.UnixShellTestStepFactory;
import org.skellig.teststep.runner.context.SkelligTestContext;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkelligDemoContext extends SkelligTestContext {

    @NotNull
    @Override
    protected List<TestStepProcessorDetails> getTestStepProcessors() {
        Config config = getConfig();
        return Stream.of(
                new TestStepProcessorDetails(
                        new HttpTestStepProcessor.Builder()
                                .withHttpService(config)
                                .withTestStepResultConverter(getTestStepResultConverter())
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        createTestStepFactoryFrom(HttpTestStepFactory::new)
                ),
                new TestStepProcessorDetails(
                        new JdbcTestStepProcessor.Builder()
                                .withDbServers(config)
                                .withTestStepResultConverter(getTestStepResultConverter())
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        createTestStepFactoryFrom(JdbcTestStepFactory::new)
                ),
                new TestStepProcessorDetails(
                        new CassandraTestStepProcessor.Builder()
                                .withDbServers(config)
                                .withTestStepResultConverter(getTestStepResultConverter())
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        createTestStepFactoryFrom(CassandraTestStepFactory::new)
                ),
                new TestStepProcessorDetails(
                        new RmqTestStepProcessor.Builder()
                                .rmqChannels(config)
                                .withTestStepResultConverter(getTestStepResultConverter())
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        createTestStepFactoryFrom(RmqTestStepFactory::new)
                ),
                new TestStepProcessorDetails(
                        new IbmMqTestStepProcessor.Builder()
                                .ibmMqChannels(config)
                                .withTestStepResultConverter(getTestStepResultConverter())
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        createTestStepFactoryFrom(IbmMqTestStepFactory::new)
                ),
                new TestStepProcessorDetails(
                        new TcpTestStepProcessor.Builder()
                                .tcpChannels(config)
                                .withTestStepResultConverter(getTestStepResultConverter())
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        createTestStepFactoryFrom(TcpTestStepFactory::new)
                ),
                new TestStepProcessorDetails(
                        new UnixShellTestStepProcessor.Builder()
                                .withHost(config)
                                .withTestStepResultConverter(getTestStepResultConverter())
                                .withTestScenarioState(getTestScenarioState())
                                .withValidator(getTestStepResultValidator())
                                .build(),
                        createTestStepFactoryFrom(UnixShellTestStepFactory::new)
                )
        ).collect(Collectors.toList());
    }
}
