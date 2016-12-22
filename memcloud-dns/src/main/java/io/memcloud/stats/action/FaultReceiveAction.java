package io.memcloud.stats.action;

import javax.annotation.Resource;

import io.memcloud.memdns.action.BaseAction;
import io.memcloud.memdns.util.CsvMedia;
import io.memcloud.stats.business.IMemInstanceMonitor;
/**
 * 
 * @author ganghuawang
 *
 */
public class FaultReceiveAction extends BaseAction  {

	private static final long serialVersionUID = 6213324767971884221L;
	
	@Resource(name = "memInstanceMonitor")
	private IMemInstanceMonitor memInstanceMonitor;
	
	@Override
	public String view()  {
		try {
			memInstanceMonitor.receiveFaultMessage(paramId);
		} catch (RuntimeException e) {
			log.warn("fault view exception",e);
			return CsvMedia.csv(getHttpResponse(),new StringBuffer("err"));
		}
		return CsvMedia.csv(getHttpResponse(),new StringBuffer("succ"));
	}


	protected Long paramId ;

	public Long getParamId() {
		return paramId;
	}

	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}

	
}
