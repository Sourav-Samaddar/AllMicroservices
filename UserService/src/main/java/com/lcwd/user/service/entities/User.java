package com.lcwd.user.service.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "micro_users")
@Data                   // Generates getters, setters, toString, equals, and hashCode
@Builder
@NoArgsConstructor      // Generates a no-arg constructor
@AllArgsConstructor     // Generates an all-arg constructor
public class User {

    @Id
    @Column(name = "ID")
    private String userId;

    @Column(name = "NAME",length = 50)
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ABOUT")
    private String about;
    
    @Transient
    private List<Rating> ratings = new ArrayList<>();
}
