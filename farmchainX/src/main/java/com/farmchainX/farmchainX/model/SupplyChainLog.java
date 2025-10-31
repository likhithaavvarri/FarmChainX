package com.farmchainX.farmchainX.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "supply_chain_log")
public class SupplyChainLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;          
    private Long fromUserId;         
    private Long toUserId;           
    private LocalDateTime timestamp; 
    private String location;         
    private String notes;            
    private String prevHash;         
    private String hash;             

  
    public SupplyChainLog() {
    }

    public SupplyChainLog(Long productId, Long fromUserId, Long toUserId,
                          LocalDateTime timestamp, String location, String notes,
                          String prevHash, String hash) {
        this.productId = productId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.timestamp = timestamp;
        this.location = location;
        this.notes = notes;
        this.prevHash = prevHash;
        this.hash = hash;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}