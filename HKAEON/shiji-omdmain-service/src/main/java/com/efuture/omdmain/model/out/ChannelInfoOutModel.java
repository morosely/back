package com.efuture.omdmain.model.out;

public class ChannelInfoOutModel {
	
	private Long entId;
	private String  channelCode;
	private String channelName;
	private Short isMarketChannel;
	public Short getIsMarketChannel() {
		return isMarketChannel;
	}
	public void setIsMarketChannel(Short isMarketChannel) {
		this.isMarketChannel = isMarketChannel;
	}
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	
}
