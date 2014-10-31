package com.foxinmy.weixin4j.mp.response;

import java.io.Serializable;
import java.io.Writer;

import com.foxinmy.weixin4j.mp.type.ResponseType;
import com.foxinmy.weixin4j.msg.BaseMessage;
import com.foxinmy.weixin4j.util.ClassUtil;
import com.foxinmy.weixin4j.xml.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

/**
 * 响应消息基类
 * <p>
 * <font color="red">回复图片等多媒体消息时需要预先上传多媒体文件到微信服务器,
 * 假如服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试</font>
 * </p>
 * 
 * @className BaseResponse
 * @author jy.hu
 * @date 2014年4月6日
 * @since JDK 1.7
 */
public class BaseResponse implements Serializable {

	private static final long serialVersionUID = 7761192742840031607L;
	protected final static XStream xmlStream = XStream.get();
	private final static XStream jsonStream = new XStream(
			new JsonHierarchicalStreamDriver() {
				public HierarchicalStreamWriter createWriter(Writer writer) {
					return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
				}
			});

	@XStreamAlias("ToUserName")
	private String toUserName; // 开发者微信号
	@XStreamAlias("FromUserName")
	private String fromUserName; // 发送方帐号（一个OpenID）
	@XStreamAlias("CreateTime")
	private long createTime = System.currentTimeMillis(); // 消息创建时间 （整型）
	@XStreamAlias("MsgType")
	private ResponseType msgType; // 消息类型

	static {
		Class<?>[] classes = ClassUtil.getClasses(
				BaseResponse.class.getPackage()).toArray(new Class[0]);

		xmlStream.processAnnotations(classes);

		jsonStream.setMode(XStream.NO_REFERENCES);
		jsonStream.autodetectAnnotations(true);
		jsonStream.processAnnotations(classes);
	}

	public BaseResponse(ResponseType msgType) {
		this.msgType = msgType;
	}

	public BaseResponse(ResponseType msgType, BaseMessage inMessage) {
		this(msgType, inMessage.getFromUserName(), inMessage.getToUserName());
	}

	public BaseResponse(ResponseType msgType, String toUserName,
			String fromUserName) {
		this.msgType = msgType;
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public ResponseType getMsgType() {
		return msgType;
	}

	public void setMsgType(ResponseType msgType) {
		this.msgType = msgType;
	}

	/**
	 * 消息对象转换为微信服务器接受的xml格式消息
	 * 
	 * @return xml字符串
	 */
	public String toXml() {
		return xmlStream.toXML(this);
	}

	/**
	 * 消息对象转换为微信服务器接受的json格式字符串
	 * 
	 * @return json字符串
	 */
	public String toJson() {
		return jsonStream.toXML(this);
	}
}
