package supermed.web.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import supermed.datamanagementsystem.impl.DataManagerImpl;
import supermed.usermanagementsystem.impl.UserManagerImpl;
import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestApplicationTest extends JerseyTest {

    RestApplication restApplication;
    DataManagerImpl dataManager;
    UserManagerImpl userManager;
    HttpServletRequest currentRequest;
    User testUser = new User();
    HttpSession session;

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Before
    public void before() {
        userManager.setDataManager(dataManager);
        UserData userData = UserData.newBuilder().setEmail("tets")
                .setFirstName("test")
                .setLastName("test")
                .setLogin("test")
                .setBirthDate("test")
                .setMiddleName("test")
                .setAddress("test")
                .setPhoneNumber("test")
                .build();
        testUser.setUserData(userData);
        testUser.setRole(Role.createRole("patient"));
        session = mock(HttpSession.class);
        when(currentRequest.getSession()).thenReturn(session);
        session.setAttribute("User", testUser);
    }

    @Test
    public void testRestExample() {
        when(userManager.logIn("test", "test")).thenReturn(testUser);
        Response response = target("/login")
                .register(RedirectTestFilter.class)
                .request().post(entity(new Form().param("login", "test").param("password",
                        "test"), MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        collector.checkThat(response.getStatus(), equalTo(OK.getStatusCode()));
    }

    @Override
    protected Application configure() {
        this.restApplication = new RestApplication();
        userManager = mock(UserManagerImpl.class);
        currentRequest = mock(HttpServletRequest.class);
        restApplication.userManagerImpl = this.userManager;
        return new ResourceConfig().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(currentRequest).to(HttpServletRequest.class);
            }
        }).register(restApplication);
    }

    private static class RedirectTestFilter implements ClientResponseFilter {
        public static final String RESOLVED_URI_HEADER = "resolved-uri";

        @Override
        public void filter(ClientRequestContext requestContext, ClientResponseContext
                responseContext) throws IOException {
            if (responseContext instanceof ClientResponse) {
                ClientResponse clientResponse = (ClientResponse) responseContext;
                responseContext.getHeaders().putSingle(RESOLVED_URI_HEADER, clientResponse
                        .getResolvedRequestUri().toString());
            }
        }
    }
}
