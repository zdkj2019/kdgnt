package com.nt.cache;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nt.utils.WebDateModel;

public class DataCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userId, username, title;
	private List<String> sqlList, menu;
	private String sql;
	private int bs;
	private String pgdxbm;
	private WebDateModel yyt;
	private WebDateModel whcs;
	private WebDateModel sbzl;
	private List<Map<String, String>> yytssudate, GZLB, sbbm, erji_GZLB, XQ;
	private String selectedId;
	private static DataCache dataCache;
	private String csz, tsxx;
	private boolean hasYHK;
	private ArrayList<Map<String, String>> data_xq, data_tc, data_JRLX;
	private Map<String, String> map, login_show_map;
	private int zzxx_num = 0;
	private int queryType = 0; // 查询类型，用于通用list跳转页面
	private byte[] imageByte;
	private File imgFile;

	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public byte[] getImageByte() {
		return imageByte;
	}

	public void setImageByte(byte[] imageByte) {
		this.imageByte = imageByte;
	}

	public ArrayList<Map<String, String>> getData_xq() {
		return data_xq;
	}

	public void setData_xq(ArrayList<Map<String, String>> data_xq) {
		this.data_xq = data_xq;
	}

	public ArrayList<Map<String, String>> getData_tc() {
		return data_tc;
	}

	public void setData_tc(ArrayList<Map<String, String>> data_tc) {
		this.data_tc = data_tc;
	}

	public ArrayList<Map<String, String>> getData_JRLX() {
		return data_JRLX;
	}

	public void setData_JRLX(ArrayList<Map<String, String>> data_JRLX) {
		this.data_JRLX = data_JRLX;
	}

	public List<Map<String, String>> getXQ() {
		return XQ;
	}

	public void setXQ(List<Map<String, String>> xQ) {
		XQ = xQ;
	}

	public List<Map<String, String>> getErji_GZLB() {
		return erji_GZLB;
	}

	public void setErji_GZLB(List<Map<String, String>> erji_GZLB) {
		this.erji_GZLB = erji_GZLB;
	}

	public List<Map<String, String>> getSbbm() {
		return sbbm;
	}

	public void setSbbm(List<Map<String, String>> sbbm) {
		this.sbbm = sbbm;
	}

	public int getBs() {
		return bs;
	}

	public void setBs(int bs) {
		this.bs = bs;
	}

	public String getCsz() {
		return csz;
	}

	public void setCsz(String csz) {
		this.csz = csz;
	}

	public static DataCache getinition() {
		if (dataCache == null) {
			dataCache = new DataCache();
		}
		return dataCache;

	}

	public List<Map<String, String>> getGZLB() {
		return GZLB;
	}

	public void setGZLB(List<Map<String, String>> gZLB) {
		GZLB = gZLB;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getSqlList() {
		return sqlList;
	}

	public void setSqlList(List<String> sqlList) {
		this.sqlList = sqlList;
	}

	public List<String> getMenu() {
		return menu;
	}

	public void setMenu(List<String> menu) {
		this.menu = menu;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getPgdxbm() {
		return pgdxbm;
	}

	public void setPgdxbm(String pgdxbm) {
		this.pgdxbm = pgdxbm;
	}

	public WebDateModel getYyt() {
		return yyt;
	}

	public void setYyt(WebDateModel yyt) {
		this.yyt = yyt;
	}

	public WebDateModel getWhcs() {
		return whcs;
	}

	public void setWhcs(WebDateModel whcs) {
		this.whcs = whcs;
	}

	public WebDateModel getSbzl() {
		return sbzl;
	}

	public void setSbzl(WebDateModel sbzl) {
		this.sbzl = sbzl;
	}

	public List<Map<String, String>> getYytssudate() {
		return yytssudate;
	}

	public void setYytssudate(List<Map<String, String>> yytssudate) {
		this.yytssudate = yytssudate;
	}

	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public boolean isHasYHK() {
		return hasYHK;
	}

	public void setHasYHK(boolean hasYHK) {
		this.hasYHK = hasYHK;
	}

	public int getZzxx_num() {
		return zzxx_num;
	}

	public void setZzxx_num(int zzxx_num) {
		this.zzxx_num = zzxx_num;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public String getTsxx() {
		return tsxx;
	}

	public void setTsxx(String tsxx) {
		this.tsxx = tsxx;
	}

	public Map<String, String> getLogin_show_map() {
		return login_show_map;
	}

	public void setLogin_show_map(Map<String, String> login_show_map) {
		this.login_show_map = login_show_map;
	}

}
