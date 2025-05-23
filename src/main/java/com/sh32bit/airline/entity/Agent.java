package com.sh32bit.airline.entity;

import jakarta.persistence.*;
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
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "airport_id"})
})
public class Agent extends User {
    @ManyToOne(optional = false)
    private Company company;

    @ManyToOne(optional = false)
    private Airport airport;

    @OneToMany(mappedBy = "agent")
    private List<Flight> flights;
}
