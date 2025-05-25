package com.sh32bit.airline.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("AGENT")
public class Agent extends User {
    @ManyToOne(optional = true)
    private Company company;

    @ManyToOne(optional = true)
    private Airport airport;

    @OneToMany(mappedBy = "agent")
    private List<Flight> flights;
}
