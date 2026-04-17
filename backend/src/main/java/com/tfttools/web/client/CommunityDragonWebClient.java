package com.tfttools.web.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.tfttools.domain.communitydragon.CommunityDragonObject;
import com.tfttools.domain.communitydragon.TeamPlannerData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class CommunityDragonWebClient {


    //todo future https://raw.communitydragon.org/latest/game/data/characters/ to get img of chars

    private final WebClient webClient;
    
    public CommunityDragonWebClient() {

        ObjectMapper objectMapper = createCommunityDragonObjectMapper();
        this.webClient = WebClient.builder()
                .baseUrl("https://raw.communitydragon.org")
                .defaultHeader(HttpHeaders.USER_AGENT, "TFTTools/1.0")
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024); // 50MB
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
                })
                .build();
    }
    
    public Mono<CommunityDragonObject> fetchData() {
        return webClient.get()
            .uri("/latest/cdragon/tft/en_us.json")
            .retrieve()
            .bodyToMono(CommunityDragonObject.class)
            .timeout(Duration.ofSeconds(30));
    }

    public Mono<TeamPlannerData> fetchTeamPlannerData() {
        return webClient.get()
                .uri("/latest/plugins/rcp-be-lol-game-data/global/default/v1/tftchampions-teamplanner.json")
                .retrieve()
                .bodyToMono(TeamPlannerData.class)
                .timeout(Duration.ofSeconds(30));
    }

    public Mono<CommunityDragonObject> fetchDataIfModified(String ifModifiedSince) {
        return webClient.get()
            .uri("/latest/cdragon/tft/en_us.json")
            .header(HttpHeaders.IF_MODIFIED_SINCE, ifModifiedSince)
            .retrieve()
            .onStatus(HttpStatus.NOT_MODIFIED::equals, response -> Mono.empty())
            .bodyToMono(CommunityDragonObject.class);
    }

    public static ObjectMapper createCommunityDragonObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
