package tech.hongjian.funnyserver.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * @author xiahongjian
 * @time 2019-03-25 10:27:00
 *
 */
public class ConsoleColorPattern extends ForegroundCompositeConverterBase<ILoggingEvent> {

	@Override
	protected String getForegroundColorCode(ILoggingEvent event) {
		Level level = event.getLevel();
		switch (level.toInt()) {
		case Level.ERROR_INT:
			return ANSIConstants.RED_FG;
		case Level.WARN_INT:
			return ANSIConstants.YELLOW_FG;
		case Level.INFO_INT:
			return ANSIConstants.BLUE_FG;
		case Level.DEBUG_INT:
			return ANSIConstants.GREEN_FG;
		case Level.TRACE_INT:
			return ANSIConstants.DEFAULT_FG;
		default:
			return ANSIConstants.DEFAULT_FG;
		}
	}

}
