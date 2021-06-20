package org.emu.membership.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

    public static final String TAG_MEMBER = "tag-member";

    @Bean
    public OpenAPI customOpenAPI() {
        final Info info = new Info()
                .title("EMU API's")
                .description("EMU API's documentation")
                .version("1.0.0");

        return new OpenAPI().components(new Components())
                .addTagsItem(createTag(TAG_MEMBER, "Member API endpoints"))
                // Other tags here...
                .info(info);
    }

    private Tag createTag(String name, String description) {
        final Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription(description);
        return tag;
    }

}
