package com.side.domain.repository;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.model.PartyApplication;

public interface PartyApplicationWriter {

    long create(PartyApplication partyApplication);

    void changeStatus(long partyApplicationId, PartyApplicationStatusTypeEnum statusType);
}
