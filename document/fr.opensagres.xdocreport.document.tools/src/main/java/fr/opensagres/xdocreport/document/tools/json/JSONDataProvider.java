package fr.opensagres.xdocreport.document.tools.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.tools.IDataProvider;
import fr.opensagres.xdocreport.template.IContext;

public class JSONDataProvider implements IDataProvider {

	private JSONObject jsonObject;

	public JSONDataProvider(String jsonData) throws Exception {
		jsonObject = new JSONObject(jsonData);
	}

	public void populateContext(IXDocReport report, IContext context)
			throws IOException, XDocReportException {
		Iterator i = jsonObject.keys();
		while (i.hasNext()) {
			String key = (String) i.next();
			try {
				Object value = jsonObject.get(key);
				if (value instanceof JSONObject) {
					Map subBean = toMap((JSONObject) value);
					context.put(key, subBean);
				} else if (value instanceof JSONArray) {
					JSONArray array = (JSONArray) value;
					int length = array.length();
					List list = new ArrayList(length);
					for (int j = 0; j < length; j++) {
						Object itemValue = array.get(j);
						if (itemValue instanceof JSONObject) {
							list.add(toMap((JSONObject) itemValue));
						} else {
							list.add(itemValue);
						}
					}
					context.put(key, list);
				} else {
					context.put(key, value);
				}
			} catch (JSONException e) {
				throw new XDocReportException(e);
			}
		}
	}

	private Map toMap(JSONObject jsonObject) throws JSONException {
		Map parentBean = new HashMap();
		Iterator i = jsonObject.keys();
		while (i.hasNext()) {
			String key = (String) i.next();
			Object value = jsonObject.get(key);
			if (value instanceof JSONObject) {
				Map subBean = toMap((JSONObject) value);
				parentBean.put(key, subBean);
			} else if (value instanceof JSONArray) {
				JSONArray array = (JSONArray) value;
				int length = array.length();
				List list = new ArrayList(length);
				for (int j = 0; j < length; j++) {
					Object itemValue = array.get(j);
					if (itemValue instanceof JSONObject) {
						list.add(toMap((JSONObject) itemValue));
					} else {
						list.add(itemValue);
					}
				}
				parentBean.put(key, list);
			} else {
				parentBean.put(key, value);
			}
		}
		return parentBean;
	}
}
