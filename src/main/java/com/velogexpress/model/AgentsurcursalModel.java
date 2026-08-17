package com.velogexpress.model;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.entity.Surcursal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentsurcursalModel {
    private Long id;
    private Clientregister client;
    private Surcursal surcursal;
}
