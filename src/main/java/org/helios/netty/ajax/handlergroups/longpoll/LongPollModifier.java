package org.helios.netty.ajax.handlergroups.longpoll;

import org.helios.netty.ajax.PipelineModifier;
import org.helios.netty.ajax.handlergroups.URIHandler;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;

/**
 * <p>Title: LongPollModifier</p>
 * <p>Description: Pipeline modifier for long polling</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.netty.ajax.handlergroups.longpoll.LongPollModifier</code></p>
 */
@URIHandler(uri={"lpoll"})
public class LongPollModifier implements PipelineModifier {
	/** The handler that this modifier adds at the end of the pipeline */
	protected final ChannelHandler handler = new LongPollHandler();
	/** The name of the handler this modifier adds */
	public static final String NAME = "lpoll";

	/**
	 * {@inheritDoc}
	 * @see org.helios.netty.ajax.PipelineModifier#getChannelHandler()
	 */
	@Override
	public ChannelHandler getChannelHandler() {
		return handler;
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see org.helios.netty.ajax.PipelineModifier#modifyPipeline(org.jboss.netty.channel.ChannelPipeline)
	 */
	@Override
	public void modifyPipeline(ChannelPipeline pipeline) {
		if(pipeline.get(NAME)==null) {
			pipeline.addLast(NAME, handler);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see org.helios.netty.ajax.PipelineModifier#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}

}
