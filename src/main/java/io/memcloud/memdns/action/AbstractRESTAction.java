//package io.memcloud.memdns.action;
//
//import java.io.IOException;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.struts2.StrutsStatics;
//
//import com.ku6.org.apache.struts2.dispatcher.mapper.MyRestActionSupportV2;
//import com.ku6.org.apache.struts2.rest.handler.HttpHeaders;
//import com.opensymphony.xwork2.ActionContext;
//import com.opensymphony.xwork2.ModelDriven;
//import io.memcloud.memdns.model.UnifiedResponse;
//import io.memcloud.utils.MyUtils;
//
//import io.downgoon.jresty.rest.struts2.action.UnifiedRestAction;
//
//public class AbstractRESTAction extends UnifiedRestAction {
//
//	private static final long serialVersionUID = 9156048853602603933L;
//
//	protected final Log log = LogFactory.getLog(this.getClass().getName());
//	
//	/**  必须是Map<String,String>类型 */
//	protected static final String REDIRECT_AQS_MAP = AbstractRESTAction.class.getSimpleName()+"#REDIRECT_AQS_MAP";
//	
//	protected final HttpServletRequest getHttpRequest(){		 
//		return (HttpServletRequest)ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
//	}
//	
//	
//	protected final void setRequestAttribute(String key,Object obj){		 
//		getHttpRequest().setAttribute(key, obj);
//	}
//	
//	protected final HttpServletResponse getHttpResponse(){		 
//		return (HttpServletResponse)ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
//	}
//	
//	/**
//	 * @return	true，表示拦截成功；false，表示拦截失败。
//	 * */
//	@SuppressWarnings("unchecked")
//	private boolean isRedirectIntercepted(Object responseMessageOverwrite, boolean appendParamWhenReidrect) {
//		String redirectURL = getParam("redirect");//参数Redirect是保留字，不能使用，一旦使用redirect输入参数，则忽略表现层格式协商，一律302
//		if(redirectURL==null) {
//			redirectURL = (String)getHttpRequest().getAttribute("redirect");//以便服务端可以控制（例如通过COOKIE传递重定向参数）
//		}
//		if(redirectURL != null && !"null".equals(redirectURL) && redirectURL.startsWith("http")) {
//			if(responseMessageOverwrite==null) {//响应对象
//				responseMessageOverwrite = this;
//			}
//			if(appendParamWhenReidrect) {//重定向时是否需要追加结果参数
//				if((responseMessageOverwrite instanceof ModelDriven)
//						&& (((ModelDriven)responseMessageOverwrite).getModel()  instanceof  UnifiedResponse) ) {
//						
//						UnifiedResponse up = (UnifiedResponse)((ModelDriven)responseMessageOverwrite).getModel();
//						if(up.getStatus() > 0) {
//							redirectURL = MyUtils.appendQS(redirectURL, "status", up.getStatus()+"");
//						}
//						if(up.getMessage() != null) {
//							redirectURL = MyUtils.appendQS(redirectURL, "message", up.getMessage());
//						}
//						if(up.getAttachment() != null) {
//							redirectURL = MyUtils.appendQS(redirectURL, "attachment", up.getAttachment().toString());//attachment采用toString方式，方便拓展其他参数
//						}
//				} 
//				//2011-05-12 支持Request.setAttribute方式设置重定向参数
//				Object redirectAQS = getHttpRequest().getAttribute(REDIRECT_AQS_MAP);
//				if(redirectAQS!=null && (redirectAQS instanceof Map)) {
//					redirectURL = MyUtils.appendQS(redirectURL, (Map<String,String>)redirectAQS);
//				}
//			}
//			
//			HttpServletResponse httpResponse = getHttpResponse();
//			try {
//				httpResponse.sendRedirect(redirectURL);
//			} catch (IOException ioe) {
//				log.warn("客户端（浏览器）可能已经关闭，HTTP响应IO失败：refnum="+getRefNum4Log()+", exception="+ioe.getMessage(),  ioe);
//			}
//			return true;//拦截成功
//		} 
//		else {
//			return false;//拦截失败
//		}
//	}
//	
//	public final String REST(String methodResult) {
//		return REST(methodResult,true);
//	}
//	/** 支持重定向 */
//	public final String REST(String methodResult, boolean appendParamWhenRedirect) {
//		if(isRedirectIntercepted(null,appendParamWhenRedirect)) {
//			return null;
//		}
//		return representation(methodResult);
//	}
//	public final String REST(HttpHeaders methodResult) {
//		return REST(methodResult,true);
//	}
//	/** 支持重定向 */
//	public final String REST(HttpHeaders methodResult,boolean appendParamWhenRedirect) {
//		if(isRedirectIntercepted(null,appendParamWhenRedirect)) {
//			return null;
//		}
//		return representation(methodResult);	
//	}
//	
//	public final String REST(HttpHeaders methodResult,
//			Object responseMessageOverwrite) {
//		return REST(methodResult,responseMessageOverwrite,true);
//	}
//	/** 支持重定向 */
//	public final String REST(HttpHeaders methodResult,
//			Object responseMessageOverwrite,boolean appendParamWhenRedirect) {
//		if(isRedirectIntercepted(responseMessageOverwrite,appendParamWhenRedirect)) {
//			return null;
//		}
//		return representation(methodResult,responseMessageOverwrite);
//	}
//	
//	public final String REST(String methodResult,
//			Object responseMessageOverwrite) {
//		return REST(methodResult,responseMessageOverwrite, true);
//	}
//
//	/** 支持重定向*/
//	public final String REST(String methodResult,
//			Object responseMessageOverwrite,boolean appendParamWhenRedirect) {
//		if(isRedirectIntercepted(responseMessageOverwrite,appendParamWhenRedirect)) {
//			return null;
//		}
//		return representation(methodResult, responseMessageOverwrite);
//	}
//
//}
