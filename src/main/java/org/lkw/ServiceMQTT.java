package org.lkw;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ServiceMQTT {

    public static final String HOST = "tcp://localhost:1883";
    private String ServiceID = "ServiceFirst";
    private String topic="APP2AIOTSIM";
    private MqttClient client;
    private MqttTopic mqttTopic;
    private MqttConnectOptions options;

    private String user = "admin";

    private String password = "public";

    private MqttMessage message;

    public ServiceMQTT(SerialAssistant serialAssistant) {
        //创建对象
        try {
            client = new MqttClient(HOST,ServiceID,new MemoryPersistence());
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        options = new MqttConnectOptions();

        //连接
        try {
            client.setCallback(serialAssistant);
            client.connect(options);
            mqttTopic = client.getTopic(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    public void publish(boolean b)  {
        try {
            String str = "{\"barrierGate\":"+b+",\"id\":0}";
            System.out.println("发送的json为"+str);
            message = new MqttMessage();
            message.setPayload(str.getBytes());
            mqttTopic.publish(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}