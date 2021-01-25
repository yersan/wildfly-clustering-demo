package org.wildfly.s2i;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class JAXRSConfiguration extends Application {
}

