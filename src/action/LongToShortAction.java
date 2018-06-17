package action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import domain.LongToShort;
import net.sf.json.JSONObject;
import service.LongToShortSerivce;
import utils.GenerateUUID;

public class LongToShortAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LongToShortSerivce ltsService;
	private String base = "localhost:8080/";//
	
	private String urlid;
	private String long_url;
	private String zidingyi;
	private String short_url;
	private String urlLength;//短链接的长度
	private String data;//该变量和ajax请求成功返回的data是一样一样的

	public String getUrlid() {
		return urlid;
	}

	public void setUrlid(String urlid) {
		this.urlid = urlid;
	}

	public LongToShortSerivce getLtsService() {
		return ltsService;
	}

	public void setLtsService(LongToShortSerivce ltsService) {
		this.ltsService = ltsService;
	}

	public String getLong_url() {
		return long_url;
	}

	public void setLong_url(String long_url) {
		this.long_url = long_url;
	}

	public String getZidingyi() {
		return zidingyi;
	}

	public void setZidingyi(String zidingyi) {
		this.zidingyi = zidingyi;
	}

	public String getShort_url() {
		return short_url;
	}

	public void setShort_url(String short_url) {
		this.short_url = short_url;
	}

	public String getUrlLength() {
		return urlLength;
	}

	public void setUrlLength(String urlLength) {
		this.urlLength = urlLength;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 根据长链接查询，
	 *     如果有，则返回
	 *     没有，则插入
	 * @throws IOException
	 */
	public void findAllByLong() throws IOException{
		String result = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		long_url = request.getParameter("long_url");
		urlLength = request.getParameter("urlLength");
		zidingyi = request.getParameter("zidingyi");
		urlid = request.getParameter("urlid");

		int length = Integer.parseInt(urlLength);

		List<LongToShort> list = ltsService.findAllByLong(long_url);
		LongToShort lts = new LongToShort();
		
		//长链接必不为空，如果自定义为空时
		if (zidingyi == null) {
			//根据长链接查询，判断是否已经存在
			if (list.size() == 0) {//如果为空，则将长链接插入数据库
				//还要判断规定的链接长度
				if (length == 6) {
					String uuid_6 = GenerateUUID.generateShortUuid_6();//得到6位的短链接
					StringBuffer short_url = new StringBuffer(base);
					short_url.append(uuid_6);

					lts.setLong_url(long_url);
					lts.setShort_url(short_url.toString());

					ltsService.insertLongAndShort(lts);
					result = short_url.toString();
				} else if(length == 8){
					String uuid_8 = GenerateUUID.generateShortUuid_8();//得到8位的短链接
					StringBuffer short_url = new StringBuffer(base);
					short_url.append(uuid_8);

					lts.setLong_url(long_url);
					lts.setShort_url(short_url.toString());

					ltsService.insertLongAndShort(lts);
					result = short_url.toString();
				}
			}else if(list.size() == 1){//如果不为空，将对应的短链接查出来并返回
				//还要判断规定的链接长度
				if (length == 6) {
					String uuid_6 = GenerateUUID.generateShortUuid_6();//得到6位的短链接
					StringBuffer short_url = new StringBuffer(base);
					short_url.append(uuid_6);
					
					lts.setId(Integer.parseInt(urlid));
					lts.setLong_url(long_url);
					lts.setShort_url(short_url.toString());

					ltsService.updateShortURL(lts);
					result = short_url.toString();
				} else if(length == 8){
					String uuid_8 = GenerateUUID.generateShortUuid_8();//得到8位的短链接
					StringBuffer short_url = new StringBuffer(base);
					short_url.append(uuid_8);
					
					lts.setId(Integer.parseInt(urlid));
					lts.setLong_url(long_url);
					lts.setShort_url(short_url.toString());

					ltsService.updateShortURL(lts);
					result = short_url.toString();
				}
			}
		} else {//如果自定义不为空时，插入数据库即可
			if (list.size() == 0) {
				StringBuffer short_url = new StringBuffer(base);
				short_url.append(zidingyi);
				
				lts.setLong_url(long_url);
				lts.setShort_url(short_url.toString());
				
				ltsService.insertLongAndShort(lts);
			} else if(list.size() == 1){
				StringBuffer short_url = new StringBuffer(base);
				short_url.append(zidingyi);
				
				lts.setId(Integer.parseInt(urlid));
				lts.setLong_url(long_url);
				lts.setShort_url(short_url.toString());
				
				ltsService.updateShortURL(lts);
			}
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("result", result);

		data = JSONObject.fromObject(map).toString();

		response.setContentType("text/json;charset=utf-8");
		response.getWriter().write(data);
	}

	/**
	 * 得到输入的短链接，重定向到相应的界面
	 * @throws IOException 
	 */
	public void findLongByShort() throws IOException{
		System.out.println("都进来了吗？");

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		String uri = request.getRequestURI();
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String lurl = "";

		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(serverName).append(":").append(port).append(uri);
		String tmep = sbuffer.toString();

		//根据短链接去查询
		List<LongToShort> list1 = ltsService.findLongByShort(tmep);

		if (list1 != null) {
			for (LongToShort l : list1) {
				lurl = l.getLong_url();
			}
			response.sendRedirect(lurl);
		} else {
			response.sendRedirect("localhost:8080/LongToShort/content/pages/404.html");
		}

	}

	/**
	 * 还原短链接，
	 * 得到短链接，从数据库中查找对应关系，返回长链接
	 * @throws IOException 
	 */
	public void reStoreShort() throws IOException{
		String result = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		String shorturl = request.getParameter("short_url");

		if (shorturl != null) {
			List<LongToShort> list = ltsService.findLongByShort(shorturl);
			if (list != null && list.size() == 1) {
				result = list.get(0).getLong_url();
				Map<String, String> map = new HashMap<String, String>();
				map.put("result", result);

				data = JSONObject.fromObject(map).toString();

				response.setContentType("text/json;charset=utf-8");
				response.getWriter().write(data);
			}
		} 

	}
	
	/**
	 * 查询链接的id
	 * @throws IOException
	 */
	public void findIdByLong() throws IOException{
		int url_id = 0;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		long_url = request.getParameter("long_url");
		
		List<LongToShort> list = ltsService.findAllByLong(long_url);
		if (long_url != null && list != null) {
			for (LongToShort l : list) {
				url_id = l.getId();
			}
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("urlid", String.valueOf(url_id));

		data = JSONObject.fromObject(map).toString();
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().write(data);
	}
	
	/**
	 * 根据自定义短链接查询是否存在
	 * @throws IOException
	 */
	public void findByShort() throws IOException{
		String result = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		zidingyi = request.getParameter("zidingyi");
		
		
		List<LongToShort> list = ltsService.findLongByShort(base+zidingyi);
		if (list.size() != 0) {
			result = "true";
		} else {
			result = "false";
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", result);

		data = JSONObject.fromObject(map).toString();
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().write(data);
		
	}

}
