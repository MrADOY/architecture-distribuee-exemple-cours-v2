package fr.uphf.comptes.ressources;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping
public class CompteRessource {

    @Autowired
    private WebClient.Builder webClient;

    @Builder
    @Getter
    @Setter
    public static class CompteDetailDTO {
        private String id;
        public List<Carte> cartes;

        @Builder
        @Getter
        @Setter
        public static class Carte {
            private String numero;
            private LocalDateTime dateExpiration;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompteDetailDTO> getCompte(@PathVariable("id") String id) {
        CompteDetailDTO.Carte[] cartesFromApi = webClient.baseUrl("http://cartes/")
                .build()
                .get()
                .uri("/cartes")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CompteDetailDTO.Carte[].class)
                .block();
        return ResponseEntity.ok(CompteDetailDTO.builder().id(id).cartes(Arrays.asList(cartesFromApi)).build());
    }

}
