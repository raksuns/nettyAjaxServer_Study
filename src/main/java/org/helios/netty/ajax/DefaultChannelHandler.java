package org.helios.netty.ajax;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * <p>Title: DefaultChannelHandler</p>
 * <p>Description: The initial and default channel handler inserted into all pipelines. This handler is intended to
 * examine the request URI and reconfigure the pipeline to handle the next request.</p> 
 * <p><code>org.helios.netty.ajax.DefaultChannelHandler</code></p>
 */

public class DefaultChannelHandler extends SimpleChannelUpstreamHandler {
	/** Instance logger */
	protected final Logger log = Logger.getLogger(getClass());

	/** The name of this handler in the pipeline */
	public static final String NAME = "router";

	protected final Map<String, PipelineModifier> modifierMap;
	/**
	 * Creates a new DefaultChannelHandler
	 * @param modifierMap The map of modifiers, keyed by the URI they accept.
	 */
	public DefaultChannelHandler(final Map<String, PipelineModifier> modifierMap) {
		this.modifierMap = modifierMap;		
	}
	
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {   
    	Object message = e.getMessage();
    	if(message instanceof HttpRequest) { 
	        HttpRequest request = (HttpRequest)message;
	        PipelineModifier modifier = getModifier(request.getUri());
	        if(!modifier.getName().equals(ctx.getAttachment())) {
	        	clearLastHandler(ctx.getPipeline());
	        	modifier.modifyPipeline(ctx.getPipeline());
	        	ctx.setAttachment(modifier.getName());
	        }
    	} else {
    		log.info("\n\t=====================\n\tNon HTTP Message Received\n\t" + message.getClass().getName() + "\n\t=====================\n");
    		ctx.sendDownstream(e);
    	}
        ctx.sendUpstream(e);
    }
    
    /**
     * Removes the last handler from the pipeline unless the last handler is this handler.
     * @param pipeline The pipeline to operate on
     */
    protected void clearLastHandler(ChannelPipeline pipeline) {
    	if(this!=pipeline.getLast()) {
    		pipeline.removeLast();
    	}
    }
    
    protected PipelineModifier getModifier(String uri) {
    	String[] frags = uri.trim().split("\\/");
    	for(String frag: frags) {
    		if(frag.trim().isEmpty()) continue;
    		PipelineModifier modifier = modifierMap.get(frag.trim());
    		if(modifier!=null) {
    			return modifier;
    		}
    	}
    	return modifierMap.get("");
    }

}
