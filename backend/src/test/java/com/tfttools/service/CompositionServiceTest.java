package com.tfttools.service;

import com.tfttools.adapter.EngineConfigurationAdapter;
import com.tfttools.domain.Unit;
import com.tfttools.mapper.CompositionMapper;
import com.tfttools.repository.UnitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Scoped to {@link CompositionService#generateTeamCode(List)} only - the rest of
 * {@code CompositionService} (the engine pipeline) has no active automated coverage today (see
 * CLAUDE.md's note on TFTEngineTest/PrefixTrieTest).
 */
@ExtendWith(MockitoExtension.class)
class CompositionServiceTest
{
    @Mock
    private EngineConfigurationAdapter adapter;

    @Mock
    private CompositionMapper compositionMapper;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private TeamPlannerService teamPlannerService;

    private CompositionService compositionService;

    @org.junit.jupiter.api.BeforeEach
    void setUp()
    {
        compositionService = new CompositionService(adapter, compositionMapper, unitRepository, teamPlannerService);
    }

    @Test
    void generateTeamCode_resolvesApiNamesAndDelegatesToTeamPlannerService()
    {
        Unit ahri = new Unit("TFT18_Ahri", "Ahri", 4, null, null, List.of(), null);
        when(unitRepository.getUnitByApiName("TFT18_Ahri")).thenReturn(ahri);
        when(teamPlannerService.exportToTeamCode(List.of(ahri))).thenReturn("02abc18");

        String teamCode = compositionService.generateTeamCode(List.of("TFT18_Ahri"));

        assertThat(teamCode).isEqualTo("02abc18");
    }

    @Test
    void generateTeamCode_throwsForUnknownApiName()
    {
        when(unitRepository.getUnitByApiName("TFT18_Bogus")).thenReturn(null);

        assertThatThrownBy(() -> compositionService.generateTeamCode(List.of("TFT18_Bogus")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TFT18_Bogus");
    }
}
