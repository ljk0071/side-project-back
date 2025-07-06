package com.side.usecase.board;

import com.side.domain.Search;
import com.side.domain.model.PartyRecruit;
import com.side.domain.service.PartyRecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyRecruitUseCase {

    private final PartyRecruitService partyRecruitService;

    @Transactional
    public void create(PartyRecruit partyRecruit) {
        partyRecruitService.create(partyRecruit);
    }

    public List<PartyRecruit> getActiveRecruits(Search search) {

        return partyRecruitService.getActiveRecruits(search);
    }
}
