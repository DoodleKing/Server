package mana.doodleking.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
            .title("DoodleKing")
            .description("""
                socket 관련 명세인 경우 해당 메시지(socket) 형식으로 문서화를 하였습니다.\n
                HTTP 명세의 경우는 테스트가 가능하지만, socket 관련 명세는 테스트가 불가능합니다.
                """
            )
            .version("1.0.0");
    }
}
