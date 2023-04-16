package hu.kispitye.itemis.analyze;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import hu.kispitye.itemis.analyze.service.AnalyzerService;
import hu.kispitye.itemis.analyze.service.AnalyzerService.Result;
import hu.kispitye.itemis.dao.HibernateRepository;
import hu.kispitye.itemis.analyze.service.AnalyzerServiceImpl;
import hu.kispitye.itemis.item.Item;
import hu.kispitye.itemis.item.dao.ItemRepository;
import hu.kispitye.itemis.item.service.ItemService;
import hu.kispitye.itemis.item.service.ItemServiceImpl;
import hu.kispitye.itemis.unit.Unit;
import hu.kispitye.itemis.unit.dao.UnitRepository;
import hu.kispitye.itemis.unit.service.UnitService;
import hu.kispitye.itemis.unit.service.UnitServiceImpl;
import hu.kispitye.itemis.user.User;
import hu.kispitye.itemis.user.dao.UserRepository;
import hu.kispitye.itemis.user.service.UserService;
import hu.kispitye.itemis.user.service.UserServiceImpl;

@DataJpaTest
@EntityScan(basePackageClasses = {User.class, Item.class, Unit.class})
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ItemRepository.class, UnitRepository.class, HibernateRepository.class})
public class AnalyzerServiceTest {
	
    private static final ThreadLocal<User> user = new ThreadLocal<>();
	
	@TestConfiguration
    static class Config {

		@Bean
         AnalyzerService getAnalyzerService() {
            return new AnalyzerServiceImpl();
            
        }
         @Bean
         UserService getUserService() {
            return new UserServiceImpl() {
          	 @Override
				public User getCurrentUser() {
          		 	return user.get();
        		}
            };
        }
         @Bean
         ItemService getItemService() {
            return new ItemServiceImpl();
            
        }
         @Bean
         UnitService getUnitService() {
            return new UnitServiceImpl();
        }
         @Bean
     	static PasswordEncoder passwordEncoder() {
             return new BCryptPasswordEncoder();
         }

         @Bean
         MessageSource messageSource() {
             ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
             messageSource.setBasename("classpath:i18n/messages");
             messageSource.setDefaultEncoding("UTF-8");
             return messageSource;
         }
    }
    
    @Autowired
	AnalyzerService analyzerService;

    @Autowired
    UserService userService;
	
    @Autowired
    ItemService itemService;
	
    @Autowired
    UnitService unitService;

	@Autowired
	private MessageSource messageSource;
	
	private String getMessage(String key, Object... args) {
		return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
	}

    private String getAnswer(hu.kispitye.itemis.analyze.service.AnalyzerService.Result result) {
    	return String.join(" ", result.getResult());
    }
	
    @BeforeEach
    void setUser() {
    	assertThat(user.get()).isNull();
		user.set(userService.createUser("user", "name"));
    }
    
    @AfterEach
    void clearUser() {
    	assertThat(user.get()).isNotNull();
    	user.remove();
    }
    
    @Test
    void testAutowired() {
    	assertThat(analyzerService).isNotNull();
    	assertThat(userService).isNotNull();
    	assertThat(itemService).isNotNull();
    	assertThat(unitService).isNotNull();
    }

    @Test
    void testEmpty() {
    	Result result = analyzerService.analyze("");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(1);
    	assertThat(result.getResult()).isEmpty();
    	hu.kispitye.itemis.analyze.service.AnalyzerService.Result.Error error = result.getErrors().get(0);
    	assertThat(error.key()).isEqualTo(AnalyzerServiceImpl.MAIN_UNCLASSIFIED);
    	assertThat(error.sender()).isEqualTo("Run");
    }

    @Test
    void testGlob() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
    	Result result = analyzerService.analyze("glob is I");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("New unit created: [glob = I]");
    }

    @Test
    void testProk() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
    	Result result = analyzerService.analyze("prok is V");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("New unit created: [prok = V]");
    }

    @Test
    void testPish() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
    	Result result = analyzerService.analyze("pish is X");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("New unit created: [pish = X]");
    }

    @Test
    void testTegj() {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
    	Result result = analyzerService.analyze("tegj is L");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("New unit created: [tegj = L]");
    }

    @Test
    void testSilver() {
    	testGlob();
    	Result result = analyzerService.analyze("glob glob Silver is 34 Credits");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("New item created: [Silver = 17]");
    }

    @Test
    void testGold() {
    	testGlob();
    	testProk();
    	Result result = analyzerService.analyze("glob prok Gold is 57800 Credits");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("New item created: [Gold = 14450]");
    }

    @Test
    void testIron() {
    	testPish();
    	Result result = analyzerService.analyze("pish pish Iron is 3910 Credits");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("New item created: [Iron = 195.5]");
    }

    @Test
    void testQuestion() {
    	testPish();
    	testTegj();
    	testGlob();
    	Result result = analyzerService.analyze("how much is pish tegj glob glob ?");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("pish tegj glob glob is XLII (42)");
    }

    @Test
    void testQuestionSilver() {
    	testProk();
    	testSilver();
    	Result result = analyzerService.analyze("how many Credits is glob prok Silver ?");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("glob prok Silver is 68 Credits");
    }

    @Test
    void testQuestionGold() {
    	testGold();
    	Result result = analyzerService.analyze("how many Credits is glob prok Gold ?");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("glob prok Gold is 57800 Credits");
    }

    @Test
    void testQuestionIron() {
    	testGlob();
    	testProk();
    	testIron();
    	Result result = analyzerService.analyze("how many Credits is glob prok Iron ?");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(0);
    	assertThat(getAnswer(result)).isEqualTo("glob prok Iron is 782.0 Credits");
    }

    @Test
    void testLastQuestion() {
    	Result result = analyzerService.analyze("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?");
    	assertThat(result).isNotNull();
    	assertThat(result.getErrors().size()).isEqualTo(1);
    	hu.kispitye.itemis.analyze.service.AnalyzerService.Result.Error error = result.getErrors().get(0);
    	assertThat(error.key()).isEqualTo(AnalyzerServiceImpl.QUESTION_UNKNOWN);
    	assertThat(error.sender()).isEqualTo("answer");
    	assertThat(getMessage(AnalyzerServiceImpl.QUESTION_UNKNOWN, (Object[]) null)).contains("I have no idea what you are talking about");
    }

}
