/**
 * 
 */
package com.epam.training.persistence.dao.utils;

/**
 * @author Oleg_Burshinov
 * 
 */
public class Paging {
	// c ((pageNumber-1)* itemsCountOnPage+1)
	public static String getPage(int pageNumber, int itemsCountOnPage, String sql) {
		int startItem = ((pageNumber - 1) * itemsCountOnPage) + 1;
		int endItemm = startItem + itemsCountOnPage - 1;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM (SELECT rownum rnum, a.*  FROM (");
		sb.append(sql);
		sb.append(") a)  WHERE rnum BETWEEN "+ startItem +" AND " + endItemm);
		return sb.toString();
	}
}
