/*
 * (c) Copyright 2005-2013 JAXIO, http://www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-jsf2-primefaces:src/main/java/util/FlowDebugUtil.p.vm.java
 */
package ca.rsagroup.web.util;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Use this bean from your flow to print debug message in the console.
 */
@Named("out")
@Singleton
public class FlowDebugUtil {
    public void print(String msg) {
        System.out.println("[flow debug] " + msg);
    }
}
