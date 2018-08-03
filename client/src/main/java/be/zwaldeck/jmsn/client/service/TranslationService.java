package be.zwaldeck.jmsn.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Service
public class TranslationService {

    public enum Language {
        EN("en-US"),
        NL("nl-BE");

        private Locale locale;

        Language(String lang) {
            locale = Locale.forLanguageTag(lang);
        }

        public Locale getLocale() {
            return locale;
        }
    }

    private final MessageSource messageSource;

    private Locale currentLanguage;

    @Autowired
    public TranslationService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    public void postConstruct() {
        // TODO load from config
        currentLanguage = Language.EN.getLocale();
    }

    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, currentLanguage);
    }
}
