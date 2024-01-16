package com.anotaai.anotaai.services.aws;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AwsSnsService {
    AmazonSNS snsClient;
    Topic topic;

    public AwsSnsService(AmazonSNS snsClient,@Qualifier("catalogEventsTopic") Topic topic){
        this.snsClient = snsClient;
        this.topic = topic;
    }

    public void publish(MessageDTO message){
        this.snsClient.publish(topic.getTopicArn(), message.toString());
    }
}
