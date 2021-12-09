package com.example.uhf_bluetoothclient.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bluetoothsdk.BluetoothUtils;
import com.example.bluetoothsdk.ConnectedThread;
import com.example.bluetoothsdk.interfaces.ConnectResultListener;
import com.example.bluetoothsdk.interfaces.TransferListener;
import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.util.MessageProtocolUtils;

import java.nio.charset.StandardCharsets;

public class ServerSimulationFragment extends Fragment {
    private static final String TAG = ServerSimulationFragment.class.getSimpleName();
    private final BluetoothUtils bluetoothUtils;
    private static ConnectedThread connectedThread;

    private View rootView;

    public ServerSimulationFragment() {
        bluetoothUtils = BluetoothUtils.getINSTANCE();
    }

    public static ServerSimulationFragment newInstance() {
        return new ServerSimulationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_server_simulation, container, false);
            Button btn_start_server = rootView.findViewById(R.id.btn_start_server);
            btn_start_server.setOnClickListener(v -> {
                bluetoothUtils.enableDiscoverable();
                bluetoothUtils.registerServer(new ConnectResultListener() {
                    @Override
                    public void connectSuccess(ConnectedThread connectedThread) {
                        Log.i(TAG, "connectSuccess: 有客户端连接");
                        ServerSimulationFragment.connectedThread = connectedThread;
                        // 读监听
                        connectedThread.setReadTransferListener(new TransferListener() {
                            @Override
                            public void transferFinish() {
                                Log.i(TAG, "transferFinish: 服务器读线程关闭");
                            }

                            @Override
                            public void transferSuccess(byte[] bytes) {
                                Log.i(TAG, "transferSuccess: 服务器接收：" + new String(bytes, StandardCharsets.UTF_8));
                            }

                            @Override
                            public void transferException(String str, Exception e) {
                                Log.e(TAG, "transferException: " + str, e);
                            }
                        });
                        // 写监听
                        connectedThread.setWriteTransferListener(new TransferListener() {
                            @Override
                            public void transferFinish() {
                                Log.i(TAG, "transferFinish: 服务器写线程关闭");
                            }

                            @Override
                            public void transferSuccess(byte[] bytes) {
                                Log.i(TAG, "transferSuccess: 服务器发送" + new String(bytes, StandardCharsets.UTF_8));
                            }

                            @Override
                            public void transferException(String str, Exception e) {
                                Log.e(TAG, "transferException: " + str, e);
                            }
                        });
                    }

                    @Override
                    public void connectFail(Exception e) {
                        Log.e(TAG, "connectFail: 服务器接收客户端失败", e);
                    }

                    @Override
                    public void disconnect() {
                        Log.i(TAG, "disconnect: 服务器断开连接");
                    }
                });
            });

