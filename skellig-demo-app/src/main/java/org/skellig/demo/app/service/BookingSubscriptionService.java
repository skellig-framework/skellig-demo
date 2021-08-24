package org.skellig.demo.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.skellig.demo.app.exception.SkelligServiceException;
import org.skellig.demo.app.rest.model.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class BookingSubscriptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingSubscriptionService.class);

    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private BookingService bookingService;
    private List<SocketRequestHandler> requestHandlers;
    private ObjectMapper objectMapper;

    @Autowired
    public BookingSubscriptionService(BookingService bookingService) {
        this.bookingService = bookingService;
        objectMapper = new ObjectMapper();
        requestHandlers = new ArrayList<>();
        executorService = Executors.newCachedThreadPool();
        try {
            startSocketServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendReportUpdatesToSubscribers() {
        LOGGER.info("Notifying all subscribers about updated bookings");
        requestHandlers.forEach(handler -> {
            if (handler.isConnected()) {
                executorService.execute(handler);
            }
        });
    }

    public void shutdown() {
        requestHandlers.forEach(SocketRequestHandler::close);
        executorService.shutdown();
    }

    private void startSocketServer() {
        executorService.execute(() -> {
            try {
                LOGGER.info("Starting up the Reporting Subscription Service...");
                serverSocket = new ServerSocket(2116);
                while (!executorService.isShutdown()) {
                    try {
                        Socket socket = serverSocket.accept();
                        socket.setKeepAlive(true);
                        LOGGER.info("Reporting Subscription Service has accepted requests");
                        SocketRequestHandler socketRequestHandler =
                                new SocketRequestHandler(socket, objectMapper, bookingService);
                        requestHandlers =
                                requestHandlers.stream()
                                        .filter(SocketRequestHandler::isConnected)
                                        .collect(Collectors.toList());
                        requestHandlers.add(socketRequestHandler);
                    } catch (Exception ignored) {
                    }
                }
            } catch (Exception ignored) {
            } finally {
                LOGGER.info("Shutting TCP Reporting Subscription Service down...");
            }
        });
    }

    private static class SocketRequestHandler implements Runnable {

        private Socket socket;
        private DataOutputStream output;
        private ObjectMapper objectMapper;
        private BookingService bookingService;

        public SocketRequestHandler(Socket socket, ObjectMapper objectMapper, BookingService bookingService) {
            this.socket = socket;
            this.objectMapper = objectMapper;
            this.bookingService = bookingService;
            try {
                output = new DataOutputStream(socket.getOutputStream());
            } catch (Exception ex) {
                throw new SkelligServiceException("Can't open stream of Socket connection " + socket.getRemoteSocketAddress(), ex);
            }
        }

        @Override
        public void run() {
            try {
                if (output != null) {
                    Collection<Booking> bookings = bookingService.getAll();
                    byte[] data = objectMapper.writeValueAsBytes(bookings);
                    output.write(data, 0, data.length);
                    output.flush();
                }
            } catch (Exception ex) {
                LOGGER.error("Failed to send data: " + ex.getMessage());
                close();
            }
        }

        boolean isConnected() {
            return !socket.isClosed();
        }

        void close() {
            try {
                LOGGER.error("Socket connection closed: " + socket.getRemoteSocketAddress());
                output.close();
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }
}
