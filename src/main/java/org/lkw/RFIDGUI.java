package org.lkw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.Map;


public class RFIDGUI implements MqttCallback {

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
    @FXML
    private TextArea allText;
    @FXML
    private Text warning;


    private ClientMQTT clientMQTT;
    private ServiceMQTT serviceMQTT;
    private Integer paidInt = 0;
    private Integer payingInt = 0;
    private Map<String,Integer> totalId=new HashMap<>();

    public RFIDGUI() {
        //接收端开启
        clientMQTT = new ClientMQTT();
        clientMQTT.clientStart(this);
        //发送端开启
        serviceMQTT = new ServiceMQTT(this);

    }


    public void collectMoney(ActionEvent actionEvent) {
        String text = limit.getText();
        Integer limit = Integer.valueOf(text);
        System.out.println("限制:" + limit + "  累计" + payingInt);
        //如果收款超过最低限制,收款,开门
        if (limit <= payingInt) {
            paid.setText(String.valueOf(paidInt += payingInt));
            paying.setText("0");
            payingInt = 0;
            totalId=new HashMap<>();
            //开门
            doorColour.setFill(new Color(0.255, 0.69, 0.0, 1));
            serviceMQTT.publish(true);
            System.out.println("开门");
            allText.setText("");
        } else {
            warning.setText("收款不足" + limit + "元,请继续选购商品");
        }
    }


    public void connectionLost(Throwable throwable) {
        //连接断掉会执行到这里
        System.out.println("连接以断，请重新连接！！！");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        //subscribe后会执行到这里
        String jsonStr = new String(mqttMessage.getPayload());
        System.out.println("消息的内容是：" + jsonStr);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> stringObjectMap = objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {});
        Object rfid_915M = stringObjectMap.get("RFID_915M");
        Platform.runLater(() -> {
            if (rfid_915M != null) {
                String id = (String) rfid_915M;
                System.out.println("id为"+id);
                //查数据库
                RfidLink rfidLink = Select.selectById(id);
                if(!totalId.containsKey(id)){
                    if(rfidLink.getId()==null){
                        warning.setText("有未被识别商品,请联系工作人员");
                    }else{
                        sendMessage(rfidLink.getName() + "  价格:" + rfidLink.getValue() + "  详细:" + rfidLink.getOther());
                        //存储到map
                        totalId.put(id,rfidLink.getValue());
                        //计钱
                        payingInt += rfidLink.getValue();
                        paying.setText(String.valueOf(payingInt));
                        //关门
                        serviceMQTT.publish(false);
                        doorColour.setFill(new Color(0.84, 0.255, 0.159, 1));
                    }
                }
            }
        });
    }


    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //publish可以执行到这里
        System.out.println("发送成功");
    }
    public void sendMessage(String str){
        allText.appendText(str+"\r\n");
    }
    public void flashNow(){
        totalId=new HashMap<>();
        allText.setText("");
        payingInt=0;
        paying.setText("0");
    }
}

