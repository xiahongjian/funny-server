package tech.hongjian.funnyserver.scanner;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import tech.hongjian.funnyserver.scanner.Test.B;
import tech.hongjian.funnyserver.util.scanner.ClassInfo;
import tech.hongjian.funnyserver.util.scanner.reader.ClassPathClassReader;
import tech.hongjian.funnyserver.util.scanner.reader.ClassReader;
import tech.hongjian.funnyserver.util.scanner.reader.DynamicContext;

/**
 * @author xiahongjian 
 * @time   2019-03-22 09:47:07
 *
 */
public class DynamicContextTest {
	
	@Test
	public void testDynamicContext() {
		ClassReader classReader = DynamicContext.getClassReader("tech.hongjian");
		Assert.assertEquals(ClassPathClassReader.class, classReader.getClass());
		Assert.assertEquals(true, DynamicContext.isJarPackage("junit.textui"));
		Assert.assertNotNull(DynamicContext.getClassReader("junit.textui"));
	}
	
	@Test
	public void testScanner() {
//		DynamicContext.recursionFindClasses("tech.hongjian.http_server").forEach(this::printClassName);
//		System.out.println("==========================================================");
//		Set<Class<?>> classes = new HashSet<Class<?>>() {{add(B.class);}};
//		DynamicContext.recursionFindClasses("tech.hongjian.http_server", classes, null).forEach(this::printClassName);
//		System.out.println("===========================================================");
//		DynamicContext.recursionFindClasses("tech.hongjian.http_server", new HashSet<Class<?>>() {{add(ClassReader.class); add(B.class);}}, null).forEach(this::printClassName);
//		System.out.println("===========================================================");
//		DynamicContext.recursionFindClasses("org.junit.validator", null, null).forEach(this::printClassName);
	}
	
	private void printClassName(ClassInfo info) {
		System.out.println(info.getClassName());
	}
}
