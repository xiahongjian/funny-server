package tech.hongjian.funny_server.util.scanner.reader;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import lombok.extern.slf4j.Slf4j;
import tech.hongjian.funny_server.util.scanner.ClassInfo;

/**
 * @author xiahongjian
 * @time 2019-03-21 17:18:43
 *
 */
@Slf4j
public class JarClassReaser extends AbstractClassReader {
	private static final String JAR_FILE = "jar:file:";
	private static final String WSJAR_FILE = "wsjar:file:";

	@Override
	public Set<ClassInfo> filteClass(String packageName, Set<Class<?>> parents,
			Set<Class<? extends Annotation>> annotaions, boolean recursive) {
		final Set<ClassInfo> classes = new HashSet<>();

		String packageDir = packageName.replace(".", "/");
		Enumeration<URL> urls;
		try {
			urls = this.getClass().getClassLoader().getResources(packageDir);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				getClasses(url, packageDir, packageName, parents, annotaions, recursive, classes);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return classes;
	}

	private void getClasses(URL url, String packageDir, String packageName, Set<Class<?>> parents,
			Set<Class<? extends Annotation>> annotations, boolean recursive, Set<ClassInfo> classes) {
		String name = url.toString();
		if (!name.startsWith(JAR_FILE) && !name.startsWith(WSJAR_FILE))
			return;

		try {
			JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String jarName = entry.getName();
				if (name.charAt(0) == '/') {
					jarName = jarName.substring(1);
				}

				if (!jarName.startsWith(packageDir)) {
					continue;
				}

				int idx = jarName.lastIndexOf('/');
				if (idx != -1) {
					packageName = jarName.substring(0, idx).replace('/', '.');
				}

				if (idx == -1 && !recursive) {
					continue;
				}

				if (!jarName.endsWith(".class") || entry.isDirectory()) {
					continue;
				}

				String className = jarName.substring(packageName.length() + 1, jarName.length() - 6);
				Class<?> clazz = Class.forName(packageName + "." + className);

				determains(clazz, parents, annotations, classes);
			}
		} catch (IOException e) {
			log.error("Failed to scan package.", e);
		} catch (ClassNotFoundException e) {
			log.error("Class not found.", e);
		}
	}

}
