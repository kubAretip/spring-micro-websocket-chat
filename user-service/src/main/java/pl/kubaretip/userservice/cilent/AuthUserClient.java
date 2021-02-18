package pl.kubaretip.userservice.cilent;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.kubaretip.dtomodels.AuthUserDTO;
import pl.kubaretip.userservice.exception.ClientException;

@Component
public class AuthUserClient {

    private final static String AUTH_SERVICE_URL = "http://auth/auth-users";
    private final RestTemplate restTemplate;

    public AuthUserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createAuthUser(String username, String password) {

        var authUserDTO = new AuthUserDTO();
        authUserDTO.setPassword(password);
        authUserDTO.setUsername(username);
        try {
            restTemplate.postForEntity(AUTH_SERVICE_URL, authUserDTO, AuthUserDTO.class);
        } catch (HttpClientErrorException ex) {
            throw new ClientException(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }


}
