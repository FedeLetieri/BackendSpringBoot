package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.WorldCApp
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer


class ServletInitializer : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(WorldCApp::class.java)
    }
}