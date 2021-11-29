package dev.gustavoteixeira.votingsession.client;

import dev.gustavoteixeira.votingsession.dto.UserInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserInfoClient {

    private final WebClient webClient;

    public UserInfoClient(@Value("${clients.user-info.url}") String url) {
        webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mono<UserInfoDto> getUserInfo(final String cpf) {
        return webClient
                .get()
                .uri("users/{cpf}", cpf)
                .retrieve()
                .bodyToMono(UserInfoDto.class);
    }

}
