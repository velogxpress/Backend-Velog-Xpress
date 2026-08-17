package com.velogexpress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="surcursal_vs_machine")
public class Surcmachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "surcursal_id")
    private Surcursal surcursal;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    private Machine machine;
}
