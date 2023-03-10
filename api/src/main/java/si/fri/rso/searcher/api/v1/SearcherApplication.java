package si.fri.rso.searcher.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import com.kumuluz.ee.cors.annotations.CrossOrigin;

@OpenAPIDefinition(info = @Info(title = "searcher API", version = "v1",
        contact = @Contact(email = "rso@fri.uni-lj.si"),
        license = @License(name = "dev"), description = "API for managing searcher."),
        servers = @Server(url = "http://localhost:8081/"))
@ApplicationPath("/v1")
@CrossOrigin(supportedMethods = "GET, POST, HEAD, OPTIONS, PUT, DELETE")
public class SearcherApplication extends Application {

}
