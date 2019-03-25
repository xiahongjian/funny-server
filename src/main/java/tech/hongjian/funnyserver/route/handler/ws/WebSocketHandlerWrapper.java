package tech.hongjian.funnyserver.route.handler.ws;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.netty.util.concurrent.FastThreadLocal;
import tech.hongjian.funnyserver.annnotation.OnClose;
import tech.hongjian.funnyserver.annnotation.OnMessage;
import tech.hongjian.funnyserver.annnotation.OnOpen;
import tech.hongjian.funnyserver.util.scanner.BeanContainer;

/**
 * @author xiahongjian
 * @time 2019-03-22 16:09:36
 *
 */
public class WebSocketHandlerWrapper implements WebSocketHandler {

	private Map<String, Class<?>> handlers = new HashMap<>(4);
	private Map<String, Map<Class<? extends Annotation>, Method>> methodCache = new HashMap<>();
	private FastThreadLocal<String> path = new FastThreadLocal<>();

	public void setPath(String path) {
		this.path.set(path);
	}

	public WebSocketHandlerWrapper wrapHandler(String path, Class<?> handler) {
		Method[] methods = handler.getMethods();
		Map<Class<? extends Annotation>, Method> cache = new HashMap<>(3);
		cacheMethod(cache, methods, OnOpen.class);
		cacheMethod(cache, methods, OnMessage.class);
		cacheMethod(cache, methods, OnClose.class);
		if (cache.size() > 0) {
			methodCache.put(path, cache);
			handlers.put(path, handler);
		}
		return this;
	}

	private static void cacheMethod(Map<Class<? extends Annotation>, Method> cache, Method[] methods,
			Class<? extends Annotation> annotation) {
		List<Method> methodList = Stream.of(methods).filter(m -> m.isAnnotationPresent(annotation))
				.collect(Collectors.toList());
		if (methodList.size() == 1) {
			cache.put(annotation, methodList.get(0));
		} else if (methodList.size() > 1) {
			throw new RuntimeException("Duplicate annotation @" + annotation.getSimpleName() + " in lcass: " + methodList.get(0).getDeclaringClass().getName());
		}
	}

	@Override
	public void onConnect(WebSocketContext ctx) {
		invoke(ctx, OnOpen.class);
	}

	@Override
	public void onText(WebSocketContext ctx) {
		invoke(ctx, OnMessage.class);
	}

	@Override
	public void onDisconnect(WebSocketContext ctx) {
		invoke(ctx, OnClose.class);
	}

	private void invoke(WebSocketContext ctx, Class<? extends Annotation> event) {
		Map<Class<? extends Annotation>, Method> methodCache = this.methodCache.get(path.get());
		if (methodCache == null) {
			return;
		}
		
		Method method = methodCache.get(event);
		if (method == null) {
			return;
		}
		
		Class<?>[] paramTypes = method.getParameterTypes();
		Object[] params = new Object[paramTypes.length];
		
		for (int i = 0; i < paramTypes.length; i++) {
			Class<?> paramType = paramTypes[i];
			if (paramType == WebSocketContext.class) {
				params[i] = ctx;
			}
		}
		try {
			method.invoke(BeanContainer.get(handlers.get(path.get())), params);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Failed to invoke websocket handle method, in "+ method.getDeclaringClass().getName() + "#" + method.getName());
		}
		
	}

}