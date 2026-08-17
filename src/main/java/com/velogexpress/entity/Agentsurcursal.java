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
@Table(name = "agent_vs_surcursal")
public class Agentsurcursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Clientregister client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "surcursal_id")
    private Surcursal surcursal;
}
