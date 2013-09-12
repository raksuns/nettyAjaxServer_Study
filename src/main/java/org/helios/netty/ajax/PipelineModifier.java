package org.helios.netty.ajax;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;

/**
 * <p>Title: PipelineModifier</p>
 * <p>Description: Defines a class that modifies a pipeline for a specific purpose</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.netty.ajax.PipelineModifier</code></p>
 */

public interface PipelineModifier {
	/**
	 * Modifies the passed pipeline to provide specific functionality
	 * @param pipeline The pipeline to modify
	 */
	public void modifyPipeline(ChannelPipeline pipeline);
	
	/**
	 * Returns the name of this modifier
	 * @return the name of this modifier
	 */
	public String getName();
	
	/**
	 * Returns the channel handler to insert into the pipeline
	 * @return the channel handler to insert into the pipeline
	 */
	public ChannelHandler getChannelHandler();
}
