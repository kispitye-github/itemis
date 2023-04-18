package hu.kispitye.itemis.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import hu.kispitye.itemis.user.service.UserService;
import hu.kispitye.itemis.user.User;
import hu.kispitye.itemis.GitHubHack;
import hu.kispitye.itemis.Security;
import hu.kispitye.itemis.analyze.service.AnalyzerService;

@Import(Security.class)
@WebMvcTest
@TestExecutionListeners({GitHubHack.class, DependencyInjectionTestExecutionListener.class})
public class HomeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    
    @MockBean
    private AnalyzerService analyzerService;
    
    private User user = new User("user", "userpwd");

    private User admin = new User("admin", "adminpwd").setAdmin(true);

    @Test
    public void testRoot(@Value("${"+HomeController.PATH_ROOT+"}") String rootPath) throws Exception {
        mvc.perform(get(rootPath))
        .andExpect(status().isOk())
        .andExpect(view().name(HomeController.VIEW_HOME));
        mvc.perform(get(rootPath).with(anonymous()))
          .andExpect(status().isOk())
          .andExpect(view().name(HomeController.VIEW_HOME));
        mvc.perform(get(rootPath).with(user(user)))
        .andExpect(status().isOk())
        .andExpect(view().name(HomeController.VIEW_HOME));
        mvc.perform(get(rootPath).with(user(admin)))
        .andExpect(status().isOk())
        .andExpect(view().name(HomeController.VIEW_HOME));
     }

}