            Button btn_server_sendMsg = rootView.findViewById(R.id.btn_server_sendMsg);
            btn_server_sendMsg.setOnClickListener(v -> {
                if (connectedThread != null) {
//                    connectedThread.write("你好，我是服务器".getBytes(StandardCharsets.UTF_8));
/*                    String str = "7B22636F6465223A313031322C2264617461223A7B22646963506F777B22636F6465223A313031322C2264617461223A7B22646963506F777B22636F6465223A313031322C2264617461223A7B22646963506F777B22636F6465223A313031322C2264617461223A7B22646963506F777B22636F6465223A313031322C2264617461223A7B22646963506F777B22636F6465223A313031322C2264617461223A7B22646963506F777B22636F6465223A313031322C2264617461223A7B22646963506F777B22636F6465223A313031322C2264617461223A7B22646963506F777B22636F6465223A313031322C2264617461223A7B22646963506F777B22636F6465223A313031322C2264617461223A7B22646963506F77";
                    long startTime = System.currentTimeMillis();
                    // 模拟发送数据
                    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
                    scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                        long now = System.currentTimeMillis();
                        if (now - startTime >= 10000) {
                            scheduledThreadPoolExecutor.shutdown();
                        } else {
                            connectedThread.write(str.getBytes(StandardCharsets.UTF_8));
                        }
                    }, 0, 10, TimeUnit.MILLISECONDS);*/

                    String str;
                    // {"code":1018,"data":{"antennaEnable":72057594037927936,"inventoryMode":1,"filter":{"area":1,"bitStart":32,"bitLength":0,"hexData":"","bData":""},"readUserdata":{"len":8,"start":0}},"rtCode":0,"rtMsg":""}
                    str = "{\"code\":1012,\"rtCode\":0}";
                    connectedThread.write(MessageProtocolUtils.addTail(str).getBytes(StandardCharsets.UTF_8));
//                    str = "{\"code\":1013,\"data\":{\"dicPower\":{\"8\":20,\"7\":20,\"6\":20,\"5\":20,\"4\":20,\"3\":20,\"2\":20,\"1\":20}},\"rtCode\":0}";
//                    connectedThread.write(MessageProtocolUtils.addTail(str).getBytes(StandardCharsets.UTF_8));
//                    str = "{\"code\":1022,\"rtCode\":0}";
//                    connectedThread.write(MessageProtocolUtils.addTail(str).getBytes(StandardCharsets.UTF_8));
//                    str = "{\"code\":1023,\"data\":{\"freqRangeIndex\":\"Chinese band1\"},\"rtCode\":0}";
//                    connectedThread.write(MessageProtocolUtils.addTail(str).getBytes(StandardCharsets.UTF_8));
//                    str = "{\"code\":100,\"data\":\"1.0\",\"rtCode\":0}";
//                    connectedThread.write(MessageProtocolUtils.addTail(str).getBytes(StandardCharsets.UTF_8));
//                    str = "{\"code\":101,\"data\":\"123456789\",\"rtCode\":0}";
//                    connectedThread.write(MessageProtocolUtils.addTail(str).getBytes(StandardCharsets.UTF_8));
//                    str = "{\"code\":1001,\"data\":[{\"antId\":1,\"bEpc\":[68,68,102,102,-24,112,-38,-46,63,94,-7,-75],\"bRes\":[34,0],\"bTid\":[-30,-128],\"bUser\":[18,-1],\"epc\":\"44446666E870DAD23F5EF9B5\",\"pc\":\"3400\",\"readTime\":\"Nov 23, 2021 4:32:43 PM\",\"reserved\":\"2200\",\"result\":0,\"rssi\":-34,\"tid\":\"e280\",\"userdata\":\"12ff\"},{\"antId\":1,\"bEpc\":[-86,-86,-69,-52,-1,-1,-1,-1,76,-68,93,116],\"bRes\":[34,0],\"bTid\":[-30,-128],\"bUser\":[18,-1],\"epc\":\"AAAABBCCFFFFFFFF4CBC5D74\",\"pc\":\"3400\",\"readTime\":\"Nov 23, 2021 4:32:43 PM\",\"reserved\":\"2200\",\"result\":0,\"rssi\":-43,\"tid\":\"e280\",\"userdata\":\"12ff\"},{\"antId\":1,\"bEpc\":[-86,-86,51,51,-35,-39,1,64,0,0,0,115],\"bRes\":[0,0],\"bTid\":[-30,-128],\"bUser\":[18,52],\"epc\":\"AAAA3333DDD9014000000073\",\"pc\":\"3400\",\"readTime\":\"Nov 23, 2021 4:32:43 PM\",\"reserved\":\"0000\",\"result\":0,\"rssi\":-36,\"tid\":\"e280\",\"userdata\":\"1234\"}],\"rtCode\":0}";
//                    connectedThread.write(MessageProtocolUtils.addTail(str).getBytes(StandardCharsets.UTF_8));
                    str = "{\"code\":1018,\"rtCode\":0}";
                    connectedThread.write(MessageProtocolUtils.addTail(str).getBytes(StandardCharsets.UTF_8));

                }
            });
        }
        return rootView;
    }
}