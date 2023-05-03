package hu.kispitye.itemis.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import hu.kispitye.itemis.user.service.UserService;
import hu.kispitye.itemis.user.User;
import hu.kispitye.itemis.GitHubHack;
import hu.kispitye.itemis.Security;
import hu.kispitye.itemis.analyze.service.AnalyzerService;

import static hu.kispitye.itemis.ItemisConstants.*;

@Import(Security.class)
@WebMvcTest
@TestExecutionListeners({GitHubHack.class, DependencyInjectionTestExecutionListener.class})
public class HomeControllerTest {

    @Autowired
    private MockMvc mvc;
    
	@Autowired
	private MessageSource messageSource;

    @MockBean
    private UserService userService;
    
    @MockBean
    private AnalyzerService analyzerService;
    
    private User user = new User("user", "userpwd");

    private User admin = new User("admin", "adminpwd").setAdmin(true);
    
	private String getMessage(String key, Object... args) {
		return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
	}

    @Test
    public void testRoot(@Value("${"+PATH_ROOT+"}") String rootPath) throws Exception {
    	LocaleContextHolder.setLocale(Locale.ENGLISH);
        mvc.perform(get(rootPath))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_HOME))
        .andExpect(content().string(containsString(getMessage(APP_EMPTY))));
        mvc.perform(get(rootPath).with(anonymous()))
          .andExpect(status().isOk())
          .andExpect(view().name(VIEW_HOME));
        mvc.perform(get(rootPath).with(user(user)))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_HOME));
        mvc.perform(get(rootPath).with(user(admin)))
        .andExpect(status().isOk())
        .andExpect(view().name(VIEW_HOME));
     }

}
