package com.velogexpress.mapper;

import com.velogexpress.entity.Agentsurcursal;
import com.velogexpress.model.AgentsurcursalModel;

public class AgentsurcursalMapper {
    public static AgentsurcursalModel mapToAgentsurcursalModel(Agentsurcursal agentsurcursal){
        return new AgentsurcursalModel(
                agentsurcursal.getId(),
                agentsurcursal.getClient(),
                agentsurcursal.getSurcursal()
        );
    }

    public static Agentsurcursal mapToAgentsurcursal(AgentsurcursalModel agentsurcursalModel){
        return new Agentsurcursal(
                agentsurcursalModel.getId(),
                agentsurcursalModel.getClient(),
                agentsurcursalModel.getSurcursal()
        );
    }
}
