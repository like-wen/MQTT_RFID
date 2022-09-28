package org.lkw;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ClientMQTT {

    public static final String HOST = "tcp://192.168.199.132:1883";
    private static final String clientID = "clientFirst";
    private String[] TOPICs={"AIOTSIM2APP"};
    private MqttClient client;
    private MqttConnectOptions options;
    private String user = "admin";
    private String password = "public";


    public void clientStart(RFIDGUI serialAssistant){
        try {
            client = new MqttClient(HOST,clientID,new MemoryPersistence());
            options = new MqttConnectOptions();
            client.setCallback(serialAssistant);
            client.connect(options);
            client.subscribe(TOPICs);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}