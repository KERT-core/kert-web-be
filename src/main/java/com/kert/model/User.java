package com.kert.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`user`")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private Long studentId;

    //update에 userdetail 객체가 user 쓰는데 문제의 소지 있음. 비번 변경은 거기서 할 생각이 없음. nullable false임
    @JsonIgnore
    @Column(nullable = false)
    private String hash;

    private String name;
    private String email;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String profilePicture;
    private String generation;
    private String major;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
