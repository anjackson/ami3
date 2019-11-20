/**
 *    Copyright 2011 Peter Murray-Rust et. al.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.contentmine.graphics.html;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.contentmine.eucl.euclid.Util;
import org.contentmine.eucl.xml.XMLUtil;
import org.contentmine.graphics.html.util.HtmlUtil;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParentNode;
import nu.xom.Text;


/** 
 * @author pm286
 *
 <table border="1">
  <thead>
    <tr>
      <th>Month</th>
      <th>Savings</th>
    </tr>
  </thead>
  <tfoot>
    <tr>
      <td>Sum</td>
      <td>$180</td>
    </tr>
  </tfoot>
  <tbody>
    <tr>
      <td>January</td>
      <td>$100</td>
    </tr>
    <tr>
      <td>February</td>
      <td>$80</td>
    </tr>
  </tbody>
</table>
 */
public class HtmlTable extends HtmlElement {
	private final static Logger LOG = Logger.getLogger(HtmlTable.class);
	public final static String TAG = "table";
	public final static String ALL_TABLE_XPATH = ".//h:table";

	/** constructor.
	 * 
	 */
	public HtmlTable() {
		super(TAG);
	}
	
	public List<HtmlTr> getRows() {
		HtmlTbody tbody = this.getTbody();
		List<HtmlTr> rowList = new ArrayList<HtmlTr>();
		List<HtmlElement> rows = (tbody != null) ? tbody.getRows() : getChildElements(this, HtmlTr.TAG);
		for (HtmlElement el : rows) {
			rowList.add((HtmlTr) el);
		}
		return rowList;
	}

	public HtmlTbody getTbody() {
		return (HtmlTbody) getSingleChildElement(this, HtmlTbody.TAG); 
	}

	public HtmlTfoot getTfoot() {
		return (HtmlTfoot) getSingleChildElement(this, HtmlTfoot.TAG); 
	}

	public HtmlThead getThead() {
		return (HtmlThead) getSingleChildElement(this, HtmlThead.TAG); 
	}

	/** at present order depends on order in calling code
	 * 
	 * @return
	 */
	public HtmlTable ensureHeadBodyFoot() {
		getOrCreateThead();
		getOrCreateTbody();
		getOrCreateTfoot();
		
		return this;
	}

	public HtmlThead getOrCreateThead() {
		HtmlThead thead = getThead(); 
		if (thead == null) {
			thead = new HtmlThead();
			this.appendChild(thead);
		}
		return thead;
	}

	public HtmlTbody getOrCreateTbody() {
		HtmlTbody tbody = getTbody(); 
		if (tbody == null) {
			tbody = new HtmlTbody();
			this.appendChild(tbody);
		}
		return tbody;
	}

	public HtmlTfoot getOrCreateTfoot() {
		HtmlTfoot tfoot = getTfoot(); 
		if (tfoot == null) {
			tfoot = new HtmlTfoot();
			this.appendChild(tfoot);
		}
		return tfoot;
	}

	public HtmlTr getSingleLeadingTrThChild() {
		List<HtmlTr> rows = getRows();
		HtmlTr tr = null;
		if (rows.size() > 0) {
			// might be a <tbody>
			Nodes trthNodes = this.query(".//*[local-name()='tr' and *[local-name()='th']]");
			// some tables have more than one th row
			if (trthNodes.size() >= 1) {
//				Element elem = (Element) this.getChildElements().get(0);
//				if (elem.equals(trthNodes.get(0))) {
					tr = (HtmlTr) trthNodes.get(0);
//				}
			}	
		}
		return tr;
	}
	
	public List<HtmlTr> getTrTdRows() {
		List<HtmlTr> rows = new ArrayList<HtmlTr>();
		// might be a <tbody>
		Nodes trthNodes = this.query(".//*[local-name()='tr' and *[local-name()='td']]");
		for (int i = 0; i < trthNodes.size(); i++) {
			rows.add((HtmlTr)trthNodes.get(i));
		}
		return rows;
	}

	public void setBorder(int i) {
		this.addAttribute(new Attribute("border", ""+i));
	}

	public void addRow(HtmlTr row) {
		this.appendChild(row);
	}

	/** convenience method to extract list of HtmlTable in element
	 * 
	 * @param htmlElement
	 * @return
	 */
	public static List<HtmlTable> extractSelfAndDescendantTables(HtmlElement htmlElement) {
		return HtmlTable.extractTables(HtmlUtil.getQueryHtmlElements(htmlElement, ALL_TABLE_XPATH));
	}

	/** makes a new list composed of the tables in the list
	 * 
	 * @param elements
	 * @return
	 */
	public static List<HtmlTable> extractTables(List<HtmlElement> elements) {
		List<HtmlTable> tableList = new ArrayList<HtmlTable>();
		for (HtmlElement element : elements) {
			if (element instanceof HtmlTable) {
				tableList.add((HtmlTable) element);
			}
		}
		return tableList;
	}

	public static HtmlTable getFirstDescendantTable(HtmlElement htmlElement) {
		List<HtmlTable> tables = extractSelfAndDescendantTables(htmlElement);
		return (tables.size() == 0) ? null : tables.get(0);
	}

	/** convenience method to extract list of HtmlTables in element
	 * 
	 * @param htmlElement
	 * @param xpath
	 * @return
	 */

	public static List<HtmlTable> extractTables(HtmlElement htmlElement, String xpath) {
		return HtmlTable.extractTables(HtmlUtil.getQueryHtmlElements(htmlElement, xpath));
	}

	public static List<HtmlTable> extractTables(File tableFile) {
		HtmlElement htmlElement = HtmlElement.create(tableFile);
		return extractSelfAndDescendantTables(htmlElement);
	}

	/** gets a row of header values either from Thead or first tr[th] row.
	 *  MESSY because there are two different approaches 
	 * @return
	 */
	public HtmlTr getHeaderRow() {
		HtmlTr tr = null;
		HtmlThead thead = this.getThead();
		if (thead != null) {
			tr = thead.getOrCreateChildTr();
		} else {
			tr = this.getSingleLeadingTrThChild();
		}
		return tr;
	}

	public String getCaption() {
		List<Element> captions = XMLUtil.getQueryElements(this, "//*[local-name()='"+HtmlCaption.TAG+"']");
		return captions.size() == 0 ? null : captions.get(0).getValue().replaceAll("\n", " ").replaceAll("\\s+", " ");
	}

	public void normalizeWhitespace() {
		List<Node> textNodes = XMLUtil.getQueryNodes(this, ".//text()");
		for (Node textNode : textNodes) {
			ParentNode parentNode = textNode.getParent();
			String value = textNode.getValue();
			if (value != null) {
				value = Util.normalizeTrimWhitespace(value);
				parentNode.replaceChild(textNode, new Text(value));
			}
		}
	}

	public void addFooterSummary() {
		HtmlTfoot tfoot = this.getOrCreateTfoot();
//		RectangularTable rectTab = RectangularTable.
//		List<HtmlTr> trList = tfoot.getTrList();
//		if (trList.size() == 0) {
//			this.getCo
//		}
		
	}



}
