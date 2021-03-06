package com.dsvl.flood.model;

import com.dsvl.flood.util.Counter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date timestamp;
    private String sender;
    private String receiver;
    private String protocol;
    private String message;

    public Log() {
        this.timestamp = new Date();
        this.id = Counter.nextId();
    }

    public Log(String sender, String receiver, String protocol, String message) {
        this();
        this.sender = sender;
        this.receiver = receiver;
        this.protocol = protocol;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", protocol='" + protocol + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
