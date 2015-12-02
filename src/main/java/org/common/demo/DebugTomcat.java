package org.common.demo;

import org.apache.catalina.Context;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.deploy.ErrorPage;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 * Created by jiazhong on 12/2/15.
 */
public class DebugTomcat {
    public static void main(String[] args) throws Exception {

        int port = 8080;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }

        String webBase = new File("src/main/webapp").getAbsolutePath();
        if (new File(webBase, "WEB-INF").exists() == false) {
            throw new RuntimeException("In order to launch Kylin web app from IDE, please copy server/src/main/webapp/WEB-INF to  webapp/app/");
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(".");

        // Add AprLifecycleListener
        StandardServer server = (StandardServer) tomcat.getServer();
        AprLifecycleListener listener = new AprLifecycleListener();
        server.addLifecycleListener(listener);

        Context webContext = tomcat.addWebapp("/demo", webBase);
        ErrorPage notFound = new ErrorPage();
        notFound.setErrorCode(404);
        notFound.setLocation("/index.html");
        webContext.addErrorPage(notFound);
        webContext.addWelcomeFile("index.html");

        // tomcat start
        tomcat.start();
        tomcat.getServer().await();
    }
}
