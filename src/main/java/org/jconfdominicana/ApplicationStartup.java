package org.jconfdominicana;

import com.vaadin.flow.server.ServiceInitEvent;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.repositories.common.UserRepository;

import java.util.List;

@ApplicationScoped
public class ApplicationStartup {

//    @Inject
//    UserRepository userRepository;
//
//    @Transactional
//    public void start(@Observes StartupEvent startupEvent) {
//
//        User admin = User.builder()
//                .username("admin")
//                .password(BcryptUtil.bcryptHash("admin"))
////                .role(Role.ADMIN)
//                .build();
//
//        User user = User.builder()
//                .username("user")
//                .password(BcryptUtil.bcryptHash("user"))
////                .role(Role.USER.name())
//                .build();
//
//        userRepository.insertAll(List.of(admin, user));
//    }

    public void startUp(@Observes ServiceInitEvent event) {
        event.addIndexHtmlRequestListener(response -> {
            System.out.println(response);
            // IndexHtmlRequestListener to change the bootstrap page
        });

        event.addDependencyFilter((dependencies, filterContext) -> {
            // DependencyFilter to add/remove/change dependencies sent to
            // the client
            System.out.println(dependencies);
            return dependencies;
        });

        event.addRequestHandler((session, request, response) -> {

            System.out.println(session + " " + request + " " + response);
            // RequestHandler to change how responses are handled
            return false;
        });
    }
}
