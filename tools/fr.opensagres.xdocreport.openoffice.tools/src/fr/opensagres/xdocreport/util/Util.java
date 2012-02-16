/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.opensagres.xdocreport.util;

/**
 *
 * @author pascalleclercq
 */
public class Util {

    public static void startNewThread(ClassLoader classLoader, Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setContextClassLoader(classLoader);
            thread.start();
        }
}
