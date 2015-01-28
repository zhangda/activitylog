package cn.rfidcn.activitylog;

import java.util.Properties; 

import kafka.javaapi.producer.Producer; 
import kafka.producer.KeyedMessage; 
import kafka.producer.ProducerConfig; 
public class KafkaTest {
	
	
//	public static void main(String[] args) {  
//		 Properties props = new Properties();   
//         props.setProperty("metadata.broker.list","test-broker2.sao.so:9092");   
////		 props.setProperty("metadata.broker.list","192.168.8.104:9092");   
//         props.setProperty("serializer.class","kafka.serializer.StringEncoder");   
//         props.put("request.required.acks","1");   
//         ProducerConfig config = new ProducerConfig(props);   
//         Producer<String, String> producer = new Producer<String, String>(config);   
////         KeyedMessage<String, String> data = new KeyedMessage<String, String>("test-topic","hallo");   
//         KeyedMessage<String, String> data = new KeyedMessage<String, String>("test","hallo");   
//         try {   
//                 producer.send(data);   
//         } catch (Exception e) {   
//             e.printStackTrace();   
//         }   
//         producer.close();   
//    } 

}
