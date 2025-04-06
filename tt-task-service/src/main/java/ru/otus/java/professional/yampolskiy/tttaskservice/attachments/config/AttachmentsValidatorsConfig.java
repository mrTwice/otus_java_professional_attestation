package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.entities.Attachment;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.validators.AttachmentValidator;
import ru.otus.java.professional.yampolskiy.tttaskservice.common.DomainValidator;

@Configuration
public class AttachmentsValidatorsConfig {
    @Bean(name = "attachmentValidator")
    public DomainValidator<Attachment> attachmentValidator() {
        return new AttachmentValidator();
    }

}
