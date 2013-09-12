package org.helios.netty.ajax.handlergroups.longpoll;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CACHE_CONTROL;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.List;

import org.apache.log4j.Logger;
import org.helios.netty.ajax.SharedChannelGroup;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;
import org.json.JSONObject;

/**
 * <p>Title: LongPollHandler</p>
 * <p>Description: The channel handler for long polling</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.netty.ajax.handlergroups.longpoll.LongPollHandler</code></p>
 * @ToDo: See http://stackoverflow.com/questions/2294010/long-polling-netty-nio-framework-java  
 */
@Sharable
public class LongPollHandler implements ChannelUpstreamHandler, ChannelDownstreamHandler {
	/** Instance logger */
	protected final Logger log = Logger.getLogger(getClass());
	/** The shared channel group instance */
	protected final SharedChannelGroup scg = SharedChannelGroup.getInstance();
	
	/**
	 * If the event is an HTTP request, add the channel to the shared channel group
	 * {@inheritDoc}
	 * @see org.jboss.netty.channel.ChannelUpstreamHandler#handleUpstream(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelEvent)
	 */
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if(e instanceof MessageEvent) {
			Object msg = ((MessageEvent)e).getMessage();
			if(msg instanceof HttpRequest) {
				Channel channel = e.getChannel();				
				Channel groupedChannel = scg.find(channel.getId());				
				if(groupedChannel==null) {
					HttpRequest request = (HttpRequest)msg;
					long timeout = getTimeout(request);
					channel = new TimeoutChannel(channel, timeout, true);
					scg.add(channel);
				} else {
					log.info("Tracking Existing Long Poller Channel [" + channel.getId() + "] from [" + channel.getRemoteAddress() + "]");
				}
			}
		}
		ctx.sendUpstream(e);
	}
	
	/**
	 * Determines the timeout for this long poll.
	 * @param req The HttpRequest
	 * @return the requested timeout, or forever if one was not found
	 */
	protected long getTimeout(HttpRequest req) {
		long tout = Long.MAX_VALUE;
		// First try the URL param
		QueryStringDecoder qp = new QueryStringDecoder(req.getUri());
		List<String> values = qp.getParameters().get("timeout");
		if(values!=null && values.size()>0) {
			try { tout = Long.parseLong(values.iterator().next().trim()); } catch (Exception e) {}
		}
		// If nothing then try the request header
		String tmp = req.getHeader("timeout");
		if(tmp!=null) {
			try { tout = Long.parseLong(tmp.trim()); } catch (Exception e) {}
		}		
		return tout;
	}

	/**
	 * {@inheritDoc}
	 * @see org.jboss.netty.channel.ChannelDownstreamHandler#handleDownstream(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelEvent)
	 */
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		final Channel channel = e.getChannel();
		if(!channel.isOpen()) return;
		if(!(e instanceof MessageEvent)) {
            ctx.sendDownstream(e);
            return;
        }
		Object message = ((MessageEvent)e).getMessage();
		if(!(message instanceof JSONObject) && !(message instanceof CharSequence)) {
            ctx.sendDownstream(e);
            return;			
		}
		
		ChannelBuffer cb = ChannelBuffers.copiedBuffer(message.toString(), CharsetUtil.UTF_8);
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		response.setHeader(CONTENT_LENGTH, cb.readableBytes());
		response.setHeader(CONTENT_TYPE, "application/json");
		response.setHeader(CACHE_CONTROL, "no-cache");
		response.setContent(cb);
		ChannelFuture cf = Channels.future(channel);
		ctx.sendDownstream(new DownstreamMessageEvent(channel, cf, response, channel.getRemoteAddress()));
		
	}


}
