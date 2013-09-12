package org.helios.netty.ajax;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import org.jboss.netty.logging.InternalLogLevel;

/**
 * <p>Title: ServerPipelineFactory</p>
 * <p>Description: The factory that creates pipelines for each connecting client. The handlers that are inserted into the pipeline
 * will be specific to the type of Ajax push that the client requests.</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.netty.ajax.ServerPipelineFactory</code></p>
 */

public class ServerPipelineFactory implements ChannelPipelineFactory {
	/** Instance logger */
	protected final Logger log = Logger.getLogger(getClass());
	/** The modifier map */
	protected final Map<String, PipelineModifier> modifierMap;
	/** The logging handler logger */
	protected final Logger logHandlerLogger = Logger.getLogger(LoggingHandler.class);
	
	/**
	 * Creates a new ServerPipelineFactory
	 * @param modifierMap The modifier map
	 */
	public ServerPipelineFactory(final Map<String, PipelineModifier> modifierMap) {
		this.modifierMap = modifierMap;
	}
	
	/**
	 * Adds a modifier to the factory
	 * @param name The URI that the modifier responds to
	 * @param modifier The modifier to add
	 */
	public void addModifier(String name, PipelineModifier modifier) {
		modifierMap.put(name, modifier);
	}
	
	/** The port unification pipeline switch */
	private final ProtocolSwitch ps = new ProtocolSwitch();
	
	/**
	 * {@inheritDoc}
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("protocolSwitch", ps);
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		if(logHandlerLogger.isDebugEnabled()) {
			pipeline.addLast("logger", new LoggingHandler(InternalLogLevel.INFO));
		}		
		pipeline.addLast(DefaultChannelHandler.NAME, new DefaultChannelHandler(modifierMap)); 
		if(log.isDebugEnabled()) log.debug("Created Pipeline [" + pipeline + "]");
		return pipeline;
	}

}
