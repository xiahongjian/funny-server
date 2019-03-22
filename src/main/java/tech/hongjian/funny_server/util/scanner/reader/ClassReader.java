package tech.hongjian.funny_server.util.scanner.reader;

import java.util.Set;

import tech.hongjian.funny_server.util.scanner.ClassInfo;
import tech.hongjian.funny_server.util.scanner.Scanner;

/**
 * @author xiahongjian 
 * @time   2019-03-21 15:10:12
 *
 */
public interface ClassReader {
	Set<ClassInfo> readClasses(Scanner scanner);
}
