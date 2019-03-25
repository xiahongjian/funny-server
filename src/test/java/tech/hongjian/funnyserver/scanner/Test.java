package tech.hongjian.funnyserver.scanner;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author xiahongjian
 * @time 2019-03-21 13:49:08
 *
 */
public class Test {
	public static void main(String[] args) {
		determainSuperClass();
	}
	
	public static void determainSuperClass() {
		System.out.println(D.class.getSuperclass());
		
		System.out.println("a is assignalbe from d: " + A.class.isAssignableFrom(D.class));
		System.out.println("b is assignalbe from d: " + B.class.isAssignableFrom(D.class));
		System.out.println("c is assignalbe from d: " + C.class.isAssignableFrom(D.class));
	}
	
	public static class A {}
	
	public static class B extends A {}
	
	public static class C extends B {}
	
	public static class D extends C {}

	private static void testReadClass() {
		try {
			String pkg = "";
			String pkgDirPath = pkg.replace(".", "/");
			Enumeration<URL> resources;
			resources = Test.class.getClassLoader().getResources(pkgDirPath);
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				System.out.println(url.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
