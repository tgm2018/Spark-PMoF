package org.apache.spark.storage.pmof;

import com.intel.rpmp.PmPoolClient;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RemotePersistentMemoryPool {
  private static String remote_host;
  private static String remote_port_str;

  private RemotePersistentMemoryPool(String remote_address, String remote_port) throws IOException {
    pmPoolClient = new PmPoolClient(remote_address, remote_port);
  }

  public static RemotePersistentMemoryPool getInstance(String remote_address, String remote_port)
      throws IOException {
    if (instance == null) {
      synchronized (RemotePersistentMemoryPool.class) {
        if (instance == null) {
          remote_host = remote_address;
          remote_port_str = remote_port;
          instance = new RemotePersistentMemoryPool(remote_address, remote_port);
        }
      }
    }
    return instance;
  }

  public static String getHost() {
    return remote_host;
  }

  public static int getPort() {
    return Integer.parseInt(remote_port_str);
  }

  public int read(long address, long size, ByteBuffer byteBuffer) {
    return pmPoolClient.read(address, size, byteBuffer);
  }

  public long put(String key, ByteBuffer data, long size) {
    return pmPoolClient.put(key, data, size);
  }

  public long[] getMeta(String key) {
    return pmPoolClient.getMeta(key);
  }

  public int del(String key) throws IOException {
    return pmPoolClient.del(key);
  }

  private static PmPoolClient pmPoolClient;
  private static RemotePersistentMemoryPool instance;
}