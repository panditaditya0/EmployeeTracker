package com.ppus.psl_user_heartbeat.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user_analytics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserAnalytics {
    public UserAnalytics(String userId, String action, String link, LocalDateTime dateTime) {
        this.setAction(action);
        this.setLink(link);
        this.setUserId(userId);
        this.setDateTime(dateTime);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "date")
    private LocalDateTime dateTime;
    private String link;
    @Column(name = "action")
    private String action;
}
