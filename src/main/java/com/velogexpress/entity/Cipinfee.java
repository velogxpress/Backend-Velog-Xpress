package com.velogexpress.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "city_vs_fees")
public class Cipinfee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ville_id", unique = true) // kept as-is (production safe)
    private Ville city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feepound_id")
    private Feepounds pounds;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inssurance_id")
    private Insurance insurance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialfee_id")
    private Specialfee specialfee;
}
