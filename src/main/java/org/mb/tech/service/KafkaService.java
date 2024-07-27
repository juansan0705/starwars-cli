package org.mb.tech.service;

import java.util.concurrent.ExecutionException;

public interface KafkaService extends AutoCloseable{
    void sendKafkaMessage(byte[] messageBytes) throws ExecutionException, InterruptedException;
    void close();
}
