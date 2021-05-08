package org.skellig.demo.app.service;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private BookingService bookingService;
    private ReportingStrategy reportingStrategy;

    @Autowired
    public ReportService(BookingService bookingService) {
        this.bookingService = bookingService;
        executorService = Executors.newCachedThreadPool();
        reportingStrategy = new ReportingStrategy();
        try {
            startSocketServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private void startSocketServer() {
        executorService.execute(() -> {
            try {
                LOGGER.info("Starting up the Reporting Service...");
                serverSocket = new ServerSocket(1116);
                while (!executorService.isShutdown()) {
                    try {
                        Socket socket = serverSocket.accept();
                        LOGGER.info("Reporting Service has accepted requests");
                        executorService.submit(() -> {
                            new SocketRequestHandler(socket, reportingStrategy).run();
                        });
                    } catch (Exception ignored) {
                    }
                }
                LOGGER.info("Shutting tcp server down...");
            } catch (Exception ignored) {
            }
        });
    }

    private static class SocketRequestHandler implements Runnable {

        private Socket socket;
        private ReportingStrategy reportingStrategy;

        public SocketRequestHandler(Socket socket, ReportingStrategy reportingStrategy) {
            this.socket = socket;
            this.reportingStrategy = reportingStrategy;
        }

        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                LOGGER.info("Reporting Service received connection at " + socket.getRemoteSocketAddress());
                readAndRespond(input, output);
            } catch (Exception ex) {
                LOGGER.error("Failed socket connection: " + ex.getMessage());
                close();
            }
        }

        private void readAndRespond(BufferedReader in, BufferedWriter out) throws Exception {
            char[] bytes = new char[1024 * 1024];
            int read = in.read(bytes, 0, 1024);
            bytes = Arrays.copyOf(bytes, read);

            LOGGER.info("Reporting Service has received request: " + new String(bytes));
            respond(out, reportingStrategy.getReport(new String(bytes)));
        }

        private void respond(BufferedWriter out, String data) throws Exception {
            out.write(data, 0, data.length());
            out.flush();

            LOGGER.info("Sending back the response from Reporting Service: " + data);
        }

        void close() {
            try {
                LOGGER.error("Socket connection closed: " + socket.getRemoteSocketAddress());
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }

    class ReportingStrategy {

        private Configuration configuration;

        public ReportingStrategy() {
            configuration = new Configuration(Configuration.VERSION_2_3_30);
            configuration.setTemplateLoader(new ClassTemplateLoader(getClass(), "/report"));
            configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_30));
        }

        public String getReport(String type) {
            if (type.equals("bookings.all")) {
                Map<Object, Object> bookings = new HashMap<>();
                bookings.put("date", LocalDateTime.now());
                bookings.put("bookings", bookingService.getAll());

                return convertFromTemplate(bookings);
            } else {
                return null;
            }
        }

        private String convertFromTemplate(Map<Object, Object> bookings) {
            try {
                StringWriter out = new StringWriter();
                Template template = configuration.getTemplate("bookings.all.ftl");
                template.process(bookings, out);
                return out.getBuffer().toString();
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
