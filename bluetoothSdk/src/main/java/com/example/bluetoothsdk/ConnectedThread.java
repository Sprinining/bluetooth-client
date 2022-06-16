package com.example.bluetoothsdk;

import android.bluetooth.BluetoothSocket;

import com.example.bluetoothsdk.interfaces.ConnectResultListener;
import com.example.bluetoothsdk.interfaces.TransferListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectedThread extends Thread {
    private final LinkedBlockingQueue<byte[]> readBlockingQueue = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<byte[]> writeBlockingQueue = new LinkedBlockingQueue<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private TransferListener readTransferListener;
    private TransferListener writeTransferListener;
    private ConnectResultListener connectResultListener;

    // 双方通信的bluetoothSocket
    private final BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ConnectedThread(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
        try {
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] read() {
        try {
            return readBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void write(byte[] bytes) {
        try {
            writeBlockingQueue.put(bytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动读写线程
     */
    @Override
    public void run() {
        threadPool.execute(new Reader());
        threadPool.execute(new Writer());
    }

    /**
     * 释放serverThread的资源
     */
    public void closeConnectedThread() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            if (threadPool != null) {
                threadPool.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connectResultListener != null) {
                connectResultListener.disconnect();
            }
        }
    }

    /**
     * 读线程
     */
    private class Reader extends Thread {
        @Override
        public void run() {
            try {
                byte[] bytes = new byte[1024 * 10];
                int n;
                // 一条指令的字节长度
                int count;
                while (bluetoothSocket.isConnected()) {
                    count = 0;
                    while ((n = inputStream.read()) != -1) {
                        bytes[count++] = (byte) n;
                        if (n == '$') {
                            break;
                        }
                    }
                    byte[] oneMessage = new byte[count];
                    System.arraycopy(bytes, 0, oneMessage, 0, count);
                    readTransferListener.transferSuccess(oneMessage);
                }
            } catch (Exception e) {
                readTransferListener.transferException("读线程异常终止", e);
            } finally {
                closeConnectedThread();
                readTransferListener.transferFinish();
            }
        }
    }

    /**
     * 写线程
     */
    private class Writer extends Thread {
        @Override
        public void run() {
            try {
                while (bluetoothSocket.isConnected()) {
                    byte[] bytes = writeBlockingQueue.take();
                    outputStream.write(bytes);
                    outputStream.flush();
                    writeTransferListener.transferSuccess(bytes);
                }
            } catch (Exception e) {
                writeTransferListener.transferException("写线程异常终止", e);
            } finally {
                closeConnectedThread();
                writeTransferListener.transferFinish();
            }
        }
    }

    public void setConnectResultListener(ConnectResultListener connectResultListener) {
        this.connectResultListener = connectResultListener;
    }

    public void setReadTransferListener(TransferListener readTransferListener) {
        this.readTransferListener = readTransferListener;
    }

    public void setWriteTransferListener(TransferListener writeTransferListener) {
        this.writeTransferListener = writeTransferListener;
    }
}
