package com.tfttools.comps.service;

import com.tfttools.comps.domain.Comp;
import com.tfttools.comps.domain.Placement;
import com.tfttools.comps.dto.CompResponse;
import com.tfttools.comps.dto.PlacementDTO;
import com.tfttools.comps.dto.SaveCompRequest;
import com.tfttools.comps.exception.CompLimitExceededException;
import com.tfttools.comps.exception.CompNotFoundException;
import com.tfttools.comps.repository.CompRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompServiceTest
{
    @Mock
    private CompRepository compRepository;

    private CompService compService;

    @org.junit.jupiter.api.BeforeEach
    void setUp()
    {
        compService = new CompService(compRepository);
    }

    @Test
    void save_persistsPlacements_whenUnderCap()
    {
        UUID userId = UUID.randomUUID();
        SaveCompRequest request = new SaveCompRequest(List.of(new PlacementDTO("TFT18_Ahri", 1, 3)));
        when(compRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of());
        when(compRepository.countByUserId(userId)).thenReturn(3);
        when(compRepository.save(any(Comp.class))).thenAnswer(invocation ->
        {
            Comp comp = invocation.getArgument(0);
            comp.setId(UUID.randomUUID());
            comp.setCreatedAt(Instant.now());
            return comp;
        });

        CompResponse response = compService.save(userId, request);

        assertThat(response.placements()).hasSize(1);
        assertThat(response.placements().get(0).unitApiName()).isEqualTo("TFT18_Ahri");

        ArgumentCaptor<Comp> savedComp = ArgumentCaptor.forClass(Comp.class);
        verify(compRepository).save(savedComp.capture());
        assertThat(savedComp.getValue().getUserId()).isEqualTo(userId);
        assertThat(savedComp.getValue().getPlacements()).containsExactly(new Placement("TFT18_Ahri", 1, 3));
    }

    @Test
    void save_throwsCompLimitExceeded_atCap_andNeverSaves()
    {
        UUID userId = UUID.randomUUID();
        SaveCompRequest request = new SaveCompRequest(List.of(new PlacementDTO("TFT18_Ahri", 1, 3)));
        when(compRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of());
        when(compRepository.countByUserId(userId)).thenReturn(25);

        assertThatThrownBy(() -> compService.save(userId, request))
                .isInstanceOf(CompLimitExceededException.class);

        verify(compRepository, never()).save(any());
    }

    @Test
    void save_overwritesExistingComp_whenSameUnitSetAtDifferentPositions()
    {
        UUID userId = UUID.randomUUID();
        Comp existing = new Comp();
        existing.setId(UUID.randomUUID());
        existing.setUserId(userId);
        existing.setPlacements(List.of(new Placement("TFT18_Ahri", 1, 3), new Placement("TFT18_Jinx", 0, 0)));
        existing.setCreatedAt(Instant.now().minusSeconds(60));

        // Same two units, different board positions
        SaveCompRequest request = new SaveCompRequest(List.of(
                new PlacementDTO("TFT18_Jinx", 2, 5),
                new PlacementDTO("TFT18_Ahri", 3, 6)
        ));
        when(compRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of(existing));
        when(compRepository.update(any(Comp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompResponse response = compService.save(userId, request);

        assertThat(response.id()).isEqualTo(existing.getId());

        ArgumentCaptor<Comp> updatedComp = ArgumentCaptor.forClass(Comp.class);
        verify(compRepository).update(updatedComp.capture());
        assertThat(updatedComp.getValue().getId()).isEqualTo(existing.getId());
        assertThat(updatedComp.getValue().getPlacements()).containsExactlyInAnyOrder(
                new Placement("TFT18_Jinx", 2, 5), new Placement("TFT18_Ahri", 3, 6));

        verify(compRepository, never()).save(any());
        verify(compRepository, never()).countByUserId(any());
    }

    @Test
    void save_overwritesExistingComp_evenAtCap()
    {
        UUID userId = UUID.randomUUID();
        Comp existing = new Comp();
        existing.setId(UUID.randomUUID());
        existing.setUserId(userId);
        existing.setPlacements(List.of(new Placement("TFT18_Ahri", 1, 3)));
        existing.setCreatedAt(Instant.now().minusSeconds(60));

        SaveCompRequest request = new SaveCompRequest(List.of(new PlacementDTO("TFT18_Ahri", 5, 5)));
        when(compRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of(existing));
        when(compRepository.update(any(Comp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompResponse response = compService.save(userId, request);

        assertThat(response.id()).isEqualTo(existing.getId());
        verify(compRepository, never()).countByUserId(any());
        verify(compRepository, never()).save(any());
    }

    @Test
    void list_returnsUserCompsMappedToResponses()
    {
        UUID userId = UUID.randomUUID();
        Comp comp = existingComp(userId);
        when(compRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of(comp));

        List<CompResponse> responses = compService.list(userId);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).id()).isEqualTo(comp.getId());
    }

    @Test
    void delete_removesComp_whenOwnedByUser()
    {
        UUID userId = UUID.randomUUID();
        UUID compId = UUID.randomUUID();
        when(compRepository.deleteByIdAndUserId(compId, userId)).thenReturn(true);

        compService.delete(userId, compId);

        verify(compRepository).deleteByIdAndUserId(compId, userId);
    }

    @Test
    void delete_throwsCompNotFound_whenNotOwnedOrMissing()
    {
        UUID userId = UUID.randomUUID();
        UUID compId = UUID.randomUUID();
        when(compRepository.deleteByIdAndUserId(compId, userId)).thenReturn(false);

        assertThatThrownBy(() -> compService.delete(userId, compId))
                .isInstanceOf(CompNotFoundException.class);
    }

    private static Comp existingComp(UUID userId)
    {
        Comp comp = new Comp();
        comp.setId(UUID.randomUUID());
        comp.setUserId(userId);
        comp.setPlacements(List.of(new Placement("TFT18_Ahri", 1, 3)));
        comp.setCreatedAt(Instant.now());
        return comp;
    }
}
