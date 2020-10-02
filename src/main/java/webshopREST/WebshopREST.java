package webshopREST;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("webapi")
public class WebshopREST extends ResourceConfig {
    public WebshopREST() {   	
        register(RolesAllowedDynamicFeature.class);
    }
}
