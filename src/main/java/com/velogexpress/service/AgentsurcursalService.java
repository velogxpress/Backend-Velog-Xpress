package com.velogexpress.service;

import com.velogexpress.model.AgentsurcursalModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AgentsurcursalService {

    AgentsurcursalModel createAgentsurcursal(AgentsurcursalModel model);

    Page<AgentsurcursalModel> getAllAgentsurcursals(Pageable pageable);

    Page<AgentsurcursalModel> searchByUserCode(String userCode, Pageable pageable);

    AgentsurcursalModel getByUserCode(String userCode);

    AgentsurcursalModel updateByUserCode(String userCode, AgentsurcursalModel model);

    void deleteByUserCode(String userCode);
}
