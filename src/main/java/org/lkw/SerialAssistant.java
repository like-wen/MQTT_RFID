package org.lkw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;


public class SerialAssistant implements MqttCallback {

@FXML
private Label paying;
@FXML
private Label paid;
@FXML
private Button collect_Money;
@FXML
private Rectangle doorColour;
@FXML
private TextField limit;


private ClientMQTT clientMQTT;
private ServiceMQTT serviceMQTT;
private Integer paidInt=0;

private Integer payingInt=0;

    public SerialAssistant() {
        //接收端开启
        clientMQTT=new ClientMQTT();
        clientMQTT.clientStart(this);
        //发送端开启
        serviceMQTT=new ServiceMQTT(this);



    }


    /*

    *//**
     * 清除框
     * @param actionEvent
     *//*
    public void clear(ActionEvent actionEvent) {
        receiveText.clear();
    }*/



    public void collectMoney(ActionEvent actionEvent) {


        String text = limit.getText();
        Integer limit=Integer.valueOf(text);
        System.out.println("限制:"+limit+"  累计"+payingInt);
        //如果收款超过最低限制,收款,开门
        if(limit<=payingInt){


            paid.setText(String.valueOf(paidInt+=payingInt));
            paying.setText("0");
            payingInt=0;
            //开门
            doorColour.setFill(new Color(0.255,0.69,0.0,1));
            serviceMQTT.publish(true);


        }

    }











    public void connectionLost(Throwable throwable) {
        //连接断掉会执行到这里
        System.out.println("连接以断，请重新连接！！！");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        //subscribe后会执行到这里

        String jsonStr = new String(mqttMessage.getPayload());
        System.out.println("消息的内容是："+jsonStr);

        ObjectMapper objectMapper = new ObjectMapper();


        Map<String, Object> stringObjectMap = objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>(){});
        Object rfid_915M = stringObjectMap.get("RFID_915M");
        if(rfid_915M!=null){
            System.out.println("识别出rfid");
            String str=(String)rfid_915M;
            if (str.equals("33D1097C")) {
                System.out.println("rfid0");
                payingInt+=10;
            }else
            if (str.equals("0645F6E6")) {
                System.out.println("rfid1");
               payingInt+=20;
            }else
            if (str.equals("33EEFFA2")) {
                System.out.println("rfid2");
                payingInt+=30;
            }

            Platform.runLater(()->{
                paying.setText(String.valueOf(payingInt));
                serviceMQTT.publish(false);
            });
            doorColour.setFill(new Color(0.84,0.255,0.159,1));//关门

        }


    }


    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //publish可以执行到这里
        System.out.println("发送成功");
    }
    public void setLabel(int rfidpay){
        paidInt+=rfidpay;
        paying.setText(String.valueOf(rfidpay));
        paid.setText(String.valueOf(paidInt));

    }
}

