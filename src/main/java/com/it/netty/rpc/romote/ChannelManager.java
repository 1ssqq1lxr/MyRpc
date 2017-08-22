package com.it.netty.rpc.romote;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import com.it.netty.rpc.message.URI;
/**
 * channel 管理器
 * @author 17070680
 *
 */
public class ChannelManager {
		private boolean available ;
		private ChannelFuture channelFuture;
		private Channel channel;
		private URI uri;
		
		public ChannelManager(boolean available, ChannelFuture channelFuture,URI uri) {
			super();
			this.available = available;
			this.channelFuture = channelFuture;
		}
		

		public Channel getChannel() {
			return channelFuture!=null?channelFuture.channel():null;
		}
		public ChannelManager(ChannelFuture channelFuture,URI uri){
			this(false,channelFuture,uri);
		}

		public boolean isAvailable() {
			return channelFuture!=null&&channelFuture.channel().isActive();
		}


		public ChannelFuture getChannelFuture() {
			return channelFuture;
		}

		public void setChannelFuture(ChannelFuture channelFuture) {
			this.channelFuture = channelFuture;
		}
		
}
