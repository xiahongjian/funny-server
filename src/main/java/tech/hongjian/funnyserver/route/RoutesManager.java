package tech.hongjian.funnyserver.route;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import tech.hongjian.funnyserver.annnotation.Controller;
import tech.hongjian.funnyserver.annnotation.WSContorller;
import tech.hongjian.funnyserver.config.ServerConfig;
import tech.hongjian.funnyserver.route.handler.RequestMapingHandler;
import tech.hongjian.funnyserver.route.handler.StataicResourceMappingHandler;
import tech.hongjian.funnyserver.route.handler.ws.WebSocketHandler;
import tech.hongjian.funnyserver.route.handler.ws.WebSocketHandlerWrapper;
import tech.hongjian.funnyserver.util.StringUtils;
import tech.hongjian.funnyserver.util.scanner.reader.DynamicContext;

/**
 * @author xiahongjian
 * @time 2019-03-04 14:47:25
 *
 */
@Slf4j
public class RoutesManager {
	private static final Map<Router, RequestMapingHandler> routeMap = new HashMap<>();
	private static final Map<String, WebSocketHandler> webSocketMap = new HashMap<>();
	static {
		init();
		printRoute();
	}

	public static void init() {
		handleStaticResource();
		// handleRequestMapping();
		handleWebSocket();
	}

	private static void handleWebSocket() {
		DynamicContext
				.recursionFindClasses(ServerConfig.getString(ServerConfig.WEBSOCKET_PACKAGE, ""), null, WSContorller.class)
				.forEach(info -> {
					WSContorller ws = info.getClazz().getAnnotation(WSContorller.class);
					String path = ws.value();
					webSocketMap.put(path, new WebSocketHandlerWrapper().wrapHandler(path, info.getClazz()));
				});

	}

	private static void handleRequestMapping() {
		Set<Class<? extends Annotation>> annotations = new HashSet<>();
		annotations.add(Controller.class);
		String[] packages = ServerConfig.getString(ServerConfig.CONROLLER_PACKAGE, "").split(",");
		Set<String> pkgNames = new HashSet<>();
		pkgNames.addAll(Arrays.asList(packages));
		DynamicContext.recursionFindClasses(pkgNames, null, annotations);

	}

	private static void handleStaticResource() {
		String pathes = ServerConfig.getString(ServerConfig.KEY_STATIC_PATH, "static");
		String prefix = ServerConfig.getString(ServerConfig.KEY_STATIC_URL_PREFIX, "");
		String[] allPathes = pathes.split(",");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		for (String path : allPathes) {
			URL url = classLoader.getResource(path);
			if (url == null)
				continue;
			File staticDir = new File(url.getPath());
			if (!staticDir.isDirectory()) {
				log.error("\"{}\" is not a right directory.", path);
				return;
			}
			File[] files = staticDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				resolveResource("", prefix, files[i]);
			}
		}
	}

	private static void resolveResource(String basePath, String prefix, File file) {
		if (file.isDirectory()) {
			basePath += "/" + file.getName();
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				resolveResource(basePath, prefix, files[i]);
			}
		} else {
			routeMap.put(Router.of(joinPath(prefix, basePath, file.getName()), "GET"),
					StataicResourceMappingHandler.of(file));
		}
	}

	private static String joinPath(String... pathes) {
		if (pathes == null)
			return null;
		return StringUtils.join("/", pathes).replaceAll("/{2,}", "/");
	}

	public static boolean hasResource(String url, HttpMethod method) {
		return getHandler(url, method) != null;
	}

	public static RequestMapingHandler getHandler(String url, HttpMethod method) {
		RequestMapingHandler handler = routeMap.get(Router.of(url, method.name()));
		if (handler == null) {
			handler = routeMap.get(Router.of(url, "*"));
		}
		return handler;
	}

	public static WebSocketHandler getWebSocket(String url) {
		return webSocketMap.get(url);
	}

	public static Response request(Request request) {
		RequestMapingHandler handler = getHandler(request.uri(), request.method());
		return handler == null ? Response.NOT_FOUND : handler.invok(request);
	}

	private static void printRoute() {
		routeMap.forEach((k, v) -> {
			log.info("{}: {}, handler: {}", k.path(), k.method(), v.toString());
		});
	}
}
