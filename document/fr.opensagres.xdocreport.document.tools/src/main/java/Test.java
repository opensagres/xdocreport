import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Test {

	public static void main(String[] args) {
		try {
			JSONObject j = new JSONObject().put("JSON", "Hello, World").put(
					"XXX", "eeee");

			System.err.println(j.toString(5));

			Map<String, Object> bean = new HashMap<String, Object>();
			Map<String, Object> project = new HashMap<String, Object>();
			project.put("name", "XXX");
			project.put("xx", "YYY");
			bean.put("project", project);
			
			List<Object> developers=new ArrayList<Object>();
			Map<String, Object> developer = new HashMap<String, Object>();
			developer.put("ccccc", "XXX");
			developer.put("dddddddd", "XXX");
			developers.add(developer);
			developers.add(developer);
			developers.add(developer);
			bean.put("developer", developers);
			
			j = new JSONObject(bean);
			System.err.println(j.toString(5));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
